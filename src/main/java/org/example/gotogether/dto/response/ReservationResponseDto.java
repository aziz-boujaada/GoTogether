package org.example.gotogether.dto.response;

import org.example.gotogether.enums.ReservationStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationResponseDto(

        Long id,

        Integer numberOfSeats,

        BigDecimal totalPrice,

        LocalDateTime reservationDate,

        ReservationStatus status,

        Long passengerId,

        Long tripId,

        Long departurePointId,

        Long arrivalPointId

) {}


