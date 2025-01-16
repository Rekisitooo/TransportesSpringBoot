package com.transports.spring.service;

import com.transports.spring.dto.DtoGetAllPassengers;
import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.InvolvedByTransport;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.TransportByTemplate;
import com.transports.spring.repository.IInvolvedByTransportRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvolvedByTransportService {

    private final IInvolvedByTransportRepository involvedByTransportRepository;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportsByTemplateService transportsByTemplateService;

    public InvolvedByTransportService(final IInvolvedByTransportRepository involvedByTransportRepository, InvolvedByTemplateService involvedByTemplateService, TransportsByTemplateService transportsByTemplateService) {
        this.involvedByTransportRepository = involvedByTransportRepository;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportsByTemplateService = transportsByTemplateService;
    }

    // DRIVERS Map<date, Map<driverName, Lis0t<passengerName>>>
    // PASSENGERS Map<date, Map<passengerName, driverName>>
    public Map<String, Map<String, String>> findAllPassengerTransports(final int templateId, final List<TransportByTemplate> templateDates){
        final Map<String, Map<String, String>> passengerTransportsMap = new HashMap<>();

        final List<Passenger> passengerList = this.involvedByTemplateService.getAllPassengersFromTemplate(templateId);
        for (final Passenger passenger : passengerList) {
            List<DtoTransport> allTransportsByPassenger = this.transportsByTemplateService.findAllTransportsByPassengerFromTemplate(passenger.getId(), templateId);
        }


        for (TransportByTemplate templateDate : templateDates) {
            //create a custom query that also retrieves the names of the involved ones
            Optional<InvolvedByTransport> byId = this.involvedByTransportRepository.findByIdWithName(templateDate.getId());
            final Map<String, String> passengerTransportsMap2 = new HashMap<>();
        }
    }
}
