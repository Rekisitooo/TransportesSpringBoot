package com.transports.spring.model.date;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Class used to convert LocalDate day of the week, month... to the data needed in the db.
 */
public class LocalDateConverter {

    private LocalDateConverter(){}

    public static int convertLocalDateDayOfWeekToDbDayOfWeek(final LocalDate localDate){
        final DayOfWeek dayOfWeek = localDate.getDayOfWeek();
        int dayOfWeekValue = dayOfWeek.getValue() + 1;

        if (dayOfWeekValue == 8) {
            dayOfWeekValue = 1;//saturday
        }

        return dayOfWeekValue;
    }
}
