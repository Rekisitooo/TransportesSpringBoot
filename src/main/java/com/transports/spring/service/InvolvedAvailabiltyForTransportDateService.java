package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.model.*;
import com.transports.spring.repository.IInvolvedAvailabiltyForTransportDateRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvolvedAvailabiltyForTransportDateService {

    private final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository;
    private final TransportDateByTemplateService transportDateByTemplateService;
    private final InvolvedByTemplateService involvedByTemplateService;

    public InvolvedAvailabiltyForTransportDateService(final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository, TransportDateByTemplateService transportDateByTemplateService, InvolvedByTemplateService involvedByTemplateService) {
        this.involvedAvailabiltyForTransportDateRepository = involvedAvailabiltyForTransportDateRepository;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    /**
     * @param templateId
     * @return Map<DateId, List<Passenger (id, completeName)>>
     */
    public Map<Integer, List<Passenger>> findAllPassengersAssistanceDatesForTemplate(final int templateId) {
        final List<InvolvedAvailabiltyForTransportDate> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengersAssistanceDatesForTemplate(templateId);
        final Map<Integer, List<Passenger>> availablePassengersForDateMap = new HashMap<>();

        for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
            final int transportDateId = transportByTemplate.getTransportDateCode();
            final Passenger driver = new Passenger(transportByTemplate.getInvolvedCode(), transportByTemplate.getInvolvedCompleteName());
            List<Passenger> availablePassengersForTransportDate = availablePassengersForDateMap.get(transportDateId);
            if (availablePassengersForTransportDate == null) {
                availablePassengersForTransportDate = new ArrayList<>();
                availablePassengersForTransportDate.add(driver);
                availablePassengersForDateMap.put(transportDateId, availablePassengersForTransportDate);
            } else {
                availablePassengersForTransportDate.add(driver);
            }

        }

        return availablePassengersForDateMap;
    }

    /**
     * @param templateId
     * @return Map<PassengerId, List<LocalDate>>
     */
    public Map<Integer, List<DtoTemplateDay>> findAllPassengersAssistanceDates(final int templateId) {
        final Map<Integer, List<DtoTemplateDay>> allPassengersAssistanceDatesMap = new HashMap<>();

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            final List<InvolvedAvailabiltyForTransportDate> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengerAssistanceDatesForTemplate(templateId, passenger.getId());
            final List<DtoTemplateDay> passengersAssistanceDates = new ArrayList<>();

            for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
                final int transportDateId = transportByTemplate.getTransportDateCode();
                final TransportDateByTemplate taransportDate = this.transportDateByTemplateService.findById(transportDateId);
                final String transportDate = taransportDate.getTransportDate();
                final LocalDate transportLocalDate = LocalDate.parse(transportDate);
                final String eventName = taransportDate.getEventName();

                passengersAssistanceDates.add(new DtoTemplateDay(transportLocalDate, eventName));
            }

            allPassengersAssistanceDatesMap.put(passenger.getId(), passengersAssistanceDates);
        }

        return allPassengersAssistanceDatesMap;
    }

    /**
     * @param templateId
     * @return Map<DateId, Driver (id, completeName)>
     */
    public Map<Integer, List<Driver>> findAllDriversAvailableDatesForTemplate(final int templateId) {
        final List<InvolvedAvailabiltyForTransportDate> availableDriversForDate = this.involvedAvailabiltyForTransportDateRepository.findAllDriversAvailableDatesForTemplate(templateId);
        final Map<Integer, List<Driver>> availableDriversForDateMap = new HashMap<>();

        for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availableDriversForDate) {
            final int transportDateId = transportByTemplate.getTransportDateCode();
            final Driver driver = new Driver(transportByTemplate.getInvolvedCode(), transportByTemplate.getInvolvedCompleteName());
            List<Driver> availableDriversForTransportDate = availableDriversForDateMap.get(transportDateId);

            if (availableDriversForTransportDate == null) {
                availableDriversForTransportDate = new ArrayList<>();
                availableDriversForTransportDate.add(driver);
                availableDriversForDateMap.put(transportDateId, availableDriversForTransportDate);
            } else {
                availableDriversForTransportDate.add(driver);
            }

        }

        return availableDriversForDateMap;
    }

    public void addInvolvedAvailabilityForDate(final DtoAddNewDateForm body, final int newTransportDateId, final int templateId) throws InvolvedDoesNotExistException {
        final List<String> addDateCardDriverAvailabilityCheck = body.getAddDateCardDriverAvailabilityCheck();
        for (final String driverIdString : addDateCardDriverAvailabilityCheck) {
            final int driverId = Integer.parseInt(driverIdString);
            this.involvedByTemplateService.getDriverByIdAndTemplate(driverId, templateId);

            final InvolvedAvailabiltyForTransportDate entity = new InvolvedAvailabiltyForTransportDate(driverId, newTransportDateId);
            this.involvedAvailabiltyForTransportDateRepository.save(entity);
        }

        final List<String> addDateCardPassegerAvailabilityCheck = body.getAddDateCardPassegerAvailabilityCheck();
        for (final String passengerIdString : addDateCardPassegerAvailabilityCheck) {
            final int passengerId = Integer.parseInt(passengerIdString);
            this.involvedByTemplateService.getPassengerByIdAndTemplate(passengerId, templateId);

            final InvolvedAvailabiltyForTransportDate entity = new InvolvedAvailabiltyForTransportDate(passengerId, newTransportDateId);
            this.involvedAvailabiltyForTransportDateRepository.save(entity);
        }
    }
}
