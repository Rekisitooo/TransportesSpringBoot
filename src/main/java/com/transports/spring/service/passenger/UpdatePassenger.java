package com.transports.spring.service.passenger;

import com.transports.spring.dto.DtoFormGetAllPassengers;
import com.transports.spring.dto.DtoGetAllPassengers;
import com.transports.spring.exception.PassengerDoesNotBelongToUserException;
import com.transports.spring.exception.PassengerDoesNotExistException;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.AbstractInvolved;
import com.transports.spring.model.AppUser;
import com.transports.spring.model.Passenger;
import com.transports.spring.repository.IPassengerRepository;
import com.transports.spring.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class UpdatePassenger {

    @Autowired
    private IPassengerRepository passengerRepository;

    @Autowired
    private AppUserService appUserService;

    public void updatePassenger(final DtoFormGetAllPassengers passengersCRUDform) throws TransportsException {
        for (final DtoGetAllPassengers passenger : passengersCRUDform.getPassengersList()) {
            //check if passenger has changed
            final Optional<AbstractInvolved> passengerToUpdate = this.passengerRepository.findById(passenger.getId());
            if (passengerToUpdate.isEmpty()) {
                throw new PassengerDoesNotExistException();
            }

            final Passenger foundPassenger = (Passenger) passengerToUpdate.get();
            if (!foundPassenger.isEqual(passenger)) {
                //check the weeklyTransportDaysValues
            }

            final int userId = 1;
            final Boolean isPassengerShared = passenger.getIsShared();

            if (isUserAuthorizedToSharePassenger(isPassengerShared)) { // user cannot change the value as it is not the owner, so the value is null
                this.passengerIsUsers(passenger, userId);
                Integer userCodeGroup = null;
                if (Boolean.TRUE.equals(isPassengerShared)) {
                    final Optional<AppUser> user = this.appUserService.findUserById(userId);
                    userCodeGroup = user.get().getGroupCode();
                }
                this.passengerRepository.updateUser(passenger.getName(), passenger.getSurname(), passenger.isActive(), passenger.getOccupiedSeats(), userCodeGroup, userId);
            } else {
                this.passengerRepository.updateUserWithoutShared(passenger.getName(), passenger.getSurname(), passenger.isActive(), passenger.getOccupiedSeats(), userId);
            }

            //update weekly transport days availability
        }
    }

    private static boolean isUserAuthorizedToSharePassenger(final Boolean isPassengerShared) {
        return isPassengerShared != null;
    }

    private void passengerIsUsers(final DtoGetAllPassengers passenger, final int userId) throws PassengerDoesNotBelongToUserException {
        final Optional<AbstractInvolved> pass = this.passengerRepository.findById(passenger.getId());
        if (pass.isEmpty() || pass.get().getUserCode() != userId) {
            throw new PassengerDoesNotBelongToUserException();
        }
    }

}
