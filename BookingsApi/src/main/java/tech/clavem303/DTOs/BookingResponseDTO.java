package tech.clavem303.DTOs;

import tech.clavem303.entities.Booking;
import tech.clavem303.entities.BookingStatus;

import java.time.LocalDate;

public record BookingResponseDTO(
        Long id,
        Long vehicleId,
        String customerId,
        LocalDate startDate,
        LocalDate endDate,
        BookingStatus status
) {
    public static BookingResponseDTO of(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getVehicleId(),
                booking.getCustomerId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus()
        );
    }
}
