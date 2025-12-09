package com.transports.spring.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoGetPassengersForDriverByDate;
import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.InvolvedTransportNotification;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.repository.ITransportRepository;
import com.transports.spring.vo.completemodel.VoCompleteTransport;

@Service
public class TransportService {

    private final ITransportRepository transportByTemplateRepository;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final InvolvedTransportNotificationService involvedTransportNotificationService;

    public TransportService(final ITransportRepository transportByTemplateRepository, InvolvedByTemplateService involvedByTemplateService, InvolvedTransportNotificationService involvedTransportNotificationService) {
        this.transportByTemplateRepository = transportByTemplateRepository;
        this.involvedByTemplateService = involvedByTemplateService;
        this.involvedTransportNotificationService = involvedTransportNotificationService;
    }

    /**
     * @param passengerList passengers available in the consulted template
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, VoCompleteTransport>>
     */
    public Map<Integer, Map<Integer, VoCompleteTransport>> findAllPassengerTransportsFromTemplate(final List<Passenger> passengerList, final int templateId) {
        final Map<Integer, Map<Integer, VoCompleteTransport>> passengerTransportsMap = new HashMap<>();
        for (final Passenger passenger : passengerList) {

            Map<Integer, VoCompleteTransport> transportsMap = new HashMap<>();
            final List<VoCompleteTransport> allPassengerTransportsFromTemplate = this.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);
            for (final VoCompleteTransport voCompleteTransport : allPassengerTransportsFromTemplate) {
                transportsMap.put(voCompleteTransport.getTransport().getTransportKey().getTransportDateId(), voCompleteTransport);
            }
            passengerTransportsMap.put(passenger.getId(), transportsMap);
        }

