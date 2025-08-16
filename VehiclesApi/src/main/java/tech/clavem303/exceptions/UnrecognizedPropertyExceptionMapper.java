package tech.clavem303.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnrecognizedPropertyExceptionMapper implements ExceptionMapper<UnrecognizedPropertyException> {

    @Override
    public Response toResponse(UnrecognizedPropertyException exception) {
        String message = String.format("Unknown field in request: '%s'.", exception.getPropertyName());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(message)
                .build();
    }
}
