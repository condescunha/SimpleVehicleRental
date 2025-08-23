package tech.clavem303.DTOs.clients;

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
