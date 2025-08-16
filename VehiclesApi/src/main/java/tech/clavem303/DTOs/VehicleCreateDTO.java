package tech.clavem303.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleCreateDTO(
        @NotBlank(message = "Mandatory brand.")
        String brand,
        @NotNull(message = "Year of manufacture is mandatory.")
        Integer fabricationYear,
        @NotBlank
        String engine,
        @NotBlank
        String model
) {
}
