package com.transports.spring.model.date;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LocalDateConverterTest {

    @Test
    void saturdayLocalDateValueIsConvertedToDbValueTest() {
        final LocalDate localDate = LocalDate.of(2025, 3, 30);
        int dbDayOfWeek = LocalDateConverter.convertLocalDateDayOfWeekToDbDayOfWeek(localDate);

        assertEquals(dbDayOfWeek, 1);
    }
}