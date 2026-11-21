package org.example.gotogether.service.ReservationService;

import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.entity.*;
import org.example.gotogether.enums.ReservationStatus;
import org.example.gotogether.enums.TripStatus;
import org.example.gotogether.exception.InvalidValueException;
import org.example.gotogether.exception.ResourceNotFoundException;
import org.example.gotogether.mapper.ReservationMapper;
import org.example.gotogether.mapper.ReservationMapperImpl;
import org.example.gotogether.repository.ReservationRepository;
import org.example.gotogether.repository.TripRepository;
import org.example.gotogether.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService{

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper ;

    public ReservationServiceImpl(
            UserRepository userRepository,
            TripRepository tripRepository,
            ReservationRepository reservationRepository,
            ReservationMapper reservationMapper
    ){
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    @Override
    public ReservationResponseDto createReservation(CreateReservationRequestDto request) {

         Passenger passenger = getPassenger(request.passengerId());
         Trip trip = getTrip(request.tripId());

         if(trip.getStatus() != TripStatus.PUBLIE){
             throw new InvalidValueException("you can only reserved on published trips");
         }

         StopPoint departureCity = getDepartureCity(request.departurePointId(), trip);
         StopPoint arrivalCity = getArrivalCity(request.arrivalPointId(), trip);
         validateAvailableSeats(trip,departureCity ,arrivalCity , request.numberOfSeats());

        if(!isPointBelongsToTrip(trip ,departureCity , arrivalCity )){
            throw new InvalidValueException("Departure and arrival points must belong to the selected trip");
        }
        BigDecimal totalPrice = calculTotal(request.numberOfSeats(), departureCity ,arrivalCity);

        Reservation reservation = reservationMapper.toEntity(request);
        reservation.setPassenger(passenger);
        reservation.setTrip(trip);
        reservation.setDeparturePoint(departureCity);
        reservation.setArrivalPoint(arrivalCity);
        reservation.setReservationDate(LocalDateTime.now());
        reservation.setTotalPrice(totalPrice);
        reservation.setStatus(ReservationStatus.CONFIRMEE);

        return reservationMapper.toResponseDto(reservationRepository.save(reservation));
    }

    @Override
    public void cancelReservation(Long reservationId) {

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        if (reservation.getStatus() == ReservationStatus.ANNULEE) {
            throw new InvalidValueException(
                    "This reservation is already cancelled"
            );
        }


        if (reservation.getStatus() != ReservationStatus.CONFIRMEE) {
            throw new InvalidValueException(
                    "Only confirmed reservations can be cancelled"
            );

        }

            if (reservation.getDeparturePoint().getPassageTime()
                    .isBefore(LocalDateTime.now().plusHours(24))) {

                throw new InvalidValueException(
                        "You must cancel at least 24 hours before departure"
                );
            }

        reservation.setStatus(ReservationStatus.ANNULEE);

        reservationRepository.save(reservation);
    }

    @Override
    public List<ReservationResponseDto> allReservations(Long passengerId) {
        return reservationRepository.findAllByPassengerId(passengerId)
                .stream()
                .map(reservationMapper::toResponseDto)
                .toList();
    }

    @Override
    public BigDecimal calculTotal(Integer numberOfSeats, StopPoint departurePoint, StopPoint arrivalCity) {
        return arrivalCity.getPriceFromDeparture()
                .subtract(departurePoint.getPriceFromDeparture())
                .multiply(BigDecimal.valueOf(numberOfSeats));
    }

    private Passenger getPassenger(Long passengerId){
        User user = userRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found "));

        if(!(user instanceof Passenger passenger)){
            throw  new InvalidValueException("only passengers can make reservation");
        }
        return passenger;
    }

    private Trip getTrip(Long tripId){
       return tripRepository.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found "));
    }

    private StopPoint getDepartureCity(Long departurePointId , Trip trip){
        return trip.getStopPoints()
                .stream()
                .filter(point -> point.getId().equals(departurePointId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("this point not found"));
    }

    private StopPoint getArrivalCity(Long arrivalPointId , Trip trip){
        return trip.getStopPoints()
                .stream()
                .filter(point -> point.getId().equals(arrivalPointId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("this departure arrival point  not found"));
    }
    private boolean isPointBelongsToTrip(Trip trip , StopPoint departurePoint ,StopPoint arrivalCity ){

        boolean departurePointExists = false;
        boolean arrivalCityExists = false;

       for(StopPoint point : trip.getStopPoints()){
           if(point.getId().equals(departurePoint.getId())){
               departurePointExists = true;
           }

           if(point.getId().equals(arrivalCity.getId())){
               arrivalCityExists = true ;
           }

       }
       return departurePointExists && arrivalCityExists ;
    }


    private void validateAvailableSeats(
            Trip trip,
            StopPoint departurePoint,
            StopPoint arrivalPoint,
            Integer numberOfSeats
    ){
        for(int i = departurePoint.getStopOrder() ; i <= arrivalPoint.getStopOrder() ; i++){
            Long reservedSeats = reservationRepository.countReservedSeatsBySegment(
                    trip.getId(),
                    i,
                    ReservationStatus.CONFIRMEE
            );

            Long resetSeats = trip.getProposedSeats() - reservedSeats;
            if(resetSeats < numberOfSeats){
                throw new InvalidValueException("Not enough available seats");
            }
        }
    }


}
