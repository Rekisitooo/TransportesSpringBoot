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
    private final InvolvedByTemplateService involvedByTemplateService;

    public TransportByTemplateService(final ITransportsByTemplateRepository transportByTemplateRepository, InvolvedByTemplateService involvedByTemplateService) {
        this.transportByTemplateRepository = transportByTemplateRepository;
        this.involvedByTemplateService = involvedByTemplateService;
    }

    /**
     * @param passengerList passengers available in the consulted template
     * @param templateId consulted template
     * @return Map<passengerId, Map<transportDateId, TransportByTemplate>>
     */
    public Map<Integer, Map<Integer, TransportByTemplate>> findAllPassengerTransportsFromTemplate(final List<Passenger> passengerList, final int templateId) {
        final Map<Integer, Map<Integer, TransportByTemplate>> passengerTransportsMap = new HashMap<>();
        for (final Passenger passenger : passengerList) {

            Map<Integer, TransportByTemplate> transportsMap = new HashMap<>();
            final List<TransportByTemplate> allPassengerTransportsFromTemplate = this.findAllPassengerTransportsFromTemplate(passenger.getId(), templateId);
            for (final TransportByTemplate transportByTemplate : allPassengerTransportsFromTemplate) {
                transportsMap.put(transportByTemplate.getTransportByTemplateKey().getTransportDateId(), transportByTemplate);
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
    public Map<Integer, Map<Integer, List<Passenger>>> findAllDriverTransportsFromTemplate(final List<Driver> driverList, final int templateId) {
        final Map<Integer, Map<Integer, List<Passenger>>> driverTransportsMap = new HashMap<>();

        for (final Driver driver : driverList) {
            final List<TransportByTemplate> allDriverTransportsFromTemplate = this.findAllDriverTransportsFromTemplate(driver.getId(), templateId);
            Map<Integer, List<Passenger>> transportPassengersMap = new HashMap<>();
            driverTransportsMap.put(driver.getId(), transportPassengersMap);

            for (final TransportByTemplate transport : allDriverTransportsFromTemplate) {
                final Integer transportDateId = transport.getTransportByTemplateKey().getTransportDateId();
                final Passenger passenger = this.involvedByTemplateService.getByIdAndTemplate(transport.getTransportByTemplateKey().getPassengerId(), templateId);

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

    public List<TransportByTemplate> findAllPassengerTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllPassengerTransportsFromTemplate(passengerId, templateId);
    }

    public List<TransportByTemplate> findAllDriverTransportsFromTemplate(final int passengerId, final int templateId) {
        return this.transportByTemplateRepository.findAllDriverTransportsFromTemplate(passengerId, templateId);
    }
}
