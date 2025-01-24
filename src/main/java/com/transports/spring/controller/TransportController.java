package com.transports.spring.controller;

import com.transports.spring.dto.DtoTransport;
import com.transports.spring.service.TransportService;
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
    public ResponseEntity<String> updateDriverInTransport(@RequestBody DtoTransport body){
        final int transportDateId = body.getTransportDateId();
        final int driverId = body.getDriverId();
        final int passengerId = body.getPassengerId();
        this.transportService.updateDriverInTransport(transportDateId, driverId, passengerId);
        //final ServiceResponse<String> response = new ServiceResponse<>("success", "fulano");
        return new ResponseEntity<>("fulano", HttpStatus.OK);
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