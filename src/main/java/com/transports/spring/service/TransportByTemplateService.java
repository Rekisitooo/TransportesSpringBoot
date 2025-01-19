package com.transports.spring.service;

import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.TransportByTemplate;
import com.transports.spring.repository.ITransportsByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransportByTemplateService {

    private final ITransportsByTemplateRepository transportByTemplateRepository;

    public TransportByTemplateService(final ITransportsByTemplateRepository transportByTemplateRepository) {
        this.transportByTemplateRepository = transportByTemplateRepository;
    }

    /**
     *
     * @param passengerList passengers available in the consulted template
     * @param templateId consulted template
     * @return Map<transportDateId, Map<passengerId, driverId>>
     */
    public Map<Integer, Map<Integer, Integer>> findAllPassengerTransportsFromTemplate(final List<Passenger> passengerList, final int templateId) {
        final Map<Integer, Map<Integer, Integer>> passengerTransportsMap = new HashMap<>();
        for (final Passenger passenger : passengerList) {
            final List<TransportByTemplate> allPassengerTransportsFromTemplate = this.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);

            for (final TransportByTemplate transport : allPassengerTransportsFromTemplate) {
                final Map<Integer, Integer> transportPassengersMap = new HashMap<>();
                transportPassengersMap.put(passenger.getId(), transport.getTransportByTemplateKey().getDriverId());
                passengerTransportsMap.put(transport.getTransportByTemplateKey().getTransportDateId(), transportPassengersMap);
            }
        }

        return passengerTransportsMap;
    }

    /**
     * @param driverList drivers available in the consulted template
     * @param templateId consulted template
     * @return Map<transportDateId, Map<driverId, List<passengerIds>>>
     */
    public Map<Integer, Map<Integer, List<Integer>>> findAllDriverTransportsFromTemplate(final List<Driver> driverList, final int templateId) {
        final Map<Integer, Map<Integer, List<Integer>>> driverTransportsMap = new HashMap<>();
        for (final Driver driver : driverList) {
            final List<TransportByTemplate> allDriverTransportsFromTemplate = this.findAllDriverTransportsFromTemplate(driver.getId(), templateId);
            final Integer transportDriverId = driver.getId();

            for (final TransportByTemplate transport : allDriverTransportsFromTemplate) {
                final Integer transportDateId = transport.getTransportByTemplateKey().getTransportDateId();
                final Integer transportPassengerId = transport.getTransportByTemplateKey().getPassengerId();
                Map<Integer, List<Integer>> transportPassengersMap = driverTransportsMap.get(transportDateId);
                List<Integer> transportPassengerList;

                if (isTransportDateInDriverTransportsMap(transportPassengersMap)) {
                    transportPassengerList = transportPassengersMap.get(transportDriverId);
                    if (isDriverInMap(transportPassengerList)) {
                        transportPassengerList.add(transportPassengerId);
                    } else {
                        transportPassengerList = new ArrayList<>(Collections.singletonList(transportPassengerId));
                        transportPassengersMap = new HashMap<>();
                        transportPassengersMap.put(transportDriverId, transportPassengerList);
                    }
                } else {
                    transportPassengerList = new ArrayList<>(Collections.singletonList(transportPassengerId));
                    transportPassengersMap = new HashMap<>();
                    transportPassengersMap.put(transportDriverId, transportPassengerList);
                    driverTransportsMap.put(transportDateId, transportPassengersMap);
                }
            }
        }

        return driverTransportsMap;
    }

    private static boolean isDriverInMap(List<Integer> transportPassengerList) {
        return transportPassengerList != null;
    }

    private static boolean isTransportDateInDriverTransportsMap(final Map<Integer, List<Integer>> transportPassengersMap) {
        return transportPassengersMap != null;
    }

    public List<TransportByTemplate> findAllPassengerTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllPassengerTransportsFromTemplate(passengerId, templateId);
    }

    public List<TransportByTemplate> findAllDriverTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllDriverTransportsFromTemplate(passengerId, templateId);
    }
}
