package tech.clavem303.resources;

import io.quarkus.security.identity.SecurityIdentity;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tech.clavem303.DTOs.BookingCreateDTO;
import tech.clavem303.DTOs.BookingUpdateStatusDTO;
import tech.clavem303.services.BookingService;

@Path("/bookings")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingResource {

    @Inject
    BookingService bookingService;

    @Inject
    SecurityIdentity securityIdentity;

    @POST
    @RolesAllowed({"user", "admin"})
    public Response createBooking(@Valid BookingCreateDTO dto) {
        return Response.status(Response.Status.CREATED)
                .entity(bookingService.createBooking(dto, getCostumerId()))
                .build();
    }

    @GET
    @RolesAllowed({"user", "admin"})
    public Response getAllBookings() {
        if(!securityIdentity.hasRole("admin"))
            return Response.ok(bookingService.findBookingsByUser(getCostumerId())).build();

        return Response.ok(bookingService.findAllBookings()).build();
    }

    @GET
    @Path("/{id}")
    @RolesAllowed({"admin", "user"})
    public Response getBookingById(@PathParam("id") Long id) {
        return Response.ok(bookingService.findBookingById(id, getCostumerId())).build();
    }

    @PATCH
    @Path("/{id}/status")
    @RolesAllowed("admin")
    public Response updateStatus(@PathParam("id") Long id, BookingUpdateStatusDTO dto) {
        return Response.ok(bookingService.updateStatus(id, dto)).build();
    }

    private String getCostumerId(){
        return securityIdentity.getPrincipal().getName();
    }

}
