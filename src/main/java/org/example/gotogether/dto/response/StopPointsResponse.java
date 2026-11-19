package org.example.gotogether.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StopPointsResponse(


        Long id,

        String city,

        Integer stopOrder,

        LocalDateTime passageTime,

        BigDecimal priceFromDeparture

) {
}


