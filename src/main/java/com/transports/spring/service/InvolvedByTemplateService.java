package com.transports.spring.service;

import com.transports.spring.dto.DtoDriverList;
import com.transports.spring.dto.DtoPassengerList;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.repository.IInvolvedByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class InvolvedByTemplateService {

    private final IInvolvedByTemplateRepository involvedByTemplateRepository;

    public InvolvedByTemplateService(final IInvolvedByTemplateRepository involvedByTemplateRepository) {
        this.involvedByTemplateRepository = involvedByTemplateRepository;
    }

    public List<Passenger> getAllPassengersFromTemplate(final int templateId) {
        return this.involvedByTemplateRepository.getAllPassengersFromTemplate(templateId);
    }

    public DtoPassengerList getAllPassengersFromTemplateForTemplateView(final int templateId) {
        final List<Passenger> passengerList = this.involvedByTemplateRepository.getAllPassengersFromTemplate(templateId);
        int occupiedSeats = 0;
        for (final Passenger passenger : passengerList) {
            occupiedSeats += passenger.getOccupiedSeats();
        }

        return new DtoPassengerList(passengerList, occupiedSeats);
    }

    public List<Driver> getAllDriversFromTemplate(int templateId) {
        return this.involvedByTemplateRepository.getAllDriversFromTemplate(templateId);
    }

    public DtoDriverList getAllDriversFromTemplateForTemplateView(final int templateId) {
        final List<Driver> driverList = this.involvedByTemplateRepository.getAllDriversFromTemplate(templateId);
        int occupiedSeats = 0;
        for (final Driver driver: driverList) {
            occupiedSeats += driver.getAvailableSeats();
        }

        return new DtoDriverList(driverList, occupiedSeats);
    }

    public Passenger getPassengerByIdAndTemplate(final int passengerId, final int templateId) throws InvolvedDoesNotExistException {
        final Passenger passenger = this.involvedByTemplateRepository.getPassengerByIdAndTemplate(passengerId, templateId);
        if (passenger == null || passenger.getId() == null) {
            throw new InvolvedDoesNotExistException();
        }
        return passenger;
    }

    public Driver getDriverByIdAndTemplate(final int driverId, final int templateId) throws InvolvedDoesNotExistException {
        final Driver driver = this.involvedByTemplateRepository.getDriverByIdAndTemplate(driverId, templateId);
        if (driver == null || driver.getId() == null) {
            throw new InvolvedDoesNotExistException();
        }
        return driver;
    }
}
