package org.example.gotogether.repository;

import org.example.gotogether.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TripRepository extends JpaRepository<Trip , Long> {
     boolean existsByVehicleIdAndDepartureDateBetween(
             Long vehicleId ,
             LocalDateTime startDate,
             LocalDateTime endDate
     );
}