        return passengerTransportsMap;
    }

    /**
     * @param driverList drivers available in the consulted template
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<VoCompleteTransport>>>
     */
    public Map<Integer, Map<Integer, List<VoCompleteTransport>>> findAllDriverTransportsFromTemplate(final List<Driver> driverList, final int templateId) throws InvolvedDoesNotExistException {
        final Map<Integer, Map<Integer, List<VoCompleteTransport>>> driverTransportsMap = new HashMap<>();

        for (final Driver driver : driverList) {
            final List<VoCompleteTransport> allDriverTransportsFromTemplate = this.findAllDriverTransportsFromTemplate(driver.getId(), templateId);
            Map<Integer, List<VoCompleteTransport>> transportPassengersMap = new HashMap<>();
            driverTransportsMap.put(driver.getId(), transportPassengersMap);

            for (final VoCompleteTransport voCompleteTransport : allDriverTransportsFromTemplate) {
                final Integer transportDateId = voCompleteTransport.getTransport().getTransportKey().getTransportDateId();

                List<VoCompleteTransport> transportPassengerList = transportPassengersMap.get(transportDateId);
                if (transportPassengerList == null) {
                    transportPassengerList = new ArrayList<>(Arrays.asList(voCompleteTransport));
                    transportPassengersMap.put(transportDateId, transportPassengerList);
                } else {
                    transportPassengerList.add(voCompleteTransport);
                }
            }
        }

        return driverTransportsMap;
    }

    public List<VoCompleteTransport> findAllPassengerTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllPassengerTransportsFromTemplate(passengerId, templateId);
    }

    public List<VoCompleteTransport> findAllDriverTransportsFromTemplate(final int driverId, final int templateId) {
        return this.transportByTemplateRepository.findAllDriverTransportsFromTemplate(driverId, templateId);
    }

    public Transport findTransportByPassenger(final int transportDateId, final int passengerId) {
        return this.transportByTemplateRepository.findTransportByPassenger(transportDateId, passengerId);
    }

    public void updateDriverInTransport(final TransportKey transportKey) {
        //TODO Checks
        this.transportByTemplateRepository.updateDriverInTransport(
                transportKey.getTransportDateId(),
                transportKey.getDriverId(),
                transportKey.getPassengerId()
        );
    }

    public Transport createTransport(final TransportKey transportKey) {
        //TODO Checks
        return this.transportByTemplateRepository.save(new Transport(transportKey));
    }

    public void deleteTransport(final TransportKey transportKey) {
        //TODO Checks
        this.transportByTemplateRepository.delete(new Transport(transportKey));
    }

    public List<DtoDriverTransport> findDriverTransportsFromTemplate(final int driverId, final int templateId) {
        final List<DtoDriverTransport> dtoDriverTransportList = new ArrayList<>();
        java.sql.Date previousTransportDate = null;
        DtoDriverTransport newTransport = null;

        final List<DtoInvolvedTransport> driverTransportsFromTemplate = this.transportByTemplateRepository.findDriverTransportsFromTemplate(driverId, templateId);
        for (final DtoInvolvedTransport dtoInvolvedTransport : driverTransportsFromTemplate) {
            if (previousTransportDate == null || !previousTransportDate.equals(dtoInvolvedTransport.getTransportDate())) {
                newTransport = new DtoDriverTransport(dtoInvolvedTransport);
                dtoDriverTransportList.add(newTransport);
            } else {
                final String passengerFullName = dtoInvolvedTransport.getName();
                newTransport.addPassengerName(passengerFullName);
            }

            previousTransportDate = newTransport.getTransportDate();
        }

        return dtoDriverTransportList;
    }

    public List<DtoPassengerTransport> findPassengerTransportsFromTemplate(final int passengerId, final int templateId) {
        final List<DtoPassengerTransport> passengerTransports = new ArrayList<>();

        final List<DtoInvolvedTransport> passengerTransportsFromTemplate = this.transportByTemplateRepository.findPassengerTransportsFromTemplate(passengerId, templateId);
        for (final DtoInvolvedTransport transport : passengerTransportsFromTemplate) {
            passengerTransports.add(new DtoPassengerTransport(transport));
        }

        return passengerTransports;
    }

    public List<DtoGetPassengersForDriverByDate> getPassengersForDriverByDate(final Integer transportDateId, final Integer driverId) {
        return this.transportByTemplateRepository.getPassengersForDriverByDate(transportDateId, driverId);
    }

    public Transport getDriverForPassengerByDate(Integer transportDateId, Integer passengerId) {
        return this.transportByTemplateRepository.findTransportByPassenger(transportDateId, passengerId);
    }

    //TODO check driver has available seats
    //TODO check input data
    //TODO check if transport allready existed

    /**
     * Gets all the passenger transports that have not been notified to him/her.
     * As JPQL does not supports unions, it has to be done adding the items manually
     * @param templateId
     * @param passengerId
     * @return list of transports
     */
    public List<Transport> getPassengerTransportsWithoutNotification(final Integer templateId, final Integer passengerId) {
        final List<Transport> passengerTransportsWithoutNotification = this.transportByTemplateRepository.getPassengerTransportsWithoutNotification(templateId, passengerId);
        final List<InvolvedTransportNotification> passengerNotificationsWithoutTransport = this.involvedTransportNotificationService.getPassengerNotificationsWithoutTransport(templateId, passengerId);

        for (final InvolvedTransportNotification notif : passengerNotificationsWithoutTransport) {

            //A passenger can be notified that he does not have a driver assigned
            if (notif.getDriverCode() != null) {
                final Transport transport = new Transport(notif.getPassengerCode(), notif.getDriverCode(), notif.getTransportDateCode());
                passengerTransportsWithoutNotification.add(transport);
            }
        }

        return passengerTransportsWithoutNotification;
    }

    /**
     * Gets all the driver transports that have not been notified to him/her
     * As JPQL does not supports unions, it has to be done adding the items manually
     * @param templateId
     * @param driverId
     * @return list of transports
     */
    public List<Transport> getDriverTransportsWithoutNotification(final Integer templateId, final Integer driverId) {
        final List<Transport> driverTransportsWithoutNotification = this.transportByTemplateRepository.getDriverTransportsWithoutNotification(templateId, driverId);
        final List<InvolvedTransportNotification> getDriverNotificationsWithoutTransport = this.involvedTransportNotificationService.getDriverNotificationsWithoutTransport(templateId, driverId);

        for (final InvolvedTransportNotification notif : getDriverNotificationsWithoutTransport) {
            if (notif.getPassengerCode() != null) {
                final Transport transport = new Transport(notif.getPassengerCode(), notif.getDriverCode(), notif.getTransportDateCode());
                driverTransportsWithoutNotification.add(transport);
            }
        }

        return driverTransportsWithoutNotification;
    }
}
