package tech.clavem303.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class BookingTest {

    private Booking booking;
    private final Long vehicleId = 1L;
    private final String customerId = "fake_customer";
    private final LocalDate startDate = LocalDate.now();
    private final LocalDate endDate = LocalDate.now().plusDays(5);
    private final String carTitle = "Ford Fusion V6";

    @BeforeEach
    public void setUp() {
        booking = new Booking(vehicleId, customerId, startDate, endDate, carTitle);
    }

    @Test
    public void testBookingCreation() {
        // Assert
        assertNotNull(booking);
        assertEquals(vehicleId, booking.getVehicleId());
        assertEquals(customerId, booking.getCustomerId());
        assertEquals(startDate, booking.getStartDate());
        assertEquals(endDate, booking.getEndDate());
        assertEquals(carTitle, booking.getCarTitle());
        assertEquals(BookingStatus.CREATED, booking.getStatus());
        assertNull(booking.getActivedAt());
        assertNull(booking.getCanceledAt());
        assertNull(booking.getFinishedAt());
    }

    @Test
    public void testActivateFromCreated() {
        // Act
        booking.activate();

        // Assert
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
        assertNotNull(booking.getActivedAt());
        assertTrue(booking.getActivedAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertNull(booking.getCanceledAt());
        assertNull(booking.getFinishedAt());
    }

    @Test
    public void testActivateFromNonCreatedThrowsException() {
        // Arrange
        booking.activate(); // Move to ACTIVE
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // Act
            booking.activate(); // Try to activate again
        });

        // Assert
        assertEquals("Activation only with CREATED status.", exception.getMessage());
        assertEquals(BookingStatus.ACTIVE, booking.getStatus());
    }

    @Test
    public void testCancelFromCreated() {
        // Act
        booking.cancel();

        // Assert
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
        assertNotNull(booking.getCanceledAt());
        assertTrue(booking.getCanceledAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertNull(booking.getActivedAt());
        assertNull(booking.getFinishedAt());
    }

    @Test
    public void testCancelFromActive() {
        // Arrange
        booking.activate(); // Move to ACTIVE

        // Act
        booking.cancel();

        // Assert
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
        assertNotNull(booking.getCanceledAt());
        assertTrue(booking.getCanceledAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertNotNull(booking.getActivedAt());
        assertNull(booking.getFinishedAt());
    }

    @Test
    public void testCancelFromFinishedThrowsException() {
        // Arrange
        booking.activate(); // Move to ACTIVE
        booking.finish();   // Move to FINISHED
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // Act
            booking.cancel(); // Try to cancel from FINISHED
        });

        // Assert
        assertEquals("Cancel only with CREATED or ACTIVE status.", exception.getMessage());
        assertEquals(BookingStatus.FINISHED, booking.getStatus());
    }

    @Test
    public void testFinishFromActive() {
        // Arrange
        booking.activate(); // Move to ACTIVE

        // Act
        booking.finish();

        // Assert
        assertEquals(BookingStatus.FINISHED, booking.getStatus());
        assertNotNull(booking.getFinishedAt());
        assertTrue(booking.getFinishedAt().isBefore(OffsetDateTime.now().plusSeconds(1)));
        assertNotNull(booking.getActivedAt());
        assertNull(booking.getCanceledAt());
    }

    @Test
    public void testFinishFromCreatedThrowsException() {
        // Act
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            booking.finish(); // Try to finish from CREATED
        });

        // Assert
        assertEquals("Finish only with ACTIVE status.", exception.getMessage());
        assertEquals(BookingStatus.CREATED, booking.getStatus());
    }

    @Test
    public void testFinishFromCanceledThrowsException() {
        // Arrange
        booking.cancel(); // Move to CANCELED
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            // Act
            booking.finish(); // Try to finish from CANCELED
        });

        // Assert
        assertEquals("Finish only with ACTIVE status.", exception.getMessage());
        assertEquals(BookingStatus.CANCELED, booking.getStatus());
    }

    @Test
    public void testDefaultConstructor() {
        // Arrange
        Booking emptyBooking = new Booking();

        // Assert
        assertNotNull(emptyBooking);
        assertNull(emptyBooking.getId());
        assertNull(emptyBooking.getVehicleId());
        assertNull(emptyBooking.getCustomerId());
        assertNull(emptyBooking.getStartDate());
        assertNull(emptyBooking.getEndDate());
        assertEquals(BookingStatus.CREATED, emptyBooking.getStatus());
        assertNull(emptyBooking.getActivedAt());
        assertNull(emptyBooking.getCanceledAt());
        assertNull(emptyBooking.getFinishedAt());
        assertNull(emptyBooking.getCarTitle());
    }
}
