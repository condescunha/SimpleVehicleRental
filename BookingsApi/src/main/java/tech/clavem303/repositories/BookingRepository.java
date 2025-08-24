package tech.clavem303.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.clavem303.entities.Booking;
import tech.clavem303.entities.BookingStatus;

import java.util.List;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {
    public List<Booking> findByCustomerId(String customerId) {
        return find("customerId", customerId).list();
    }

    public boolean isVehicleBooked(Long vehicleId) {
        long count = find("vehicleId = ?1 and (status = ?2 or status = ?3)",
                vehicleId, BookingStatus.CREATED, BookingStatus.ACTIVE).count();
        return count > 0;
    }
}
