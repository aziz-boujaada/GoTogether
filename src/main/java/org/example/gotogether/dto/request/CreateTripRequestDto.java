package org.example.gotogether.dto.request;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record CreateTripRequestDto(
        @NotNull(message = "Departure date is required")
        @Future(message = "Departure date must be in the future")
        LocalDateTime departureDate,

        @NotNull(message = "Proposed seats is required")
        @Min(value = 1, message = "At least one seat is required")
        Integer proposedSeats,

        @NotNull(message = "Vehicle ID is required")
        @Positive
        Long vehicleId
) {
}
