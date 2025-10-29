package com.transports.spring.vo.transportcrudview.passenger;

import java.util.HashMap;
import java.util.Map;

import com.transports.spring.model.Passenger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoTransCVPassenger {
	private Passenger passenger;
	private VoTransCVGeneralPassengerIcon voScreenGeneralPassengerIcon;

	/**
	 * Key: DateId
	 * Value: Info of the passenger for that date
	 */
	private final Map<Integer, VoTransCVPassengerDateInfo> passengerInfoMap;

	public VoTransCVPassenger(final VoTransCVGeneralPassengerIcon voScreenGeneralPassengerIcon, final Passenger passenger) {
		this.voScreenGeneralPassengerIcon = voScreenGeneralPassengerIcon;
		this.passenger = passenger;
		this.passengerInfoMap = new HashMap<>();
	}

	public void addPassengerInfo(final Integer dateId, final VoTransCVPassengerDateInfo screenPassenger) {
		this.passengerInfoMap.put(dateId, screenPassenger);
	}
}
