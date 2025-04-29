package com.transports.spring.templatecrudview.involvedAvailability;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.transports.spring.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.transports.spring.templatecrudview.TemplateCrudConstants.PASSENGER_TABLE_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class IndicatePassengerAssistsTest extends AbstractPassengerAssists {

    @BeforeEach
    public void setUp() {
        super.playwright = Playwright.create();
        super.browser = this.playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        super.page = this.browser.newPage();

        /*
            TODO change this url to:
             set the settings for the transport dates
             create a passenger and a driver with their availabilty
             create a new template for a month
         */
        super.page.navigate(TestConstants.APP_URL + "/template/openTemplate?id=5");
    }

    @AfterEach
    public void tearDown() {
        super.page.close();
        super.browser.close();
        super.playwright.close();
    }

    /**
     * Context: It has to be a <td> with in which the passenger does not assist.
     */
    @Test
    void indicatePassengerAssists() {
        final String passengerTdSelector = "#" + PASSENGER_TABLE_ID + " tbody tr:nth-child(1) td:nth-child(3)";
        final String assistanceIconSelector = passengerTdSelector + ASSISTANCE_ICON_SELECTOR;

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(passengerTdSelector + DRIVER_SELECT_SELECTOR);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        if (!super.isEverythingOkWhenPassengerDoesNotAssist(passengerTdSelector)) {
            fail();
        }

        //mark passenger assists
        this.page.click(assistanceIconSelector);

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(passengerTdSelector + DRIVER_SELECT_SELECTOR);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        if (super.isEverythingOkWhenPassengerAssistsAndNeedsTransport(passengerTdSelector)) {
            this.resetContext(assistanceIconSelector);
            assertTrue(true);
        } else {
            this.resetContext(assistanceIconSelector);
            fail();
        }
    }

    private void resetContext(final String assistanceIconSelector) {
        this.page.click(assistanceIconSelector);
    }
}
