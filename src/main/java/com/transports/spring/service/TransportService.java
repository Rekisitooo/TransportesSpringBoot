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
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.operation.transportcrudview.driver.notification.DriverNotificationIconCalculator;
import com.transports.spring.operation.transportcrudview.passenger.notification.PassengerNotificationIconCalculator;
import com.transports.spring.repository.ITransportRepository;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

@Service
public class TransportService {

    private final ITransportRepository transportByTemplateRepository;
    private final InvolvedTransportNotificationService involvedTransportNotificationService;

    public TransportService(final ITransportRepository transportByTemplateRepository, InvolvedTransportNotificationService involvedTransportNotificationService) {
        this.transportByTemplateRepository = transportByTemplateRepository;
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
            final Map<Integer, VoCompleteTransport> transportsMap = this.findPassengerTransportsFromTemplate(passenger, templateId);
            passengerTransportsMap.put(passenger.getId(), transportsMap);
        }

        return passengerTransportsMap;
    }

    /**
     * @param passenger passenger from the template
     * @param templateId consulted template
     * @return Map<transportDateId, VoCompleteTransport>
     */
    public Map<Integer, VoCompleteTransport> findPassengerTransportsFromTemplate(final Passenger passenger, final int templateId) {
        final Map<Integer, VoCompleteTransport> passengerTransportsMap = new HashMap<>();

        final List<VoCompleteTransport> allPassengerTransportsFromTemplate = this.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);
        for (final VoCompleteTransport voCompleteTransport : allPassengerTransportsFromTemplate) {
            passengerTransportsMap.put(voCompleteTransport.getTransport().getTransportKey().getTransportDateId(), voCompleteTransport);
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
            final Map<Integer, List<VoCompleteTransport>> transportPassengersMap = this.findDriverTransportsFromTemplate(driver, templateId);
            driverTransportsMap.put(driver.getId(), transportPassengersMap);
        }

        return driverTransportsMap;
    }

    /**
     * @param driver driver from the template
     * @param templateId consulted template
     * @return Map<transportDateId, List<VoCompleteTransport>>
     */
    public Map<Integer, List<VoCompleteTransport>> findDriverTransportsFromTemplate(final Driver driver, final int templateId) {        
        final Map<Integer, List<VoCompleteTransport>> transportPassengersMap = new HashMap<>();

        final List<VoCompleteTransport> allDriverTransportsFromTemplate = this.findAllDriverTransportsFromTemplate(driver.getId(), templateId);
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
        
        return transportPassengersMap;
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
     * Gets how the general driver notification icon should be at the moment: red, invisible...
     * @param templateId
     * @param passengerId
     * @return
     */
    public VoTCVNotificationIconDisplay getGeneralPassengerNotificationIconStatus(final Integer templateId, final Integer passengerId) {
        final Passenger passenger = new Passenger(passengerId, null);
        final Map<Integer, VoCompleteNotification> passengerNotificationsMap = this.involvedTransportNotificationService.getPassengerNotificationsMapByTemplate(templateId, passenger);
        final Map<Integer, VoCompleteTransport> passengerTransportsMap =  this.findPassengerTransportsFromTemplate(passenger, templateId);
        
        return new PassengerNotificationIconCalculator().calculateGeneralNotificationsPassengerIcon(passengerNotificationsMap, passengerTransportsMap);
    }

    /**
     * Gets how the general driver notification icon should be at the moment: red, invisible...
     * @param templateId
     * @param driverId
     * @return
     */
    public VoTCVNotificationIconDisplay getGeneralDriverNotificationIconStatus(final Integer templateId, final Integer driverId) {
        final Driver driver = new Driver(driverId, null);
        final Map<Integer, List<VoCompleteNotification>> driverNotificationsMap = this.involvedTransportNotificationService.getDriverNotificationsMapByTemplate(templateId, driver);
        final Map<Integer, List<VoCompleteTransport>> driverTransportsMap =  this.findDriverTransportsFromTemplate(driver, templateId);
        
        return new DriverNotificationIconCalculator().calculateGeneralNotificationDriverIcon(driverNotificationsMap, driverTransportsMap);
    }
}
