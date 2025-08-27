package tech.clavem303.resources;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.clavem303.DTOs.VehicleCreateDTO;
import tech.clavem303.DTOs.VehicleResponseDTO;
import tech.clavem303.DTOs.VehicleStatusUpdateDTO;
import tech.clavem303.DTOs.VehicleUpdateDTO;
import tech.clavem303.services.VehicleService;

import java.util.List;

@Path("/vehicles")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
public class VehicleResource {

    @Inject
    VehicleService vehicleService;

    @POST
    public Response create(@Valid VehicleCreateDTO dto) {
        VehicleResponseDTO createdVehicle = vehicleService.createVehicle(dto);
        return Response.status(Response.Status.CREATED).entity(createdVehicle).build();
    }

    @GET
    public List<VehicleResponseDTO> getAllVehicles() {
        return vehicleService.findAllVehicles();
    }

    @GET
    @Path("/{id}")
    public VehicleResponseDTO getById(@PathParam("id") Long id) {
        return vehicleService.findVehicleById(id);
    }

    @PATCH
    @Path("/{id}")
    public VehicleResponseDTO update(@PathParam("id") Long id, VehicleUpdateDTO dto) {
        return vehicleService.updateVehicle(id, dto);
    }

    @PUT
    @Path("/{id}/status")
    public VehicleResponseDTO updateStatus(@PathParam("id") Long id, @Valid VehicleStatusUpdateDTO dto) {
        return vehicleService.updateStatus(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        vehicleService.deleteVehicleById(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
