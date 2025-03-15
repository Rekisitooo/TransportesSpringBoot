package com.transports.spring.service.passenger;

import com.transports.spring.dto.DtoFormGetAllPassengers;
import com.transports.spring.dto.DtoGetAllPassengers;
import com.transports.spring.exception.PassengerDoesNotBelongToUserException;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.AppUser;
import com.transports.spring.model.Passenger;
import com.transports.spring.repository.IPassengerRepository;
import com.transports.spring.service.AppUserService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class UpdatePassenger {

    private final IPassengerRepository passengerRepository;
    private final AppUserService appUserService;

    public UpdatePassenger(IPassengerRepository passengerRepository, AppUserService appUserService) {
        this.passengerRepository = passengerRepository;
        this.appUserService = appUserService;
    }

    public void updatePassenger(final DtoFormGetAllPassengers passengersCRUDform) throws TransportsException {

        for (final DtoGetAllPassengers passenger : passengersCRUDform.getPassengersList()) {
            final Optional<Passenger> passengerToUpdate = this.retrievePassenger(passenger);
            final Passenger foundPassenger = (Passenger) passengerToUpdate.get();
            if (!foundPassenger.isEqual(passenger)) {
                //check the weeklyTransportDaysValues
            }

            final int userId = 1;
            final Boolean isPassengerShared = passenger.getIsShared();
            Integer userCodeGroup;
            if (isUserAuthorizedToSharePassenger(isPassengerShared)) { // user cannot change the value as it is not the owner, so the value is null
                this.passengerIsUsers(passenger, userId);
                userCodeGroup = null;
                if (Boolean.TRUE.equals(isPassengerShared)) {
                    final Optional<AppUser> user = this.appUserService.findUserById(userId);
                    userCodeGroup = user.get().getGroupCode();
                }
            } else {
                userCodeGroup = foundPassenger.getUserCodeGroup();
            }
            this.passengerRepository.updateUser(passenger.getName(), passenger.getSurname(), passenger.isActive(), passenger.getOccupiedSeats(), userCodeGroup, userId);

            //update weekly transport days availability
        }
    }

    private Optional<Passenger> retrievePassenger(DtoGetAllPassengers passenger) throws InvolvedDoesNotExistException {
        final Optional<Passenger> passengerToUpdate = this.passengerRepository.findById(passenger.getId());
        if (passengerToUpdate.isEmpty()) {
            throw new InvolvedDoesNotExistException();
        }
        return passengerToUpdate;
    }

    private static boolean isUserAuthorizedToSharePassenger(final Boolean isPassengerShared) {
        return isPassengerShared != null;
    }

    private void passengerIsUsers(final DtoGetAllPassengers passenger, final int userId) throws PassengerDoesNotBelongToUserException {
        final Optional<Passenger> pass = this.passengerRepository.findById(passenger.getId());
        if (pass.isEmpty() || pass.get().getUserCode() != userId) {
            throw new PassengerDoesNotBelongToUserException();
        }
    }

}
