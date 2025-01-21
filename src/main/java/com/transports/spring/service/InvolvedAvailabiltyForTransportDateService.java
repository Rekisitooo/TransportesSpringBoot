package com.transports.spring.service;

import com.transports.spring.model.InvolvedAvailabiltyForTransportDate;
import com.transports.spring.model.TransportByTemplate;
import com.transports.spring.model.TransportDateByTemplate;
import com.transports.spring.repository.IInvolvedAvailabiltyForTransportDateRepository;
import com.transports.spring.repository.ITransportDateByTemplateRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvolvedAvailabiltyForTransportDateService {

    private final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository;

    public InvolvedAvailabiltyForTransportDateService(final IInvolvedAvailabiltyForTransportDateRepository involvedAvailabiltyForTransportDateRepository) {
        this.involvedAvailabiltyForTransportDateRepository = involvedAvailabiltyForTransportDateRepository;
    }

    /**
     * @param templateId
     * @return Map<DateId, List<PassengerId>>
     */
    public Map<Integer, List<Integer>> findAllPassengersAvailableForDate(final int templateId) {
        final List<InvolvedAvailabiltyForTransportDate> availablePassengersForDate = this.involvedAvailabiltyForTransportDateRepository.findAllPassengersAvailableForDate(templateId);
        return findAllInvolvedsAvailableForDate(availablePassengersForDate);
    }

    /**
     * @param templateId
     * @return Map<DateId, List<DriverId>>
     */
    public Map<Integer, List<Integer>> findAllDriversAvailableForDate(final int templateId) {
        final List<InvolvedAvailabiltyForTransportDate> availableDriversForDate = this.involvedAvailabiltyForTransportDateRepository.findAllDriversAvailableForDate(templateId);
        return findAllInvolvedsAvailableForDate(availableDriversForDate);
    }

    private Map<Integer, List<Integer>> findAllInvolvedsAvailableForDate(final List<InvolvedAvailabiltyForTransportDate> availableInvolvedsForDate) {
        final Map<Integer, List<Integer>> availableInvolvedForDateMap = new HashMap<>();

        for (final InvolvedAvailabiltyForTransportDate transportByTemplate : availableInvolvedsForDate) {
            final int transportDateId = transportByTemplate.getTransportDateCode();
            List<Integer> availableInvolvedsForTransportDate = availableInvolvedForDateMap.get(transportDateId);
            if (availableInvolvedsForTransportDate == null) {
                availableInvolvedsForTransportDate = new ArrayList<>();
                availableInvolvedsForTransportDate.add(transportByTemplate.getInvolvedCode());
                availableInvolvedForDateMap.put(transportDateId,availableInvolvedsForTransportDate);
            } else {
                availableInvolvedsForTransportDate.add(transportByTemplate.getInvolvedCode());
            }

        }

        return availableInvolvedForDateMap;
    }
}
