package tech.clavem303.resources;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import tech.clavem303.DTOs.VehicleResponseDTO;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@QuarkusTest
public class VehicleResourceTest {

    @Test
    public void testCreateVehicleWithValidYearReturnsCreated() {
        createDefaultVehicle();
    }

    @Test
    public void testCreateVehicleWithFutureYearReturnsBadRequest() {
        String body = """
            {
                "brand": "Honda",
                "fabricationYear": 2026,
                "engine": "1.5",
                "model": "New City Hatchback"
            }
            """;
        // Send the POST request.
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/vehicles")
                .then()
                // 3. Verificar a resposta
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(containsString("Invalid year (future)."));
    }

    @Test
    public void testCreateVehicleWithOldYearReturnsBadRequest() {
        int oldYear = LocalDate.now().minusYears(4).getYear();
        String body = String.format("""
            {
                "brand": "Honda",
                "fabricationYear": %d,
                "engine": "1.5",
                "model": "New City Hatchback"
            }
            """, oldYear);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/vehicles")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(containsString("Invalid year (max three-year-old)."));
    }

    @Test
    public void testCreateVehicleWithBlankBrandReturnsBadRequest() {
        int currentYear = LocalDate.now().getYear();
        String body = String.format("""
            {
                "brand": "",
                "fabricationYear": %d,
                "engine": "1.5",
                "model": "New City Hatchback"
            }
            """, currentYear);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/vehicles")
                .then()
                .statusCode(Response.Status.BAD_REQUEST.getStatusCode())
                .body(containsString("Mandatory brand."));
    }

    @Test
    public void testGetAllVehicles() {
        createDefaultVehicle();
        // Act & Assert: Verify that the GET request returns status 200 and the vehicle is created.
        given()
                .when()
                .get("/vehicles")
                .then()
                .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    public void testGetVehicleByIdReturnsOk() {
        VehicleResponseDTO createdVehicle = createDefaultVehicle();
        // Act & Assert: Makes the GET request with the extracted ID.
        given()
                .when()
                .get("/vehicles/{id}", createdVehicle.id())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("id", equalTo(createdVehicle.id().intValue()))
                .body("brand", equalTo(createdVehicle.brand()))
                .body("model", equalTo(createdVehicle.model()));
    }

    @Test
    public void testGetVehicleByInvalidIdReturnsNotFound() {
        Long nonExistentId = 9999L;
        // Act & Assert: Attempts to search for a vehicle with an invalid ID.
        given()
                .when()
                .get("/vehicles/{id}", nonExistentId)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode())
                .body(containsString("Vehicle with ID " + nonExistentId + " not found."));
    }

    @Test
    public void testPatchUpdateVehiclePartial() {
        VehicleResponseDTO createdVehicle = createDefaultVehicle();
        // Act: Makes the PATCH request to update only the model.
        String newModel = "Voyage";
        String body = String.format("""
            {
                "model": "%s"
            }
        """, newModel);
        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/vehicles/{id}", createdVehicle.id())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("model", equalTo(newModel))
                .body("brand", equalTo(createdVehicle.brand()))
                .body("fabricationYear", equalTo(createdVehicle.fabricationYear()));
    }

    @Test
    public void testPatchUpdateVehicleWithInvalidIdReturnsNotFound() {
        Long nonExistentId = 9999L;
        String body = "{\"model\": \"Corolla\"}";

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .patch("/vehicles/{id}", nonExistentId)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testPutUpdateStatusToUnderMaintenance() {
        VehicleResponseDTO createdVehicle = createDefaultVehicle();
        // Act & Assert: Try changing the status to 'UNDER MAINTENANCE'.
        String newStatus = "UNDER_MAINTENANCE";
        given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"status\": \"%s\"}", newStatus))
                .when()
                .put("/vehicles/{id}/status", createdVehicle.id())
                .then()
                .statusCode(Response.Status.OK.getStatusCode())
                .body("status", equalTo(newStatus));
    }

    @Test
    public void testPutUpdateStatusInvalidTransitionReturnsConflict() {
        VehicleResponseDTO createdVehicle = createDefaultVehicle();
        // Act & Assert: Try changing the status back to 'AVAILABLE'.
        String newStatus = "AVAILABLE";
        given()
                .contentType(ContentType.JSON)
                .body(String.format("{\"status\": \"%s\"}", newStatus))
                .when()
                .put("/vehicles/{id}/status", createdVehicle.id())
                .then()
                .statusCode(Response.Status.CONFLICT.getStatusCode())
                .body(containsString("From 'AVAILABLE' to 'UNDER_MAINTENANCE' only."));
    }

    @Test
    public void testDeleteVehicleById() {
        VehicleResponseDTO createdVehicle = createDefaultVehicle();
        // Act & Assert 1: Make the DELETE request and check the 204 response.
        given()
                .when()
                .delete("/vehicles/{id}", createdVehicle.id())
                .then()
                .statusCode(Response.Status.NO_CONTENT.getStatusCode());
        // Assert 2: Try searching for the deleted vehicle to ensure it no longer exists.
        given()
                .when()
                .get("/vehicles/{id}", createdVehicle.id())
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testDeleteNonExistentVehicleReturnsNotFound() {
        Long nonExistentId = 9999L;

        given()
                .when()
                .delete("/vehicles/{id}", nonExistentId)
                .then()
                .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    private VehicleResponseDTO createDefaultVehicle() {
        int currentYear = LocalDate.now().getYear();

        String body = String.format("""
            {
                "brand": "Volkswagen",
                "fabricationYear": %d,
                "engine": "1.6",
                "model": "Tiguan"
            }
        """, currentYear);

        return given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/vehicles")
                .then()
                .statusCode(Response.Status.CREATED.getStatusCode())
                .extract().body().as(VehicleResponseDTO.class);
    }
}
