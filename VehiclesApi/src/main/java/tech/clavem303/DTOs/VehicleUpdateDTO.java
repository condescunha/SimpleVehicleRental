package tech.clavem303.DTOs;

public record VehicleUpdateDTO(
        String brand,
        Integer fabricationYear,
        String engine,
        String model
) {
}
