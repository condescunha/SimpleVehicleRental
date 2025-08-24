package tech.clavem303.entities;

public enum BookingStatus {

    /** The booking has been created but not yet confirmed. */
    CREATED,

    /** The vehicle has been picked up by the user. */
    ACTIVE,

    /** The booking has been picked up and returned by the user. */
    FINISHED,

    /** The booking was canceled by the user or by the system. */
    CANCELED

}
