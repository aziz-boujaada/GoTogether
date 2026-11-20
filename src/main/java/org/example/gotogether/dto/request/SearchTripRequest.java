package org.example.gotogether.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record SearchTripRequest(

        @NotBlank(message = "Departure city is required")
        String departureCity,

        @NotBlank(message = "Arrival city is required")
        String arrivalCity,
        
        LocalDateTime departureDate,
        Integer requiredSeats,
        BigDecimal maxPrice
) {
}
