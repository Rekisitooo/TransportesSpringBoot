package com.transports.spring.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.model.Template;
import com.transports.spring.repository.ITemplateRepository;

@Service
public class TemplateService {

    private final ITemplateRepository templateRepository;

    public TemplateService(final ITemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public DtoTemplateData getTemplateDataById(final int templateId) {
        final Template template = this.findById(templateId);
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.parseInt(template.getYear()), Integer.parseInt(template.getMonth()) - 1, 1);
        final int lastMonthDate = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return new DtoTemplateData(template, lastMonthDate);
    }

    public Template findById(final int templateId) {
        return this.templateRepository.findById(templateId).orElseThrow();
    }

    public List<Template> getAll() {
        return this.templateRepository.findAll();
    }

    @GetMapping("/getAllWithMonthNames")
    public List<Template> getAllWithMonthNames() {
        final List<Template> templateList = this.getAll();
        for (final Template template : templateList) {
            template.setMonth(template.getMonthName());
        }

        return templateList;
    }

    @GetMapping("/getAllWithMonthNamesTemplateSelectionView")
    public List<List<Template>> getAllWithMonthNamesTemplateSelectionView() {
        final List<List<Template>> templateListList = new ArrayList<>();
        final List<Template> templateList = this.getAllWithMonthNames();
        for (int i = 0; i < templateList.size(); i += 6) {
            templateListList.add(templateList.subList(i, Math.min(i + 6, templateList.size())));
        }
        return templateListList;
    }

    public Template create(final Template template) {
        return this.templateRepository.save(template);
    }

    public ResponseEntity<Template> delete(final int templateId) {
        final Template existingTemplate = this.findById(templateId);
        this.templateRepository.delete(existingTemplate);
        return ResponseEntity.ok().build();
    }
/* 
    public List<VoScreenPassengerTransportsTableInfo> getScreenPassengerTransportsTableInfo(VoScreenPassengerTransportsTableInfo voScreenPassengerTransportsTableInfo)  {
        
        VoScreenPassengerTransportsTableInfo = new VoScreenPassengerTransportsTableInfo();
        List<DtoTransportCrudScreenPassenger> listDtoTransportCrudScreenPassenger = new ArrayList<>();


        for each Passenger

            GeneralPassengerIcon = PassengerIconCalculator.calculateGeneralPassengerIcons(
                passengerNotifications.get(passengerId), 
                passengerTransports.get(PassengerId)
            )

            DtoTransportCrudScreenPassenger = new DtoTransportCrudScreenPassenger(Passenger, GeneralPassengerIcon)
            
            for each Date
                assistsOnDate = passengersAssistanceDates.get(Date.id).indexOf(Passenger).get(Passenger); 
                needsTransport = passengersAssistanceDates.get(Passenger.id).get(Date.id).needsTransport();
                NotificationIconDisplay = 																NotificationIconDisplay.calculateDatePassengerIcon(
                        passengerNotifications.get(PassengerId).get(Date.id), 
                        passengerTransports.get(PassengerId).get(Date.id)
                    )
                
                PassengerTransportDisplay = new PassengerTransportDisplay(
                    passengerTransports.get(PassengerId).get(Date.id),
                    driversAvailableByDate.get(Date.id)
                )

                ScreenPassenger = new ScreenPassenger(assistsOnDate, needsTransport, NotificationIconDisplay, PassengerTransportDisplay)
                DtoTransportCrudScreenPassenger.add(Date.id, ScreenPassenger)
            end for each Date

        end for each Passenger

        return listDtoTransportCrudScreenPassenger;
       
    } */
}
