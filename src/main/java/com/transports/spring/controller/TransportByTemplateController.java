package com.transports.spring.controller;

import com.transports.spring.service.TransportByTemplateService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transportByTemplate")
public final class TransportByTemplateController {

    private TransportByTemplateService transportByTemplateService;

    public TransportByTemplateController(TransportByTemplateService transportByTemplateService){
        this.transportByTemplateService = transportByTemplateService;
    }

    @RequestMapping(value="updateDriverInTransport", method=RequestMethod.PUT, produces= MimeTypeUtils.TEXT_PLAIN_VALUE)
    public void updateDriverInTransport(@RequestParam("transportDateId") final int transportDateId, @RequestParam("driverId") final int driverId, @RequestParam("passengerId") final int passengerId){
        this.transportByTemplateService.updateDriverInTransport(transportDateId, driverId, passengerId);
    }

    @RequestMapping(value="createTransport", method=RequestMethod.POST, produces= MimeTypeUtils.TEXT_PLAIN_VALUE)
    public void createDriverInTransport(@RequestParam("transportDateId") final int transportDateId, @RequestParam("driverId") final int driverId, @RequestParam("passengerId") final int passengerId){
        System.out.println("createTransport");
        //this.transportByTemplateService.createTransport(transportDateId, driverId, passengerId);
    }

    @RequestMapping(value="deleteTransport", method=RequestMethod.DELETE, produces= MimeTypeUtils.TEXT_PLAIN_VALUE)
    public void deleteDriverInTransport(@RequestParam("transportDateId") final int transportDateId, @RequestParam("driverId") final int driverId, @RequestParam("passengerId") final int passengerId){
        System.out.println("deleteTransport");
        //this.transportByTemplateService.deleteTransport(transportDateId, driverId, passengerId);
    }
}