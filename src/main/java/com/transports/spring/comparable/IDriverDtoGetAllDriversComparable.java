package com.transports.spring.comparable;

public interface IDriverDtoGetAllDriversComparable {
    Integer getId();
    String getName();
    String getSurname();
    int getAvailableSeats();
    boolean isActive();
    boolean isShared();
}
