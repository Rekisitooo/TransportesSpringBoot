package com.transports.spring.model;

import com.transports.spring.comparable.IPassengerDtoGetAllPassengersComparable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public final class Passenger extends AbstractInvolved implements IPassengerDtoGetAllPassengersComparable {

    @Column(name = "NUMERO_PLAZAS")
    private String occupiedSeats;

    public Passenger(int id, String name, String surname, boolean isActive, int roleCode, int userCode, Integer userCodeGroup, String occupiedSeats) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isActive = isActive;
        this.roleCode = roleCode;
        this.userCode = userCode;
        this.userCodeGroup = userCodeGroup;
        this.occupiedSeats = occupiedSeats;
    }

    public Passenger(int id, String completeName){
        super(id, completeName);
    }

    public boolean isEqual(final IPassengerDtoGetAllPassengersComparable entity) {
        return this.getName().equalsIgnoreCase(entity.getName()) &&
                this.getSurname().equalsIgnoreCase(entity.getSurname()) &&
                this.getOccupiedSeats() == entity.getOccupiedSeats() &&
                this.isActive() == entity.isActive() &&
                this.isShared() == entity.isShared();
    }

    @Override
    public int getOccupiedSeats() {
        return Integer.parseInt(this.occupiedSeats);
    }

    @Override
    public boolean isShared() {
        return this.getUserCodeGroup() != null;
    }
}
