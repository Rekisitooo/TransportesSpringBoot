package com.transports.spring.model;

import com.transports.spring.comparable.IPassengerDtoGetAllPassengersComparable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.antlr.v4.runtime.misc.NotNull;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public final class Passenger extends AbstractInvolved implements IPassengerDtoGetAllPassengersComparable {

    @Column(name = "NUMERO_PLAZAS")
    private int occupiedSeats;

    public boolean isEqual(final IPassengerDtoGetAllPassengersComparable entity) {
        return this.getName().equalsIgnoreCase(entity.getName()) &&
                this.getSurname().equalsIgnoreCase(entity.getSurname()) &&
                this.getOccupiedSeats() == entity.getOccupiedSeats() &&
                this.isActive() == entity.isActive() &&
                this.isShared() == entity.isShared();
    }

    @Override
    public boolean isShared() {
        return this.getUserCodeGroup() != null;
    }
}
