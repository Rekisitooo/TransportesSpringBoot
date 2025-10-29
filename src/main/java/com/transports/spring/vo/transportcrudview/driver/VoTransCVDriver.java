package com.transports.spring.vo.transportcrudview.driver;

import java.util.HashMap;
import java.util.Map;

import com.transports.spring.model.Driver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VoTransCVDriver {
	
	/**
	 * Key: DateId
	 * Value: Info of the driver for that date
	 */
	private final Map<Integer, VoTransCVDriverDateInfo> driverInfoMap;
    private Driver driver;
	private VoTransCVGeneralDriverIcon voScreenGeneralDriverIcon;

	/**
	 * Constructor for VoTransportCrudScreenDriver
	 */
	public VoTransCVDriver(final VoTransCVGeneralDriverIcon voScreenGeneralDriverIcon, final Driver driver) {
		this.voScreenGeneralDriverIcon = voScreenGeneralDriverIcon;
		this.driver = driver;
		this.driverInfoMap = new HashMap<>();
	}

	public void addDriverInfo(final Integer dateId, final VoTransCVDriverDateInfo screenDriver) {
		this.driverInfoMap.put(dateId, screenDriver);
	}

}
