package org.example.gotogether.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "passengers")
@Getter
@Setter
@NoArgsConstructor
public class Passenger extends User {
    @OneToMany(mappedBy = "passenger")
    private List<Reservation> reservations = new ArrayList<>();
}
