package org.example.gotogether.service.ReservationService;

import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.entity.Reservation;
import org.example.gotogether.entity.StopPoint;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface ReservationService {
    ReservationResponseDto createReservation(CreateReservationRequestDto request);
    void cancelReservation(Long reservationId);
    List<ReservationResponseDto> allReservations(Long passengerId);
    BigDecimal calculTotal(Integer numberOfSeats , StopPoint departurePoint , StopPoint arrivalCity);
}
