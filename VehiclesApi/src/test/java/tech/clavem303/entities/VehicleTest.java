package tech.clavem303.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tech.clavem303.DTOs.VehicleUpdateDTO;
import tech.clavem303.exceptions.BusinessRuleException;

import java.time.LocalDate;

public class VehicleTest {

    @Test
    public void testCreateVehicleWithValidYear() {
        int currentYear = LocalDate.now().getYear();
        Vehicle vehicle = new Vehicle("Honda", currentYear, "1.5", "City");
        // Checks if the object is not null and if the year was set correctly.
        Assertions.assertNotNull(vehicle);
        Assertions.assertEquals(currentYear, vehicle.getFabricationYear());
    }

    @Test
    public void testCreateVehicleWithFutureYearThrowsException() {
        int futureYear = LocalDate.now().plusYears(1).getYear();
        // Checks if the exception is thrown.
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle("Honda", futureYear, "1.5", "City")
        );
    }

    @Test
    public void testCreateVehicleWithOldYearThrowsException() {
        int oldYear = LocalDate.now().minusYears(4).getYear();
        // Checks if the exception is thrown.
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new Vehicle("Honda", oldYear, "1.5", "City")
        );
    }

    @Test
    public void testChangeStatusFromAvailableToUnderMaintenance() {
        Vehicle vehicle = new Vehicle("Honda", LocalDate.now().getYear(), "1.5", "City");
        vehicle.changeStatus(VehicleStatus.UNDER_MAINTENANCE);
        // Check if the status was changed successfully.
        Assertions.assertEquals(VehicleStatus.UNDER_MAINTENANCE, vehicle.getStatus());
    }

    @Test
    public void testChangeStatusFromUnderMaintenanceToAvailable() {
        Vehicle vehicle = new Vehicle("Honda", LocalDate.now().getYear(), "1.5", "City");
        vehicle.changeStatus(VehicleStatus.UNDER_MAINTENANCE);
        vehicle.changeStatus(VehicleStatus.AVAILABLE);
        // Assert: Check the result.
        Assertions.assertEquals(VehicleStatus.AVAILABLE, vehicle.getStatus());
    }

    @Test
    public void testChangeStatusFromAvailableToAvailableThrowsException() {
        Vehicle vehicle = new Vehicle("Honda", LocalDate.now().getYear(), "1.5", "City");
        Assertions.assertThrows(BusinessRuleException.class, () -> vehicle.changeStatus(VehicleStatus.AVAILABLE));
    }

    @Test
    public void testChangeStatusFromUnderMaintenanceToUnderMaintenanceThrowsException() {
        Vehicle vehicle = new Vehicle("Honda", LocalDate.now().getYear(), "1.5", "City");
        vehicle.changeStatus(VehicleStatus.UNDER_MAINTENANCE);
        Assertions.assertThrows(BusinessRuleException.class, () -> vehicle.changeStatus(VehicleStatus.UNDER_MAINTENANCE));
    }

    @Test
    public void testUpdateAttributesPartially() {
        // Arrange
        Vehicle vehicle = new Vehicle("Volkswagen", 2023, "2.0", "Gol");
        VehicleUpdateDTO dto = new VehicleUpdateDTO("Ford", null, null, null);
        // Act
        vehicle.updateAttributes(dto);
        // Assert
        Assertions.assertEquals("Ford", vehicle.getBrand()); // Atributo atualizado
        Assertions.assertEquals(2023, vehicle.getFabricationYear()); // Atributo não alterado
        Assertions.assertEquals("2.0", vehicle.getEngine()); // Atributo não alterado
        Assertions.assertEquals("Gol", vehicle.getModel()); // Atributo não alterado
    }

}
