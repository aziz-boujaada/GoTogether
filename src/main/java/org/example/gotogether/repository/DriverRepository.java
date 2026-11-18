package org.example.gotogether.repository;

import org.example.gotogether.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver , Long> {
    boolean existsByPermisNumber(String permisNumber);
}
