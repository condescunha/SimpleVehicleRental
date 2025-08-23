package tech.clavem303.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.clavem303.entities.Booking;

import java.util.List;

@ApplicationScoped
public class BookingRepository implements PanacheRepository<Booking> {
    public List<Booking> findByCustomerId(String customerId) {
        return find("customerId", customerId).list();
    }
}
