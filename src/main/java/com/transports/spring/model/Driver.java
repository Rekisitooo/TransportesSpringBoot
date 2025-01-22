package com.transports.spring.model;

import com.transports.spring.comparable.IDriverDtoGetAllDriversComparable;
import com.transports.spring.comparable.IPassengerDtoGetAllPassengersComparable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public final class Driver extends AbstractInvolved implements IDriverDtoGetAllDriversComparable {

    @Column(name = "NUMERO_PLAZAS")
    private int availableSeats;

    public Driver(int id, String name, String surname, boolean isActive, int roleCode, int userCode, Integer userCodeGroup, int availableSeats) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.isActive = isActive;
        this.roleCode = roleCode;
        this.userCode = userCode;
        this.userCodeGroup = userCodeGroup;
        this.availableSeats = availableSeats;
    }

    public Driver(int id, String name, String surname, int roleCode, int availableSeats) {
        super();
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.roleCode = roleCode;
        this.availableSeats = availableSeats;
    }

    public Driver(int id, String completeName){
        super(id, completeName);
    }

    public boolean isEqual(final IDriverDtoGetAllDriversComparable entity) {
        return this.getName().equalsIgnoreCase(entity.getName()) &&
                this.getSurname().equalsIgnoreCase(entity.getSurname()) &&
                this.getAvailableSeats() == entity.getAvailableSeats() &&
                this.isActive() == entity.isActive() &&
                this.isShared() == entity.isShared();
    }

    @Override
    public boolean isShared() {
        return this.getUserCodeGroup() != null;
    }
}
