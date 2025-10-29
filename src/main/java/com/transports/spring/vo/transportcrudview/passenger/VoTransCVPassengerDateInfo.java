package com.transports.spring.vo.transportcrudview.passenger;

import com.transports.spring.vo.transportcrudview.notification.VoTCVNotificationIconDisplay;
import com.transports.spring.vo.transportcrudview.passenger.transport.VoPassengerTransportDisplay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoTransCVPassengerDateInfo {

	/**
	 * For the notification tab
	 */
	private String assignedDriverName;
	
	/**
	 * For the notification tab
	 */
	private String notifiedDriverName;
	
    private boolean assistsOnDate;
	private boolean needsTransport;
	private VoTCVNotificationIconDisplay voNotificationIconDisplay;
	private VoPassengerTransportDisplay voPassengerTransportDisplay;
}
