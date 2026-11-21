package org.example.gotogether.dto.request;

import jakarta.validation.constraints.*;

public record CreateReservationRequestDto(
        @NotNull
        @Positive
        Integer numberOfSeats,

        @NotNull
        @Positive
        Long passengerId,

        @NotNull
        @Positive
        Long tripId,

        @NotNull
        @Positive
        Long departurePointId,

        @NotNull
        @Positive
        Long arrivalPointId
) {
}
