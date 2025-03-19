package com.transports.spring.templatecrudview;

import com.microsoft.playwright.*;
import com.transports.spring.TestConstants;
import org.glassfish.jaxb.core.v2.TODO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AddDateModalTests {

    private Playwright playwright;
    private Browser browser;
    private Page page;

    @BeforeEach
    public void setUp() {
        this.playwright = Playwright.create();
        this.browser = this.playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        this.page = this.browser.newPage();

        /*
            TODO change this url to:
             set the settings for the transport dates
             create a passenger and a driver with their availabilty
             create a new template for a month
         */
        this.page.navigate(TestConstants.APP_URL + "/template/openTemplate?id=3");
        this.page.click("#showAddDateModal");
    }

    @AfterEach
    public void tearDown() {
        this.page.close();
        this.browser.close();
        this.playwright.close();
    }

    @Test
    void cannotSelectOtherMonthOrYearTest() {
        this.page.click("#addDateCardDateInput");
        final boolean isSelectPreviousMonthDisabled = this.page.querySelector(".datepicker-days .table-condensed thead tr:nth-child(2) .prev.disabled") != null;
        final boolean isSelectNextMonthDisabled = this.page.querySelector(".datepicker-days .table-condensed thead tr:nth-child(2) .next.disabled") != null;

        this.page.click(".datepicker-switch");
        final boolean isSelectPreviousYearDisabled = this.page.querySelector(".datepicker-months .table-condensed thead tr:nth-child(2) .prev.disabled") != null;
        final boolean isSelectNextYearDisabled = this.page.querySelector(".datepicker-months .table-condensed thead tr:nth-child(2) .next.disabled") != null;

        assertTrue(isSelectPreviousMonthDisabled && isSelectNextMonthDisabled && isSelectPreviousYearDisabled && isSelectNextYearDisabled);
    }

    /**
     * Gets the list of transport days in the month from the passenger table heading
     * and compares it with the td elements that contain the month days in the datepicker
     * to see if every of them is disabled.
     */
    @Test
    void transportDatesCannotBeSelectedTest() {
        final List<String> monthTransportDays = new ArrayList<>();
        //selector takes first column (days of the week) and ignores the first col, that is empty and has date 01-mm-yyyy
        final List<ElementHandle> passengerTableHeadingDateThElements = this.page.querySelectorAll("#passengerTransportsTable thead tr:nth-child(1) th:nth-child(n+2)");
        for (final ElementHandle tableHeadingElement : passengerTableHeadingDateThElements) {
            final String monthDate = tableHeadingElement.getAttribute("data-date");
            final String monthDay = monthDate.substring(8);
            monthTransportDays.add(monthDay);
        }

        this.page.click("#addDateCardDateInput");

        final List<ElementHandle> dateInputDayTdElements = this.page.querySelectorAll(".datepicker-days .table-condensed tbody tr td");
        for (final ElementHandle dateInputDayTdElement : dateInputDayTdElements) {
            for (final String monthTransportDay : monthTransportDays) {
                final String dayOfTheMonth = dateInputDayTdElement.textContent();
                if (dayOfTheMonth.equals(monthTransportDay)) {
                    final String classAttribute = dateInputDayTdElement.getAttribute("class");
                    if (!classAttribute.equals("old disabled day") && !classAttribute.equals("disabled disabled-date day")) {
                        fail();
                    }
                }
            }
        }
        assertTrue(true);
    }
}
