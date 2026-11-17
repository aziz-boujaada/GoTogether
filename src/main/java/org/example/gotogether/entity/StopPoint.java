package org.example.gotogether.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "stop_points")
@Getter
@Setter
@NoArgsConstructor
public class StopPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private Integer stopOrder;
    private LocalDateTime passageTime;
    private BigDecimal priceFromDeparture;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @OneToMany(mappedBy = "departurePoint")
    private List<Reservation> departureReservations = new ArrayList<>();

    @OneToMany(mappedBy = "arrivalPoint")
    private List<Reservation> arrivalReservations = new ArrayList<>();
}
