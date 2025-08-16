package tech.clavem303.DTOs;

import tech.clavem303.entities.VehicleStatus;

public record VehicleResponseDTO(
        Long id,
        String brand,
        Integer fabricationYear,
        String engine,
        String model,
        VehicleStatus status,
        String carTitle
) {
}
