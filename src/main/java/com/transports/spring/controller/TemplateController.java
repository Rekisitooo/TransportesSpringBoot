package com.transports.spring.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.transports.spring.dto.DtoAddNewDateForm;
import com.transports.spring.dto.DtoDriverList;
import com.transports.spring.dto.DtoPassengerList;
import com.transports.spring.dto.DtoTemplateData;
import com.transports.spring.dto.DtoTemplateDate;
import com.transports.spring.dto.driver.DtoScreenDriverTransportsTableInfo;
import com.transports.spring.dto.notificationview.driver.DtoDriverNotificationVTableInfo;
import com.transports.spring.dto.notificationview.passenger.DtoPassengerNotificationVTableInfo;
import com.transports.spring.dto.passenger.DtoScreenPassengerTransportsTableInfo;
import com.transports.spring.exception.GenerateJpgFromExcelException;
import com.transports.spring.exception.GeneratePdfFromExcelException;
import com.transports.spring.exception.InvolvedDoesNotExistException;
import com.transports.spring.exception.TransportsException;
import com.transports.spring.model.Driver;
import com.transports.spring.model.Passenger;
import com.transports.spring.model.Template;
import com.transports.spring.operation.notificationview.driver.NotificationVDriverDataProvider;
import com.transports.spring.operation.notificationview.passenger.NotificationVPassengerDataProvider;
import com.transports.spring.operation.transportcrudview.driver.DriverTemplateCrudDataProvider;
import com.transports.spring.operation.transportcrudview.passenger.PassengerTemplateCrudDataProvider;
import com.transports.spring.service.AddNewDateToTemplateService;
import com.transports.spring.service.InvolvedAvailabiltyForTransportDateService;
import com.transports.spring.service.InvolvedByTemplateService;
import com.transports.spring.service.InvolvedTransportNotificationService;
import com.transports.spring.service.TemplateDateService;
import com.transports.spring.service.TemplateFileService;
import com.transports.spring.service.TemplateService;
import com.transports.spring.service.TransportService;
import com.transports.spring.vo.completemodel.VoCompleteInvolvedAvailability;
import com.transports.spring.vo.completemodel.VoCompleteNotification;
import com.transports.spring.vo.completemodel.VoCompleteTransport;
import com.transports.spring.vo.notificationview.driver.VoNotifVDriver;
import com.transports.spring.vo.notificationview.passenger.VoNotifVPassenger;
import com.transports.spring.vo.transportcrudview.driver.VoTransCVDriver;
import com.transports.spring.vo.transportcrudview.passenger.VoTransCVPassenger;

@Controller
@RequestMapping("/template")
public final class TemplateController {

    public static final String GENERIC_ERROR_FLASH_ATTR = "genericError";
    private final AddNewDateToTemplateService addNewDateToTemplateService;
    private final TemplateService templateService;
    private final InvolvedByTemplateService involvedByTemplateService;
    private final TransportService transportService;
    private final TemplateDateService templateDateService;
    private final InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService;
    private final TemplateFileService templateFileService;
    private final InvolvedTransportNotificationService involvedTransportNotificationService;

    public TemplateController(AddNewDateToTemplateService addNewDateToTemplateService, TemplateService templateService,
            InvolvedByTemplateService involvedByTemplateService, TransportService transportService,
            TemplateDateService templateDateService,
            InvolvedAvailabiltyForTransportDateService involvedAvailabiltyForTransportDateService,
            TemplateFileService templateFileService,
            InvolvedTransportNotificationService involvedTransportNotificationService) {
        this.addNewDateToTemplateService = addNewDateToTemplateService;
        this.templateService = templateService;
        this.involvedByTemplateService = involvedByTemplateService;
        this.transportService = transportService;
        this.templateDateService = templateDateService;
        this.involvedAvailabiltyForTransportDateService = involvedAvailabiltyForTransportDateService;
        this.templateFileService = templateFileService;
        this.involvedTransportNotificationService = involvedTransportNotificationService;
    }

