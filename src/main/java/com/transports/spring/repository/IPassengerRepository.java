package com.transports.spring.repository;

import com.transports.spring.model.AbstractInvolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IPassengerRepository extends JpaRepository<AbstractInvolved, Integer> {

    @Modifying
    @Query("UPDATE Passenger i SET i.name = :name, i.surname = :surname, i.isActive = :isActive, i.occupiedSeats = :occupiedSeats, i.userCodeGroup = :userGroupCode WHERE i.userCode = :ownerUserCode")
    void updateUser(@Param("name") final String name, @Param("surname") final String surname, @Param("isActive") final Boolean isActive, @Param("occupiedSeats") final int occupiedSeats, @Param("userGroupCode") final Integer userGroupCode, @Param("ownerUserCode") final int ownerUserCode);

    @Modifying
    @Query("UPDATE Passenger i SET i.name = :name, i.surname = :surname, i.isActive = :isActive, i.occupiedSeats = :occupiedSeats WHERE i.userCode = :ownerUserCode")
    void updateUserWithoutShared(@Param("name") final String name, @Param("surname") final String surname, @Param("isActive") final Boolean isActive, @Param("occupiedSeats") final int occupiedSeats, @Param("ownerUserCode") final int ownerUserCode);

}