package com.transports.spring.vo.notificationview.passenger;

import java.util.HashMap;
import java.util.Map;

import com.transports.spring.model.Passenger;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoNotifVPassenger {
    
    private Passenger passenger;
    
    /**
	 * Key: DateId
	 * Value: Info of the passenger for that date
	 */
	private final Map<Integer, VoNotifVPassengerDateInfo> passengerInfoMap;

	public VoNotifVPassenger(final Passenger passenger) {
		this.passenger = passenger;
		this.passengerInfoMap = new HashMap<>();
	}

	public VoNotifVPassenger() {
		this.passenger = new Passenger();
		this.passengerInfoMap = new HashMap<>();
	}

	public void addPassengerInfo(final Integer dateId, final VoNotifVPassengerDateInfo screenPassenger) {
		this.passengerInfoMap.put(dateId, screenPassenger);
	}
}
