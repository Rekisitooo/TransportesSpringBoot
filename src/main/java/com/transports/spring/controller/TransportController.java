package com.transports.spring.controller;

import com.transports.spring.dto.DtoGetPassengersForDriverByDate;
import com.transports.spring.dto.DtoTransport;
import com.transports.spring.model.Transport;
import com.transports.spring.model.key.TransportKey;
import com.transports.spring.service.TransportService;
import com.transports.spring.service.response.ServiceResponse;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/t")
public class TransportController {

    private final TransportService transportService;

    public TransportController(TransportService transportService){
        this.transportService = transportService;
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
        this.transportService.createTransport(transportKey);
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
    public ResponseEntity<Object> getPassengersForDriverByDate(@RequestParam Integer transportDateCode, @RequestParam Integer involvedCommunicatedId) {
        final List<DtoGetPassengersForDriverByDate> list = this.transportService.getPassengersForDriverByDate(transportDateCode, involvedCommunicatedId);
        return ResponseEntity.status(HttpStatus.OK).body(new ServiceResponse<>("ok", list));
    }

    private static TransportKey transformDtoTransportToTransportKey(DtoTransport body) {
        return TransportKey.builder().
                driverId(body.getDriverId()).
                transportDateId(body.getTransportDateId()).
                passengerId(body.getPassengerId()).
                build();
    }
}