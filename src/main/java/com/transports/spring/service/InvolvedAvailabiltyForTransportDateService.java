package com.transports.spring.service;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoInvolvedAvailabiltyForTransportDate;
import com.transports.spring.dto.DtoTemplateDay;
import com.transports.spring.dto.requestbody.DtoUpdateNeedForTransport;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.model.*;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.repository.IInvolvedAvailabiltyForTransportDateRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
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
    private final TransportService transportService;

    public InvolvedAvailabiltyForTransportDateService(final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository, TransportDateByTemplateService transportDateByTemplateService, InvolvedByTemplateService involvedByTemplateService, TransportService transportService) {
        this.involvedAvailabiltyForTransportDateRepository = involvedAvailabiltyForTransportDateRepository;
        this.transportDateByTemplateService = transportDateByTemplateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
    }

    /**
     * @param templateId
     * @return Map<DateId, List<Passenger (id, completeName)>>
     */
    public Map<Integer, List<Passenger>> findAllPassengersAssistanceDatesForTemplate(final int templateId) {
        final List<DtoInvolvedAvailabiltyForTransportDate> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengersAssistanceDatesForTemplate(templateId);
        final Map<Integer, List<Passenger>> availablePassengersForDateMap = new HashMap<>();

        for (final DtoInvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
            final InvolvedAvailabiltyForTransportDate involvedAvailabiltyForTransportDate = transportByTemplate.getInvolvedAvailabiltyForTransportDate();
            final int transportDateId = involvedAvailabiltyForTransportDate.getTransportDateCode();
            final Passenger driver = new Passenger(involvedAvailabiltyForTransportDate.getInvolvedCode(), transportByTemplate.getInvolvedCompleteName());
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
     * @return Map<PassengerId, Map<LocalDate, DtoTemplateDay>>
     */
    public Map<Integer, Map<LocalDate, DtoTemplateDay>> findAllPassengersAssistanceDates(final int templateId) {
        final Map<Integer, Map<LocalDate, DtoTemplateDay>> allPassengersAssistanceDatesMap = new HashMap<>();

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            final List<InvolvedAvailabiltyForTransportDate> availablePassengersForDate =
                    this.involvedAvailabiltyForTransportDateRepository.findAllPassengerAssistanceDatesForTemplate(templateId, passenger.getId());
            final Map<LocalDate, DtoTemplateDay> passengersAssistanceDates = new HashMap<>();

            for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
                final int transportDateId = transportByTemplate.getTransportDateCode();
                final TransportDateByTemplate transportDate = this.transportDateByTemplateService.findById(transportDateId);
                final Date transportDateObj = transportDate.getTransportDate();
                final LocalDate transportLocalDate = transportDateObj.toLocalDate();
                final String eventName = transportDate.getEventName();
                final int needsTransport = transportByTemplate.getNeedsTransport();

                passengersAssistanceDates.put(transportLocalDate, new DtoTemplateDay(transportLocalDate, eventName, needsTransport));
            }

            allPassengersAssistanceDatesMap.put(passenger.getId(), passengersAssistanceDates);
        }

        return allPassengersAssistanceDatesMap;
    }

    /**
     * @param templateId
     * @return Map<DriverId, Map<LocalDate, DtoTemplateDay>>
     */
    public Map<Integer, Map<LocalDate, DtoTemplateDay>> findAllDriversAssistanceDates(final int templateId) {
        final Map<Integer, Map<LocalDate, DtoTemplateDay>> allDriversAssistanceDatesMap = new HashMap<>();

        final List<Driver> driverList = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        for (final Driver driver : driverList) {
            final int id = driver.getId();
            final List<InvolvedAvailabiltyForTransportDate> availablePassengersForDate =
                    this.involvedAvailabiltyForTransportDateRepository.findAllDriversAssistanceDatesForTemplate(templateId, id);

            final Map<LocalDate, DtoTemplateDay> driversAssistanceDates = new HashMap<>();
            for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
                final int transportDateId = transportByTemplate.getTransportDateCode();
                final TransportDateByTemplate transportDate = this.transportDateByTemplateService.findById(transportDateId);
                final Date transportDateObj = transportDate.getTransportDate();
                final LocalDate transportLocalDate = transportDateObj.toLocalDate();
                final String eventName = transportDate.getEventName();
                final int needsTransport = transportByTemplate.getNeedsTransport();

                driversAssistanceDates.put(transportLocalDate, new DtoTemplateDay(transportLocalDate, eventName, needsTransport));
            }

            allDriversAssistanceDatesMap.put(id, driversAssistanceDates);
        }

        return allDriversAssistanceDatesMap;
    }

    /**
     * @param templateId
     * @return Map<DateId, Driver (id, completeName)>
     */
    public Map<Integer, List<Driver>> findAllDriversAvailableDatesForTemplate(final int templateId) {
        final List<DtoInvolvedAvailabiltyForTransportDate> availableDriversForDate = this.involvedAvailabiltyForTransportDateRepository.findAllDriversAvailableDatesForTemplate(templateId);
        final Map<Integer, List<Driver>> availableDriversForDateMap = new HashMap<>();

        for (final DtoInvolvedAvailabiltyForTransportDate transportByTemplate : availableDriversForDate) {
            final InvolvedAvailabiltyForTransportDate involvedAvailabiltyForTransportDate = transportByTemplate.getInvolvedAvailabiltyForTransportDate();
            final int transportDateId = involvedAvailabiltyForTransportDate.getTransportDateCode();
            final Driver driver = new Driver(involvedAvailabiltyForTransportDate.getInvolvedCode(), transportByTemplate.getInvolvedCompleteName());
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

        final List<String> addDateCardPassegerAvailabilityCheck = body.getAddDateCardPassengerAvailabilityCheck();
        for (final String passengerIdString : addDateCardPassegerAvailabilityCheck) {
            final int passengerId = Integer.parseInt(passengerIdString);
            this.involvedByTemplateService.getPassengerByIdAndTemplate(passengerId, templateId);

            final InvolvedAvailabiltyForTransportDate entity = new InvolvedAvailabiltyForTransportDate(passengerId, newTransportDateId);
            this.involvedAvailabiltyForTransportDateRepository.save(entity);
        }
    }

    @Transactional
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> updateInvolvedNeedForTransport(final DtoUpdateNeedForTransport body, final Integer passengerId) {
        final Integer transportDateId = body.getTransportDateId();
        final Integer needsTransport = body.getPassengerNeedsTransport();
        final Integer driverId = body.getDriverId();
        if (driverId != null) {
            final Transport transportByPassenger = this.transportService.findTransportByPassenger(transportDateId, passengerId);
            if (transportByPassenger != null) {
                this.transportService.deleteTransport(new TransportKey(passengerId, driverId, transportDateId));
            }
        }
        this.involvedAvailabiltyForTransportDateRepository.updateInvolvedNeedForTransport(needsTransport, passengerId, transportDateId);
        return ResponseEntity.ok().build();
    }

    /**
     * Transports associated are erased in a trigger
     * @param involvedId passenger id to delete in db
     * @param dateId date id to delete in db
     * @return ok response
     */
    @Transactional
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> deleteInvolvedAssistanceForDate(final Integer involvedId, final Integer dateId) {
        this.involvedAvailabiltyForTransportDateRepository.deleteInvolvedAssistanceForDate(involvedId, dateId);
        return ResponseEntity.ok().build();
    }

    @Transactional
    public ResponseEntity<InvolvedAvailabiltyForTransportDate> saveAvailability(final Integer involvedId, final Integer dateId) {
        this.involvedAvailabiltyForTransportDateRepository.save(new InvolvedAvailabiltyForTransportDate(involvedId, dateId));
        return ResponseEntity.ok().build();
    }
}
