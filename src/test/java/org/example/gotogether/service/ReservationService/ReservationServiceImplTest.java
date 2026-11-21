package org.example.gotogether.service;

import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.entity.Passenger;
import org.example.gotogether.entity.Reservation;
import org.example.gotogether.entity.StopPoint;
import org.example.gotogether.entity.Trip;
import org.example.gotogether.entity.User;
import org.example.gotogether.enums.ReservationStatus;
import org.example.gotogether.enums.TripStatus;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.example.gotogether.mapper.ReservationMapper;
import org.example.gotogether.repository.ReservationRepository;
import org.example.gotogether.repository.TripRepository;
import org.example.gotogether.repository.UserRepository;
import org.example.gotogether.service.ReservationService.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private TripRepository tripRepository;
    @Mock private ReservationRepository reservationRepository;
    @Mock private ReservationMapper reservationMapper;

    @InjectMocks private ReservationServiceImpl reservationService;

    private Passenger passenger;
    private Trip trip;
    private StopPoint departure;
    private StopPoint arrival;
    private CreateReservationRequestDto request;

    @BeforeEach
    void setUp() {
        passenger = new Passenger();
        passenger.setId(10L);

        departure = stopPoint(20L, 0, "0.00");
        arrival = stopPoint(21L, 1, "25.50");

        trip = new Trip();
        trip.setId(30L);
        trip.setStatus(TripStatus.PUBLIE);
        trip.setProposedSeats(4);
        trip.setStopPoints(List.of(departure, arrival));

        request = new CreateReservationRequestDto(2, 10L, 30L, 20L, 21L);
    }

    @Test
    void createReservationSavesConfirmedReservationWithCalculatedFare() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(passenger));
        when(tripRepository.findById(30L)).thenReturn(Optional.of(trip));
        when(reservationRepository.countReservedSeatsBySegment(30L, 0, ReservationStatus.CONFIRMEE))
                .thenReturn(1L);
        when(reservationRepository.countReservedSeatsBySegment(30L, 1, ReservationStatus.CONFIRMEE))
                .thenReturn(0L);
        when(reservationMapper.toEntity(request)).thenReturn(new Reservation());
        when(reservationRepository.save(any(Reservation.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        ReservationResponseDto expected = response(ReservationStatus.CONFIRMEE);
        when(reservationMapper.toResponseDto(any(Reservation.class))).thenReturn(expected);

        ReservationResponseDto result = reservationService.createReservation(request);

        assertSame(expected, result);
        ArgumentCaptor<Reservation> captor = ArgumentCaptor.forClass(Reservation.class);
        verify(reservationRepository).save(captor.capture());
        Reservation saved = captor.getValue();
        assertSame(passenger, saved.getPassenger());
        assertSame(trip, saved.getTrip());
        assertSame(departure, saved.getDeparturePoint());
        assertSame(arrival, saved.getArrivalPoint());
        assertEquals(new BigDecimal("51.00"), saved.getTotalPrice());
        assertEquals(ReservationStatus.CONFIRMEE, saved.getStatus());
        assertNotNull(saved.getReservationDate());
    }

    @Test
    void createReservationRejectsNonPassengerUsers() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(new User()));

        assertThrows(InvalidValueException.class, () -> reservationService.createReservation(request));
        verifyNoInteractions(tripRepository, reservationRepository, reservationMapper);
    }

    @Test
    void createReservationRejectsUnpublishedTrips() {
        trip.setStatus(TripStatus.COMPLET);
        when(userRepository.findById(10L)).thenReturn(Optional.of(passenger));
        when(tripRepository.findById(30L)).thenReturn(Optional.of(trip));

        assertThrows(InvalidValueException.class, () -> reservationService.createReservation(request));
        verifyNoInteractions(reservationRepository, reservationMapper);
    }

    @Test
    void createReservationRejectsInsufficientSeatsOnAnySegment() {
        when(userRepository.findById(10L)).thenReturn(Optional.of(passenger));
        when(tripRepository.findById(30L)).thenReturn(Optional.of(trip));
        when(reservationRepository.countReservedSeatsBySegment(30L, 0, ReservationStatus.CONFIRMEE))
                .thenReturn(3L);

        assertThrows(InvalidValueException.class, () -> reservationService.createReservation(request));
        verify(reservationRepository, never()).save(any());
        verifyNoInteractions(reservationMapper);
    }

    @Test
    void cancelReservationMarksFutureConfirmedReservationCancelled() {
        Reservation reservation = confirmedReservation(LocalDateTime.now().plusDays(2));
        when(reservationRepository.findById(40L)).thenReturn(Optional.of(reservation));

        reservationService.cancelReservation(40L);

        assertEquals(ReservationStatus.ANNULEE, reservation.getStatus());
        verify(reservationRepository).save(reservation);
    }

    @Test
    void cancelReservationRejectsDepartureWithin24Hours() {
        Reservation reservation = confirmedReservation(LocalDateTime.now().plusHours(23));
        when(reservationRepository.findById(40L)).thenReturn(Optional.of(reservation));

        assertThrows(InvalidValueException.class, () -> reservationService.cancelReservation(40L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservationRejectsAlreadyCancelledReservation() {
        Reservation reservation = confirmedReservation(LocalDateTime.now().plusDays(2));
        reservation.setStatus(ReservationStatus.ANNULEE);
        when(reservationRepository.findById(40L)).thenReturn(Optional.of(reservation));

        assertThrows(InvalidValueException.class, () -> reservationService.cancelReservation(40L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void cancelReservationReportsMissingReservation() {
        when(reservationRepository.findById(404L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservationService.cancelReservation(404L));
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void allReservationsMapsRepositoryResults() {
        Reservation reservation = new Reservation();
        ReservationResponseDto expected = response(ReservationStatus.CONFIRMEE);
        when(reservationRepository.findAllByPassengerId(10L)).thenReturn(List.of(reservation));
        when(reservationMapper.toResponseDto(reservation)).thenReturn(expected);

        assertEquals(List.of(expected), reservationService.allReservations(10L));
        verify(reservationRepository).findAllByPassengerId(10L);
    }

    @Test
    void calculTotalSubtractsDepartureFareAndMultipliesBySeatCount() {
        assertEquals(new BigDecimal("51.00"), reservationService.calculTotal(2, departure, arrival));
    }

    private StopPoint stopPoint(Long id, int order, String price) {
        StopPoint point = new StopPoint();
        point.setId(id);
        point.setStopOrder(order);
        point.setPriceFromDeparture(new BigDecimal(price));
        point.setPassageTime(LocalDateTime.now().plusDays(2));
        return point;
    }

    private Reservation confirmedReservation(LocalDateTime departureTime) {
        StopPoint futureDeparture = stopPoint(20L, 0, "0.00");
        futureDeparture.setPassageTime(departureTime);
        Reservation reservation = new Reservation();
        reservation.setStatus(ReservationStatus.CONFIRMEE);
        reservation.setDeparturePoint(futureDeparture);
        return reservation;
    }

    private ReservationResponseDto response(ReservationStatus status) {
        return new ReservationResponseDto(1L, 2, new BigDecimal("51.00"), LocalDateTime.now(),
                status, 10L, 30L, 20L, 21L);
    }
}
