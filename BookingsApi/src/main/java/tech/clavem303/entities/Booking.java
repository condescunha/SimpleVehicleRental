package tech.clavem303.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long vehicleId;
    private String customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    @Enumerated(EnumType.STRING)
    private BookingStatus status = BookingStatus.CREATED;

    public Booking(Long vehicleId, String customerId, LocalDate startDate, LocalDate endDate) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void cancel() {
        if (this.status != BookingStatus.CREATED) {
            throw new IllegalStateException("Cancel only with CREATED status.");
        }
        this.status = BookingStatus.CANCELED;
    }

    public void finish() {
        if (this.status != BookingStatus.CREATED) {
            throw new IllegalStateException("Finish only with CREATED status.");
        }
        this.status = BookingStatus.FINISHED;
    }
}
