package com.transports.spring.comparable;

public interface IPassengerDtoGetAllPassengersComparable {
    Integer getId();
    String getName();
    String getSurname();
    int getOccupiedSeats();
    boolean isActive();

    boolean isShared();
}
