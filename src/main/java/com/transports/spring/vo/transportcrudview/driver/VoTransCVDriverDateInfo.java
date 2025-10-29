package com.transports.spring.vo.transportcrudview.driver;

import java.util.ArrayList;
import java.util.List;

import com.transports.spring.model.Passenger;
import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoTransCVDriverDateInfo {
	private boolean assistsOnDate;
    private boolean providesTransport;
	private List<Passenger> assignedPassengerList;
	private List<Passenger> notifiedPassengerList;
	private VoTCVNotificationIconDisplay voNotificationIconDisplay;
    
	public VoTransCVDriverDateInfo() {
		this.assistsOnDate = false;
		this.providesTransport = false;
		this.voNotificationIconDisplay = new VoTCVNotificationIconDisplay();
		this.assignedPassengerList = new ArrayList<>();
		this.notifiedPassengerList = new ArrayList<>();
	}

	public void addAssignedPassenger(final Passenger passenger) {
		this.assignedPassengerList.add(passenger);
	}

	public void addNotifiedPassenger(final Passenger passenger) {
		this.notifiedPassengerList.add(passenger);
	}
}
