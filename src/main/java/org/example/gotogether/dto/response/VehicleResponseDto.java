package org.example.gotogether.dto.response;

public record VehicleResponseDto(
        Long id,
        String marque,
        String modele,
        String immatriculation,
        String couleur,
        Integer capacite,
        UserResponseDto driver
) {
}
