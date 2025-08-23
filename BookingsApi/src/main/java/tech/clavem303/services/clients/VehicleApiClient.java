package tech.clavem303.services.clients;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.annotation.RegisterClientHeaders;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import tech.clavem303.DTOs.clients.VehicleResponseDTO;

import java.util.Optional;

@Path("/vehicles")
@RegisterRestClient(configKey = "VehicleApi")
public interface VehicleApiClient {

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    Optional<VehicleResponseDTO> getVehicle(@PathParam("id") Long id);
}
