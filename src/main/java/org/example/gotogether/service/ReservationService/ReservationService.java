package org.example.gotogether.service.ReservationService;

import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.entity.Reservation;
import org.example.gotogether.entity.StopPoint;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationService {
    ReservationResponseDto createReservation(CreateReservationRequestDto request);
    void cancelReservation(Reservation reservation);
    List<ReservationResponseDto> allReservations(Long passengerId);
    BigDecimal calculTotal(Integer numberOfSeats , StopPoint departurePoint , StopPoint arrivalCity);
}
