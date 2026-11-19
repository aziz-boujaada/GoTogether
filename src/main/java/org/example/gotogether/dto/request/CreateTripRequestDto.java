package org.example.gotogether.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record CreateTripRequestDto(
        @NotNull(message = "Departure date is required")
        @Future(message = "Departure date must be in the future")
        LocalDateTime departureDate,

        @NotNull(message = "Proposed seats is required")
        @Min(value = 1, message = "At least one seat is required")
        Integer proposedSeats,

        @NotNull(message = "Vehicle ID is required")
        @Positive
        Long vehicleId,

        @NotNull
        @Size(min = 2 , message = "trip must contain at least 2 points to start")
        List<@NotNull @Valid CreateStopPointsRequest> pointsRequests
) {
}
