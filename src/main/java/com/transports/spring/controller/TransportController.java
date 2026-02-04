package com.transports.spring.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.transports.spring.dto.DtoGetPassengersForDriverByDate;
import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.service.TransportService;
import com.transports.spring.service.notification.GeneralNotifIconService;
import com.transports.spring.service.response.ServiceResponse;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/t")
public class TransportController {

    private final TransportService transportService;
    private final GeneralNotifIconService generalNotifIconService;

    public TransportController( 
        final TransportService transportService, 
        final GeneralNotifIconService generalNotifIconService){
        this.transportService = transportService;
        this.generalNotifIconService = generalNotifIconService;
    }

    @Transactional
    @PutMapping("/updateDriver")
    public ResponseEntity<Object> updateDriverInTransport(@RequestBody final DtoTransport body){
        final TransportKey transportKey = transformDtoTransportToTransportKey(body);
        final Transport transport = this.transportService.findTransportByPassenger(transportKey.getTransportDateId(), transportKey.getPassengerId());
        this.transportService.updateDriverInTransport(transportKey);

        body.setP(transport.getTransportKey().getDriverId());
        return new ResponseEntity<>(new ServiceResponse<>("ok", body), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> createDriverInTransport(@RequestBody DtoTransport body){
        final TransportKey transportKey = transformDtoTransportToTransportKey(body);
        final Transport transport = this.transportService.createTransport(transportKey);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ServiceResponse<>("ok", body));
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteDriverInTransport(@RequestBody DtoTransport body){
        final TransportKey transportKey = transformDtoTransportToTransportKey(body);
        final Transport transport = this.transportService.findTransportByPassenger(transportKey.getTransportDateId(), transportKey.getPassengerId());
        final int driverId = transport.getTransportKey().getDriverId();

        transportKey.setDriverId(driverId);
        this.transportService.deleteTransport(transportKey);

        body.setDriverId(driverId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", body));
    }

    @GetMapping("/getPassengersForDriverByDate")
    public ResponseEntity<Object> getPassengersForDriverByDate(@RequestParam Integer transportDateCode, @RequestParam Integer driverId) {
        final List<DtoGetPassengersForDriverByDate> list = this.transportService.getPassengersForDriverByDate(transportDateCode, driverId);
        if (list != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ServiceResponse<>("No passengers found for the given driver and date", list)
            );
        }
    }

    @GetMapping("/getDriverForPassengerByDate")
    public ResponseEntity<Object> getDriverForPassengerByDate(@RequestParam Integer transportDateId, @RequestParam Integer passengerId) {
        final Transport transport = this.transportService.getDriverForPassengerByDate(transportDateId, passengerId);
        if (transport != null) {
            return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", transport));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ServiceResponse<>("No transport found for the given passenger and date", null)
            );
        }
    }

    private static TransportKey transformDtoTransportToTransportKey(DtoTransport body) {
        return TransportKey.builder().
                driverId(body.getDriverId()).
                transportDateId(body.getTransportDateId()).
                passengerId(body.getPassengerId()).
                build();
    }

    /**
     * Gets all the passenger transports that have not been notified to him/her.
     *
     * @param templateId
     * @param passengerId
     * @return list of transports
     */
    @GetMapping("/getGeneralPassengerNotificationIconStatus")
    public ResponseEntity<Object> getGeneralPassengerNotificationIconStatus(@RequestParam Integer templateId, @RequestParam Integer passengerId) {
        final VoTCVNotificationIconDisplay passengerGeneralNotifIcon = this.generalNotifIconService.getGeneralPassengerNotificationIconStatus(templateId, passengerId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", passengerGeneralNotifIcon));
    }

    /**
     * Gets all the driver transports that have not been notified to him/her
     * @param templateId
     * @param driverId
     * @return list of transports
     */
    @GetMapping("/getGeneralDriverNotificationIconStatus")
    public ResponseEntity<Object> getGeneralDriverNotificationIconStatus(@RequestParam Integer templateId, @RequestParam Integer driverId) {
        final VoTCVNotificationIconDisplay driverGeneralNotifIcon = this.generalNotifIconService.getGeneralDriverNotificationIconStatus(templateId, driverId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", driverGeneralNotifIcon));
    }
}