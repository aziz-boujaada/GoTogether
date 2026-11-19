package org.example.gotogether.dto.response;

import org.example.gotogether.enums.TripStatus;

import java.time.LocalDateTime;
import java.util.List;

public record TripResponseDto(
        Long id,

        LocalDateTime departureDate,

        Integer proposedSeats,

        TripStatus status,

        Long driverId,

        Long vehicleId,

        List<StopPointsResponse> stopPoints
) {
}
