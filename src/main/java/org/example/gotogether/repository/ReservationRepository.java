package org.example.gotogether.repository;

import org.example.gotogether.entity.Reservation;
import org.example.gotogether.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation , Long> {
    @Query("""
     SELECT COALESCE(SUM(r.numberOfSeats), 0)
     FROM Reservation  r
     WHERE r.trip.id = :tripId
     AND r.departurePoint.stopOrder <= :segmentOrder
     AND r.arrivalPoint.stopOrder > :segmentOrder
     AND r.status = :status
""")
    Long countReservedSeatsBySegment(
           @Param("tripId") Long tripId ,
           @Param("segmentOrder") Integer segmentOrder,
           @Param("status") ReservationStatus status
    );
}
