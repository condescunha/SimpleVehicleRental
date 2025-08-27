package tech.clavem303.services;

import io.quarkus.cache.CacheInvalidateAll;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class VehicleCacheInvalidatorService {

    @Incoming("vehicle-updates")
    @CacheInvalidateAll(cacheName = "vehicles")
    public void invalidateCacheOnVehicleUpdate(String message) {
        System.out.println("Vehicle change event received: " + message);
        System.out.println("Invalidating vehicle cache.");
    }
}
