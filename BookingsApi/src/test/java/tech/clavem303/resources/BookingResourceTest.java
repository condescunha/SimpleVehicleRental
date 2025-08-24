package tech.clavem303.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
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
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@QuarkusTest
public class BookingResourceTest {

    @InjectMock
    BookingService bookingService;

    VehicleApiClient vehicleApiClient;

    @Produces
    @Singleton
    VehicleApiClient getMockedVehicleApiClient() {
        VehicleApiClient mock = Mockito.mock(VehicleApiClient.class);

        VehicleResponseDTO mockVehicle = new VehicleResponseDTO(
                VehicleStatus.AVAILABLE, // Apenas o status é necessário
                "123-ABC"                // E apenas o carTitle
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
                "Ford Fusion"
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
}
