package tech.clavem303.repositories;


import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import tech.clavem303.entities.Vehicle;

@ApplicationScoped
public class VehicleRepository implements PanacheRepository<Vehicle> {

}
