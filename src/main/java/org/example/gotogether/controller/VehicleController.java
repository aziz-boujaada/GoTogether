
package org.example.gotogether.controller;

import jakarta.validation.Valid;
import org.example.gotogether.dto.request.CreateVehicleRequest;
import org.example.gotogether.dto.response.VehicleResponseDto;
import org.example.gotogether.entity.Vehicle;
import org.example.gotogether.service.VehicleService.VehicleService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<VehicleResponseDto> createVehicle(
            @Valid @RequestBody CreateVehicleRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.createVehicle(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(
            @PathVariable Long id,
            @Valid @RequestBody CreateVehicleRequest request
    ) {
        return ResponseEntity.ok(
                vehicleService.updateVehicle(request, id)
        );
    }

    @GetMapping
    public ResponseEntity<List<VehicleResponseDto>> getAllVehicles() {
        return ResponseEntity.ok(vehicleService.allVehicles());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(vehicleService.findVehicleById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(
            @PathVariable Long id
    ) {
        vehicleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
