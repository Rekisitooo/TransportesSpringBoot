package com.transports.spring.service;

import com.transports.spring.dto.DtoDriverTransport;
import com.transports.spring.dto.DtoGetPassengersForDriverByDate;
import com.transports.spring.dto.DtoInvolvedTransport;
import com.transports.spring.dto.DtoPassengerTransport;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.repository.ITransportRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransportService {

    private final ITransportRepository transportByTemplateRepository;
    private final InvolvedByTemplateService involvedByTemplateService;

    public TransportService(final ITransportRepository transportByTemplateRepository, InvolvedByTemplateService involvedByTemplateService) {
        this.transportByTemplateRepository = transportByTemplateRepository;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    /**
     * @param passengerList passengers available in the consulted template
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, Transport>>
     */
    public Map<Integer, Map<Integer, Transport>> findAllPassengerTransportsFromTemplate(final List<Passenger> passengerList, final int templateId) {
        final Map<Integer, Map<Integer, Transport>> passengerTransportsMap = new HashMap<>();
        for (final Passenger passenger : passengerList) {

            Map<Integer, Transport> transportsMap = new HashMap<>();
            final List<Transport> allPassengerTransportsFromTemplate = this.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);
            for (final Transport transportByTemplate : allPassengerTransportsFromTemplate) {
                transportsMap.put(transportByTemplate.getTransportKey().getTransportDateId(), transportByTemplate);
            }
            passengerTransportsMap.put(passenger.getId(), transportsMap);
        }

        return passengerTransportsMap;
    }

    /**
     * @param driverList drivers available in the consulted template
     * @param templateId consulted template
     * @return Map<driverId, Map<transportDateId, List<Passenger>>>
     */
    public Map<Integer, Map<Integer, List<Passenger>>> findAllDriverTransportsFromTemplate(final List<Driver> driverList, final int templateId) throws InvolvedDoesNotExistException {
        final Map<Integer, Map<Integer, List<Passenger>>> driverTransportsMap = new HashMap<>();

        for (final Driver driver : driverList) {
            final List<Transport> allDriverTransportsFromTemplate = this.findAllDriverTransportsFromTemplate(driver.getId(), templateId);
            Map<Integer, List<Passenger>> transportPassengersMap = new HashMap<>();
            driverTransportsMap.put(driver.getId(), transportPassengersMap);

            for (final Transport transport : allDriverTransportsFromTemplate) {
                final Integer transportDateId = transport.getTransportKey().getTransportDateId();
                final Passenger passenger = this.involvedByTemplateService.getPassengerByIdAndTemplate(transport.getTransportKey().getPassengerId(), templateId);

                List<Passenger> transportPassengerList = transportPassengersMap.get(transportDateId);
                if (transportPassengerList == null) {
                    transportPassengerList = new ArrayList<>(Collections.singletonList(passenger));
                    transportPassengersMap.put(transportDateId, transportPassengerList);
                } else {
                    transportPassengerList.add(passenger);
                }
            }
        }

        return driverTransportsMap;
    }

    public List<Transport> findAllPassengerTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllPassengerTransportsFromTemplate(passengerId, templateId);
    }

    public List<Transport> findAllDriverTransportsFromTemplate(final int driverId, final int templateId) {
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
}
