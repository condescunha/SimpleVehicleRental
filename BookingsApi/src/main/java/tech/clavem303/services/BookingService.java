package tech.clavem303.services;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import tech.clavem303.DTOs.BookingCreateDTO;
import tech.clavem303.DTOs.BookingResponseDTO;
import tech.clavem303.DTOs.BookingUpdateStatusDTO;
import tech.clavem303.DTOs.clients.VehicleResponseDTO;
import tech.clavem303.DTOs.clients.VehicleStatus;
import tech.clavem303.entities.Booking;
import tech.clavem303.entities.BookingStatus;
import tech.clavem303.exceptions.BusinessRuleException;
import tech.clavem303.exceptions.ResourceNotFoundException;
import tech.clavem303.repositories.BookingRepository;
import tech.clavem303.services.clients.VehicleApiClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingService {
    @Inject
    BookingRepository bookingRepository;

    @Inject
    @RestClient
    VehicleApiClient vehicleApiClient;

    @Inject
    SecurityIdentity securityIdentity;

    @Transactional
    public BookingResponseDTO createBooking(BookingCreateDTO dto, String customerId) {
        validateBookingDates(dto.startDate(), dto.endDate());

        if (bookingRepository.isVehicleBooked(dto.vehicleId()))
            throw new BusinessRuleException("The vehicle is already booked for this period.");

        Optional<VehicleResponseDTO> vehicleOptional = vehicleApiClient.getVehicle(dto.vehicleId());

        if (vehicleOptional.isEmpty() || !VehicleStatus.AVAILABLE.equals(vehicleOptional.get().status()))
            throw new BusinessRuleException("It's not available for booking.");

        if (bookingRepository.isVehicleBooked(dto.vehicleId()))
            throw new BusinessRuleException("A concurrent booking was detected. Please try again.");

        String carTitle = vehicleOptional.map(VehicleResponseDTO::carTitle).orElse("N/A");

        Booking booking = new Booking(dto.vehicleId(), customerId, dto.startDate(), dto.endDate(), carTitle );
        bookingRepository.persist(booking);
        return toResponseDTO(booking);
    }

    public List<BookingResponseDTO> findBookingsByUser(String customerId) {
        List<Booking> bookings = bookingRepository.findByCustomerId(customerId);
        return bookings.stream()
                .map(BookingResponseDTO::of)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO findBookingById(Long id, String customerId) {
        Booking booking = findBookingOrThrow(id);

        if (securityIdentity.hasRole("user") && !booking.getCustomerId().equals(customerId))
            throw new ResourceNotFoundException("Booking not found or not authorized for this user.");

        return toResponseDTO(booking);
    }

    public List<BookingResponseDTO> findAllBookings() {
        return bookingRepository.listAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookingResponseDTO cancelBooking(Long id, BookingUpdateStatusDTO dto) {
        Booking booking = findBookingOrThrow(id);

        if(dto.status() != BookingStatus.CANCELED)
            throw new BusinessRuleException(
                    "Only 'CANCELED' status updates are allowed here. " +
                            "Use the specific check-in/check-out endpoints for other transitions."
            );
        booking.cancel();
        return toResponseDTO(booking);
    }

    @Transactional
    public BookingResponseDTO checkInBooking(Long id) {
        Booking booking = findBookingOrThrow(id);
        booking.activate();
        return toResponseDTO(booking);
    }

    @Transactional
    public BookingResponseDTO checkOutBooking(Long id) {
        Booking booking = findBookingOrThrow(id);
        booking.finish();
        return toResponseDTO(booking);
    }

    private Booking findBookingOrThrow(Long id) {
        Booking booking = bookingRepository.findById(id);
        if (booking == null) {
            throw new ResourceNotFoundException("Reservation with ID " + id + " not found.");
        }
        return booking;
    }

    private void validateBookingDates(LocalDate startDate, LocalDate endDate) {
        if (startDate.isBefore(LocalDate.now())) {
            throw new BusinessRuleException("The start date must be today or in the future.");
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessRuleException("The end date must be equal to or later than the start date.");
        }
    }

    private BookingResponseDTO toResponseDTO(Booking booking) {
        return new BookingResponseDTO(
                booking.getId(),
                booking.getVehicleId(),
                booking.getCustomerId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getStatus(),
                booking.getActivedAt(),
                booking.getFinishedAt(),
                booking.getCanceledAt(),
                booking.getCarTitle()
        );
    }

}