package org.example.gotogether.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateStopPointsRequest(
        @NotBlank
        String city,

        @NotNull
        LocalDateTime passageTime,

        @NotNull
        @DecimalMin("0.0")
        BigDecimal priceFromDeparture
) {
}
