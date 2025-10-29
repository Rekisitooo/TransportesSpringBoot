package com.transports.spring.vo.notificationview.driver;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VoNotifVDriverDateInfo {
    private List<String> notifiedPassengerNameList;

    public VoNotifVDriverDateInfo() {
        this.notifiedPassengerNameList = new ArrayList<>();
    }

    private VoNotifVDriverDateInfo(final List<String> notifiedPassengerNameList) {
        this.notifiedPassengerNameList = new ArrayList<>();
    }

    public void addNotifiedPassengerName(final String passengerName) {
        this.notifiedPassengerNameList.add(passengerName);
    }
}
