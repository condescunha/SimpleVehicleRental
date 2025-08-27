package tech.clavem303.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

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
    private OffsetDateTime activedAt;
    private OffsetDateTime finishedAt;
    private OffsetDateTime canceledAt;
    private String carTitle;

    public Booking(Long vehicleId, String customerId, LocalDate startDate, LocalDate endDate, String carTitle) {
        this.vehicleId = vehicleId;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.carTitle = carTitle;
    }

    public void activate() {
        if (this.status != BookingStatus.CREATED)
            throw new IllegalStateException("Activation only with CREATED status.");
        this.status = BookingStatus.ACTIVE;
        this.activedAt = OffsetDateTime.now();
    }

    public void cancel() {
        if (this.status != BookingStatus.CREATED && this.status != BookingStatus.ACTIVE)
            throw new IllegalStateException("Cancel only with CREATED or ACTIVE status.");
        this.status = BookingStatus.CANCELED;
        this.canceledAt = OffsetDateTime.now();
    }

    public void finish() {
        if (this.status != BookingStatus.ACTIVE)
            throw new IllegalStateException("Finish only with ACTIVE status.");
        this.status = BookingStatus.FINISHED;
        this.finishedAt = OffsetDateTime.now();
    }

}
