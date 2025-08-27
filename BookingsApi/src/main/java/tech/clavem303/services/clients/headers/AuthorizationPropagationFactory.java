package tech.clavem303.services.clients.headers;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;

@RequestScoped
public class AuthorizationPropagationFactory implements ClientHeadersFactory {

    @Inject
    JsonWebToken accessToken;

    @Override
    public MultivaluedMap<String, String> update(MultivaluedMap<String, String> incomingHeaders, MultivaluedMap<String, String> clientHeaders) {
        MultivaluedMap<String, String> newHeaders = new MultivaluedHashMap<>();
        String token = accessToken.getRawToken();

        if (!(token != null && !token.isEmpty()))
            throw new IllegalStateException("Missing authorization token for API call.");

        newHeaders.add("Authorization", "Bearer " + token);
        return newHeaders;
    }
}
