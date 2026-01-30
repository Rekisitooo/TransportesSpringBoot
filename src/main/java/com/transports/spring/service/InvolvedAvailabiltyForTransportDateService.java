package com.transports.spring.service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.transports.spring.dto.DtoInvolvedAvailabiltyForTransportDate;
import com.transports.spring.dto.requestbody.DtoUpdateNeedForTransport;
import com.transports.spring.model.Driver;
import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.repository.IInvolvedAvailabiltyForTransportDateRepository;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;

@Service
public class InvolvedAvailabiltyForTransportDateService {

    private final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportService transportService;

    public InvolvedAvailabiltyForTransportDateService(
            final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository,
            InvolvedByTemplateService involvedByTemplateService, TransportService transportService) {
        this.involvedAvailabiltyForTransportDateRepository = involvedAvailabiltyForTransportDateRepository;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
    }

    /**
     * @param templateId
     * @return Map<DateId, List<Passenger (id, completeName)>>
     */
    public Map<Integer, List<Passenger>> findAllPassengersAssistanceDatesForTemplate(final int templateId) {
        final List<DtoInvolvedAvailabiltyForTransportDate> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository
                .findAllPassengersAssistanceDatesForTemplate(templateId);
        final Map<Integer, List<Passenger>> availablePassengersForDateMap = new HashMap<>();

        for (final DtoInvolvedAvailabiltyForTransportDate transportByTemplate : availablePassengersForDate) {
            final InvolvedAvailabiltyForTransportDate involvedAvailabiltyForTransportDate = transportByTemplate.getInvolvedAvailabiltyForTransportDate();
            final int transportDateId = involvedAvailabiltyForTransportDate.getTransportDateCode();
            final Passenger driver = new Passenger(involvedAvailabiltyForTransportDate.getInvolvedCode(),transportByTemplate.getInvolvedCompleteName());
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
     * @return Map<PassengerId, Map<DateId, VoCompleteInvolvedAvailability>>
     */
    public Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> findAllPassengersAssistanceDatesByTemplate(final int templateId) {
        final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> allPassengersAssistanceDatesMap = new HashMap<>();

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);

        // for each passenger
        for (final Passenger passenger : passengerList) {

            final Map<Integer, VoCompleteInvolvedAvailability> passengersAssistanceDates = new HashMap<>();
            final List<VoCompleteInvolvedAvailability> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengerAssistanceDatesForTemplate(templateId, passenger.getId());

            // add all the dates
            for (final VoCompleteInvolvedAvailability voCompleteInvolvedAvailability : availablePassengersForDate) {
                final int transportDateId = voCompleteInvolvedAvailability.getTransportDateByTemplate().getId();
                passengersAssistanceDates.put(transportDateId, voCompleteInvolvedAvailability);
            }

            allPassengersAssistanceDatesMap.put(passenger.getId(), passengersAssistanceDates);
        }

        return allPassengersAssistanceDatesMap;
    }

    /**
     * @param templateId
     * @return Map<PassengerId, Map<LocalDate, DtoTemplateDay>>
     */
    public Map<Integer, Map<LocalDate, VoCompleteInvolvedAvailability>> findAllPassengersAssistanceDates(final int templateId) {
        final Map<Integer, Map<LocalDate, VoCompleteInvolvedAvailability>> allPassengersAssistanceDatesMap = new HashMap<>();

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            final List<VoCompleteInvolvedAvailability> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengerAssistanceDatesForTemplate(templateId, passenger.getId());
            final Map<LocalDate, VoCompleteInvolvedAvailability> passengersAssistanceDates = new HashMap<>();

            for (final VoCompleteInvolvedAvailability voCompleteInvolvedAvailability : availablePassengersForDate) {
                final Date transportDateObj = voCompleteInvolvedAvailability.getTransportDateByTemplate().getTransportDate();
                final LocalDate transportLocalDate = transportDateObj.toLocalDate();

                passengersAssistanceDates.put(transportLocalDate, voCompleteInvolvedAvailability);
            }

            allPassengersAssistanceDatesMap.put(passenger.getId(), passengersAssistanceDates);
        }

        return allPassengersAssistanceDatesMap;
    }

    /**
     * @param templateId
     * @return Map<DriverId, Map<DateId, DtoTemplateDay>>
     */
    public Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> findAllDriversAssistanceDates(final int templateId) {
        final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> allDriversAssistanceDatesMap = new HashMap<>();

        final List<Driver> driverList = this.involvedByTemplateService.getAllDriversFromTemplate(templateId);
        for (final Driver driver : driverList) {
            final int id = driver.getId();
            final List<VoCompleteInvolvedAvailability> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllDriversAssistanceDatesForTemplate(templateId, id);

            final Map<Integer, VoCompleteInvolvedAvailability> driversAssistanceDates = new HashMap<>();
            for (final VoCompleteInvolvedAvailability voCompleteInvolvedAvailability : availablePassengersForDate) {
                final int transportDateId = voCompleteInvolvedAvailability.getTransportDateByTemplate().getId();

                driversAssistanceDates.put(transportDateId, voCompleteInvolvedAvailability);
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
     * 
     * @param involvedId passenger id to delete in db
     * @param dateId     date id to delete in db
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
