package com.transports.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
import java.util.List;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DtoAddNewDateForm {
    private String addDateCardEventNameInput;
    private Date addDateCardDateInput;
    private Boolean addDateCardIsTransportDateCheckboxInput;
    private List<String> addDateCardDriverAvailabilityCheck;
    private List<String> addDateCardPassengerAvailabilityCheck;
}
