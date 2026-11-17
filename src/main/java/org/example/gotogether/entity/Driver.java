package org.example.gotogether.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
public class Driver extends User {
    @Column(nullable = false , unique = true)
    private String permisNumber ;

    @OneToMany(mappedBy = "driver")
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(mappedBy = "driver")
    private List<Trip> trips = new ArrayList<>();
}
