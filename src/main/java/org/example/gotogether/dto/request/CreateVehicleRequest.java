package org.example.gotogether.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.gotogether.entity.Driver;

public record CreateVehicleRequest(
       @NotNull(message = "driver id is required")
       Long driverId ,

       @NotBlank(message = "marque is required" )
       String marque ,

       @NotBlank(message = "modele is required" )
       String modele ,

       @NotBlank(message = "immatriculation is required")
       String immatriculation,

       @NotBlank(message = "color is required")
       String couleur,

       @NotNull(message = "Capacity is required")
       @Min(value = 1, message = "Capacity must be at least 1")
       Integer capacite
) {

}
