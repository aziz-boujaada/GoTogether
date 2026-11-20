package org.example.gotogether.repository;

import org.example.gotogether.entity.Trip;
import org.example.gotogether.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip , Long> {
     boolean existsByVehicleIdAndStatusNot(
             Long vehicleId,
             TripStatus status
     );

     @Query(""" 
                   SELECT DISTINCT t
                      FROM Trip t
                      JOIN t.stopPoints departure
                      JOIN t.stopPoints arrival
                      WHERE
             
                                LOWER(departure.city) = LOWER(:departureCity)
             
             
                                 AND LOWER(arrival.city) = LOWER(:arrivalCity)
             
                               AND departure.stopOrder < arrival.stopOrder
             
                                AND (CAST(:startDate AS LocalDateTime) IS NULL
                                    OR departure.passageTime >= :startDate)
             
                                AND (CAST(:requiredSeats AS Integer) IS NULL
                                    OR t.proposedSeats >= :requiredSeats)
             
                                AND (CAST(:maxPrice AS BigDecimal) IS NULL
                                    OR (arrival.priceFromDeparture -
                                        departure.priceFromDeparture) <= :maxPrice)
             
                                AND t.status = :status
             """)
     List<Trip> searchForTrips(
             @Param("departureCity") String departureCity,
             @Param("arrivalCity") String arrivalCity,
             @Param("startDate")LocalDateTime startDate,
             @Param("requiredSeats")Integer requiredSeats,
             @Param("maxPrice")BigDecimal maxPrice,
             @Param("status") TripStatus status
             );
}
