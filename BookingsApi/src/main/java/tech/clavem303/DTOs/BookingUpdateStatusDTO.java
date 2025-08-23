package tech.clavem303.DTOs;

import jakarta.validation.constraints.NotNull;
import tech.clavem303.entities.BookingStatus;

public record BookingUpdateStatusDTO(
        @NotNull
        BookingStatus status
) {
}