    @GetMapping("/openTemplate")
    public String openTemplate(final Model model, @RequestParam(value = "id") final int templateId)
            throws InvolvedDoesNotExistException {
        this.addDataToTransportCrud(model, templateId);

        return "transportCRUD";
    }

    @GetMapping("/generate")
    public String generate(final RedirectAttributes rm, final Model model,
            @RequestParam(value = "id") final int templateId) throws InvolvedDoesNotExistException {
        try {
            this.templateFileService.generateFiles(templateId);
        } catch (final IOException | GeneratePdfFromExcelException | GenerateJpgFromExcelException e) {
            rm.addFlashAttribute(GENERIC_ERROR_FLASH_ATTR, GENERIC_ERROR_FLASH_ATTR);
        }

        this.addDataToTransportCrud(model, templateId);

        return "redirect:/template/openTemplate?id=" + templateId;
    }

    @GetMapping("/create")
    public Template create(@RequestBody final Template template) {
        return this.templateService.create(template);
    }

    @GetMapping("/delete")
    public ResponseEntity<Template> delete(@PathVariable(value = "id") final int templateId) {
        return this.templateService.delete(templateId);
    }

    @PostMapping(path = "/newDate/{id}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String newDate(final Model model, final RedirectAttributes rm,
            @PathVariable(value = "id") final int templateId, final DtoAddNewDateForm body)
            throws InvolvedDoesNotExistException {
        try {
            this.addNewDateToTemplateService.newDate(templateId, body);
        } catch (final TransportsException e) {
            rm.addFlashAttribute(GENERIC_ERROR_FLASH_ATTR, GENERIC_ERROR_FLASH_ATTR);
        }

        this.addDataToTransportCrud(model, templateId);
        return "redirect:/template/openTemplate?id=" + templateId;
    }

    private void addDataToTransportCrud(final Model model, final int templateId) throws InvolvedDoesNotExistException {
        final String templateIdString = String.valueOf(templateId);

        final DtoTemplateData template = this.templateService.getTemplateDataById(templateId);
        model.addAttribute("lastMonthDay", template.getLastMonthDay());
        model.addAttribute("monthNumber", template.getMonth());
        model.addAttribute("templateMonth", template.getMonthName().toUpperCase());
        model.addAttribute("templateYear", template.getYear());
        model.addAttribute("templateId", template.getId());

        final List<DtoTemplateDate> templateDates = this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);
        
        // passenger transport info
        final DtoPassengerList dtoPassengerList = this.involvedByTemplateService.getAllPassengersFromTemplateForTemplateView(templateId);
        final List<Passenger> passengersFromTemplateList = dtoPassengerList.getPassengersFromTemplateList();
        final Map<Integer, Map<Integer, VoCompleteTransport>> allPassengerTransportsFromTemplate = this.transportService.findAllPassengerTransportsFromTemplate(passengersFromTemplateList, templateId);
        final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> passengerAssistanceDates = this.involvedAvailabiltyForTransportDateService.findAllPassengersAssistanceDatesByTemplate(templateId);
        final Map<Integer, List<Driver>> driversAvailableForDate = this.involvedAvailabiltyForTransportDateService.findAllDriversAvailableDatesForTemplate(templateId);
        final Map<Integer, Map<Integer, VoCompleteNotification>> passengerTransportNotifications = this.involvedTransportNotificationService.getPassengerNotificationsMapByTemplate(templateIdString);
       
        final DtoScreenPassengerTransportsTableInfo dtoTransportCrudScreenPassenger = new DtoScreenPassengerTransportsTableInfo(
                templateDates, passengersFromTemplateList, allPassengerTransportsFromTemplate, passengerAssistanceDates, passengerTransportNotifications, driversAvailableForDate
        );

        final List<VoTransCVPassenger> voTransportCrudScreenPassengers = PassengerTemplateCrudDataProvider.getScreenPassengerTransportsTableInfo(dtoTransportCrudScreenPassenger);
        model.addAttribute("passengerTransportsTableInfo", voTransportCrudScreenPassengers);

