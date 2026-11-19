package org.example.gotogether.controller;


import jakarta.validation.Valid;
import org.example.gotogether.dto.request.CreateTripRequestDto;
import org.example.gotogether.dto.response.TripResponseDto;
import org.example.gotogether.service.TripService.TripService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping
    public ResponseEntity<TripResponseDto> createTrip(
            @Valid @RequestBody CreateTripRequestDto request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tripService.createTrip(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TripResponseDto> updateTrip(
            @PathVariable Long id,
            @Valid @RequestBody CreateTripRequestDto request
    ) {
        return ResponseEntity.ok(
                tripService.updateTrip(request, id)
        );
    }

    @GetMapping
    public ResponseEntity<List<TripResponseDto>> getAllTrips() {
        return ResponseEntity.ok(tripService.allTrips());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TripResponseDto> getTripById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                tripService.findTripById(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrip(
            @PathVariable Long id
    ) {
        tripService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

