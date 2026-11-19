package org.example.gotogether.repository;

import org.example.gotogether.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TripRepository extends JpaRepository<Trip , Long> {
}
