package org.example.gotogether.repository;

import org.example.gotogether.entity.Trip;
import org.example.gotogether.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TripRepository extends JpaRepository<Trip , Long> {
     boolean existsByVehicleIdAndStatusNot(
             Long vehicleId,
             TripStatus status
     );
}
