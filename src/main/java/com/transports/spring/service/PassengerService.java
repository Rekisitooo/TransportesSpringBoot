package com.transports.spring.service;

import com.transports.spring.dto.DtoFormGetAllPassengers;
import com.transports.spring.model.AbstractInvolved;
import com.transports.spring.model.Passenger;
import com.transports.spring.repository.PassengerProcedureRepository;
import com.transports.spring.dto.DtoGetAllPassengers;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.repository.IPassengerRepository;
import com.transports.spring.service.passenger.UpdatePassenger;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerService {

    private final IPassengerRepository passengerRepository;
    private final PassengerProcedureRepository procedureRepository;
    private final UpdatePassenger updatePassenger;

    public PassengerService(UpdatePassenger updatePassenger, IPassengerRepository passengerRepository, PassengerProcedureRepository procedureRepository) {
        this.passengerRepository = passengerRepository;
        this.procedureRepository = procedureRepository;
        this.updatePassenger = updatePassenger;
    }

    @Transactional
    public void updatePassenger(final DtoFormGetAllPassengers passengersCRUDform) throws TransportsException {
        this.updatePassenger.updatePassenger(passengersCRUDform);
    }

    public List<DtoGetAllPassengers> getAllPassengers(final int userId, final Integer groupId, final Pageable pageable) throws SQLException {
        return this.procedureRepository.getAllPassengers(userId, groupId, pageable);
    }

    public List<Passenger> getAll() {
        return this.passengerRepository.findAll();
    }

    public Passenger findById(final int passengerId){
        final Optional<Passenger> passengerOpt = this.passengerRepository.findById(passengerId);
        Passenger passenger = new Passenger();
        if (passengerOpt.isPresent()) {
            passenger = passengerOpt.get();
        }
        return passenger;
    }
}
