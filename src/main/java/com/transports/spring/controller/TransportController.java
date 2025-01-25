package com.transports.spring.controller;

import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.Transport;
import com.transports.spring.service.TransportService;
import com.transports.spring.service.resposonse.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/t")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService){
        this.transportService = transportService;
    }

    @Transactional
    @PostMapping("/updateDriver")
    public ResponseEntity<Object> updateDriverInTransport(@RequestBody final DtoTransport body){
        final int transportDateId = body.getTransportDateId();
        final int driverId = body.getDriverId();
        final int passengerId = body.getPassengerId();
        final Transport transport = this.transportService.findTransportByPassenger(transportDateId, passengerId);
        this.transportService.updateDriverInTransport(transportDateId, driverId, passengerId);
        //TODO create if on wrong update and add something to explain that the id of the driver in charge before the update is needed to update the other table
        body.setP(transport.getTransportKey().getDriverId());
        final ServiceResponse<DtoTransport> response = new ServiceResponse<>("ok", body);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/create")
    public ResponseEntity<String> createDriverInTransport(@RequestBody DtoTransport body){
        System.out.println("createTransport");
        //this.transportService.createTransport(transportDateId, driverId, passengerId);
        return new ResponseEntity<>("fulano", HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDriverInTransport(@RequestBody DtoTransport body){
        System.out.println("deleteTransport");
        //this.transportService.deleteTransport(transportDateId, driverId, passengerId);
        return new ResponseEntity<>("fulano", HttpStatus.OK);
    }
}