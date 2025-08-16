package tech.clavem303.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import tech.clavem303.DTOs.VehicleCreateDTO;
import tech.clavem303.DTOs.VehicleResponseDTO;
import tech.clavem303.DTOs.VehicleStatusUpdateDTO;
import tech.clavem303.DTOs.VehicleUpdateDTO;
import tech.clavem303.entities.Vehicle;
import tech.clavem303.exceptions.ResourceNotFoundException;
import tech.clavem303.repositories.VehicleRepository;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class VehicleService {

    @Inject
    VehicleRepository vehicleRepository;

    @Transactional
    public VehicleResponseDTO createVehicle(VehicleCreateDTO dto) {
        Vehicle vehicle = new Vehicle(dto.brand(), dto.fabricationYear(), dto.engine(), dto.model());
        vehicleRepository.persist(vehicle);
        return toResponseDTO(vehicle);
    }

    public List<VehicleResponseDTO> findAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.listAll();
        return toResponseDTOs(vehicles);
    }

    public VehicleResponseDTO findVehicleById(Long id) {
        Vehicle vehicle = findVehicleOrThrow(id);
        return toResponseDTO(vehicle);
    }

    @Transactional
    public VehicleResponseDTO updateVehicle(Long id, VehicleUpdateDTO dto) {
        Vehicle vehicle = findVehicleOrThrow(id);
        vehicle.updateAttributes(dto);
        return toResponseDTO(vehicle);
    }

    @Transactional
    public VehicleResponseDTO updateStatus(Long id, VehicleStatusUpdateDTO dto) {
        Vehicle vehicle = findVehicleOrThrow(id);
        vehicle.changeStatus(dto.status());
        return toResponseDTO(vehicle);
    }

    @Transactional
    public void deleteVehicleById(Long id) {
        boolean deleted = vehicleRepository.deleteById(id);
        if (!deleted) {
            throw new ResourceNotFoundException("Vehicle with ID " + id + "  not found.");
        }
    }

    private Vehicle findVehicleOrThrow(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id);
        if (vehicle == null) {
            throw new ResourceNotFoundException("Vehicle with ID " + id + " not found.");
        }
        return vehicle;
    }

    private List<VehicleResponseDTO> toResponseDTOs(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    private VehicleResponseDTO toResponseDTO(Vehicle vehicle) {
        return new VehicleResponseDTO(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getFabricationYear(),
                vehicle.getEngine(),
                vehicle.getModel(),
                vehicle.getStatus(),
                vehicle.getCarTitle());
    }
}
