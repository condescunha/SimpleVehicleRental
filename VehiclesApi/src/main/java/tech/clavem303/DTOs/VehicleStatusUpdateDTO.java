package tech.clavem303.DTOs;

import jakarta.validation.constraints.NotNull;
import tech.clavem303.entities.VehicleStatus;

public record VehicleStatusUpdateDTO(
        @NotNull
        VehicleStatus status
) { }
