package tech.clavem303.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Singleton;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tech.clavem303.DTOs.BookingCreateDTO;
import tech.clavem303.DTOs.BookingResponseDTO;
import tech.clavem303.DTOs.clients.VehicleResponseDTO;
import tech.clavem303.DTOs.clients.VehicleStatus;
import tech.clavem303.entities.BookingStatus;
import tech.clavem303.services.BookingService;
import tech.clavem303.services.clients.VehicleApiClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestSecurity(user = "fake_customer", roles = "admin")
public class BookingResourceTest {

    @InjectMock
    BookingService bookingService;

    @Produces
    @Singleton
    VehicleApiClient getMockedVehicleApiClient() {
        VehicleApiClient mock = Mockito.mock(VehicleApiClient.class);

        VehicleResponseDTO mockVehicle = new VehicleResponseDTO(
                1L,
                "Ford",
                2024,
                "V6",
                "Fusion",
                VehicleStatus.AVAILABLE,
                "Ford Fusion V6"
        );
        when(mock.getVehicle(anyLong())).thenReturn(Optional.of(mockVehicle));

        return mock;
    }

    @Test
    public void testCreateBookingSuccessfully() {
        // Arrange
        BookingCreateDTO createDTO = new BookingCreateDTO(
                1L,
                LocalDate.now(),
                LocalDate.now().plusDays(5)
        );

        BookingResponseDTO responseDTO = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.CREATED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        Mockito
                .when(bookingService.createBooking(any(BookingCreateDTO.class), anyString()))
                .thenReturn(responseDTO);

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .body(createDTO)
                .when()
                .post("/bookings")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .body("id", is(1))
                .body("vehicleId", is(1))
                .body("status", is("CREATED"));
    }

    @Test
    @TestSecurity(user = "fake_customer", roles = "user")
    public void testGetAllBookingsAsUser() {
        // Arrange
        BookingResponseDTO userBooking = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.CREATED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        when(bookingService.findBookingsByUser("fake_customer")).thenReturn(List.of(userBooking));

        // Act & Assert
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/bookings")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$.size()", is(1))
                .body("[0].id", is(1))
                .body("[0].customerId", is("fake_customer"));
    }

    @Test
    public void testGetAllBookingsAsAdmin() {
        // Arrange
        BookingResponseDTO booking1 = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.CREATED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        BookingResponseDTO booking2 = new BookingResponseDTO(
                2L,
                2L,
                "another_customer",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(6),
                BookingStatus.ACTIVE,
                null,
                null,
                null,
                "Tesla Model 3"
        );

        when(bookingService.findAllBookings()).thenReturn(List.of(booking1, booking2));

        // Act & Assert
        given()
                .when()
                .get("/bookings")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("$.size()", is(2))
                .body("[0].id", is(1))
                .body("[1].id", is(2));
    }

    @Test
    public void testGetBookingByIdSuccessfully() {
        // Arrange
        BookingResponseDTO responseDTO = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.CREATED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        when(bookingService.findBookingById(1L, "fake_customer")).thenReturn(responseDTO);

        // Act & Assert
        given()
                .when()
                .get("/bookings/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(1))
                .body("status", is("CREATED"));
    }

    @Test
    public void testCancelBookingSuccessfully() {
        // Arrange
        BookingResponseDTO canceledDTO = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.CANCELED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        when(bookingService.cancelBooking(1L)).thenReturn(canceledDTO);

        // Act & Assert
        given()
                .when()
                .patch("/bookings/cancel/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(1))
                .body("status", is("CANCELED"));
    }

    @Test
    public void testCheckInBookingSuccessfully() {
        // Arrange
        BookingResponseDTO activeDTO = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.ACTIVE,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        when(bookingService.checkInBooking(1L)).thenReturn(activeDTO);

        // Act & Assert
        given()
                .when()
                .patch("/bookings/checkin/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(1))
                .body("status", is("ACTIVE"));
    }

    @Test
    public void testCheckOutBookingSuccessfully() {
        // Arrange
        BookingResponseDTO finishedDTO = new BookingResponseDTO(
                1L,
                1L,
                "fake_customer",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                BookingStatus.FINISHED,
                null,
                null,
                null,
                "Ford Fusion V6"
        );

        when(bookingService.checkOutBooking(1L)).thenReturn(finishedDTO);

        // Act & Assert
        given()
                .when()
                .patch("/bookings/checkout/1")
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", is(1))
                .body("status", is("FINISHED"));
    }

    // Additional failure test example: Unauthorized access to cancel as user
    @Test
    @TestSecurity(user = "fake_customer", roles = "user")
    public void testCancelBookingUnauthorized() {
        // Act & Assert
        given()
                .when()
                .patch("/bookings/cancel/1")
                .then()
                .statusCode(Response.Status.FORBIDDEN.getStatusCode());
    }
}
