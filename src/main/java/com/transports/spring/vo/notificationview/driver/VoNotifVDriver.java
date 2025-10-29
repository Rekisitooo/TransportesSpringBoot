package com.transports.spring.vo.notificationview.driver;

import java.util.HashMap;
import java.util.Map;

import com.transports.spring.model.Driver;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoNotifVDriver {
    
    private Driver driver;
    
    /**
	 * Key: DateId
	 * Value: Info of the passenger for that date
	 */
	private final Map<Integer, VoNotifVDriverDateInfo> driverInfoMap;

	public VoNotifVDriver(final Driver driver) {
		this.driver = driver;
		this.driverInfoMap = new HashMap<>();
	}

	public VoNotifVDriver() {
		this.driver = new Driver();
		this.driverInfoMap = new HashMap<>();
	}

	public void addDriverInfo(final Integer dateId, final VoNotifVDriverDateInfo screenDriver) {
		this.driverInfoMap.put(dateId, screenDriver);
	}
}
