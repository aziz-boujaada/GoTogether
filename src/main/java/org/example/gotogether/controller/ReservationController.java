
package org.example.gotogether.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gotogether.dto.request.CreateReservationRequestDto;
import org.example.gotogether.dto.response.ReservationResponseDto;
import org.example.gotogether.service.ReservationService.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDto> createReservation(
            @Valid @RequestBody CreateReservationRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservationService.createReservation(request));
    }

    @GetMapping("/passenger/{passengerId}")
    public ResponseEntity<List<ReservationResponseDto>> allReservations(
            @PathVariable Long passengerId
    ) {
        return ResponseEntity.ok(
                reservationService.allReservations(passengerId)
        );
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelReservation(
            @PathVariable Long id
    ) {
        reservationService.cancelReservation(id);

        return ResponseEntity.noContent().build();
    }
}
