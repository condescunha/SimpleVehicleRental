package tech.clavem303.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tech.clavem303.DTOs.VehicleUpdateDTO;
import tech.clavem303.exceptions.BusinessRuleException;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String brand;
    private Integer fabricationYear;
    private String engine;
    private String model;
    @Enumerated(EnumType.STRING)
    private VehicleStatus status = VehicleStatus.AVAILABLE;

    public Vehicle(String brand, Integer year, String engine, String model) {
        this.brand = brand;
        this.fabricationYear = validateFabricationYear(year);
        this.engine = engine;
        this.model = model;
    }

    public void changeStatus(VehicleStatus newStatus) {
        if (newStatus == VehicleStatus.AVAILABLE && this.status != VehicleStatus.UNDER_MAINTENANCE) {
            throw new BusinessRuleException("From 'AVAILABLE' to 'UNDER_MAINTENANCE' only.");
        }

        if (newStatus == VehicleStatus.UNDER_MAINTENANCE && this.status != VehicleStatus.AVAILABLE) {
            throw new BusinessRuleException("From 'UNDER_MAINTENANCE' to 'AVAILABLE' only.");
        }
        this.status = newStatus;
    }

    public void updateAttributes(VehicleUpdateDTO dto) {
        if (dto.brand() != null) {
            this.brand = dto.brand();
        }
        if (dto.fabricationYear() != null) {
            this.fabricationYear = validateFabricationYear(dto.fabricationYear());
        }
        if (dto.engine() != null) {
            this.engine = dto.engine();
        }

        if (dto.model() != null) {
            this.model = dto.model();
        }
    }

    public String getCarTitle() {
        return brand + " " + model + " " + engine;
    }

    private Integer validateFabricationYear(Integer fabricationYear) {
        if (fabricationYear > LocalDate.now().getYear()) throw new IllegalArgumentException("Invalid year (future).");
        if (fabricationYear < LocalDate.now().minusYears(3).getYear()) throw new IllegalArgumentException("Invalid year (max three-year-old).");
        return fabricationYear;
    }

}