        // driver assistance info
        final DtoDriverList dtoDriverList = this.involvedByTemplateService.getAllDriversFromTemplateForTemplateView(templateId);
        final List<Driver> driversFromTemplateList = dtoDriverList.getDriversFromTemplateList();
        final Map<Integer, Map<Integer, VoCompleteInvolvedAvailability>> driverAssistanceDates = this.involvedAvailabiltyForTransportDateService.findAllDriversAssistanceDates(templateId);
        final Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverTransportNotifications = this.involvedTransportNotificationService.getDriverNotificationsMapByTemplate(templateIdString);
        final Map<Integer, Map<Integer, List<VoCompleteTransport>>> allDriverTransportsFromTemplate = this.transportService.findAllDriverTransportsFromTemplate(driversFromTemplateList, templateId);

        final DtoScreenDriverTransportsTableInfo dtoTransportCrudScreenDriver = new DtoScreenDriverTransportsTableInfo(
                templateDates, driversFromTemplateList, allDriverTransportsFromTemplate, driverAssistanceDates, driverTransportNotifications
        );

        final List<VoTransCVDriver> voTransportCrudScreenDrivers = DriverTemplateCrudDataProvider.getScreenDriverTransportsTableInfo(dtoTransportCrudScreenDriver);
        model.addAttribute("driverTransportsTableInfo", voTransportCrudScreenDrivers);
    }

    @GetMapping("/openNotificationsTab")
    public String openNotificationsTab(final Model model, @RequestParam(value = "id") final int templateId) {
        final String templateIdString = String.valueOf(templateId);

        final List<DtoTemplateDate> templateDates = this.templateDateService.findAllMonthDatesWithNameDayOfTheWeekByTemplateId(templateId);
        model.addAttribute("templateDates", templateDates);

        // passenger transport info
        final DtoPassengerList dtoPassengerList = this.involvedByTemplateService.getAllPassengersFromTemplateForTemplateView(templateId);
        final List<Passenger> passengersFromTemplateList = dtoPassengerList.getPassengersFromTemplateList();
        final Map<Integer, Map<Integer, VoCompleteNotification>> passengerTransportNotifications = this.involvedTransportNotificationService.getPassengerNotificationsMapByTemplate(templateIdString);    

        final DtoPassengerNotificationVTableInfo dtoPassengerNotificationVTableInfo = new DtoPassengerNotificationVTableInfo(
                templateDates, passengersFromTemplateList, passengerTransportNotifications
        );

        final List<VoNotifVPassenger> voTransportCrudScreenPassengers = NotificationVPassengerDataProvider.getScreenPassengerNotificationsTableInfo(dtoPassengerNotificationVTableInfo);
        model.addAttribute("passengerNotificationsTableInfo", voTransportCrudScreenPassengers);

        // driver transport info
        final DtoDriverList dtoDriverList = this.involvedByTemplateService.getAllDriversFromTemplateForTemplateView(templateId);
        final List<Driver> driversFromTemplateList = dtoDriverList.getDriversFromTemplateList();
        final Map<Integer, Map<Integer, List<VoCompleteNotification>>> driverTransportNotifications = this.involvedTransportNotificationService.getDriverNotificationsMapByTemplate(templateIdString);

        final DtoDriverNotificationVTableInfo dtoDriverNotificationVTableInfo = new DtoDriverNotificationVTableInfo(
                templateDates, driversFromTemplateList, driverTransportNotifications
        );

        final List<VoNotifVDriver> voTransportCrudScreenDrivers = NotificationVDriverDataProvider.getScreenDriverNotificationsTableInfo(dtoDriverNotificationVTableInfo);
        model.addAttribute("driverNotificationsTableInfo", voTransportCrudScreenDrivers);

        return "components/transportcrud/notifications/notifications :: notifications";
    }

    @GetMapping("/openTransportsTab")
    public String openTransportsTab(final Model model, @RequestParam(value = "id") final int templateId)
            throws InvolvedDoesNotExistException {
        this.addDataToTransportCrud(model, templateId);

        return "components/transportcrud/transports/transports :: transports";
    }
}