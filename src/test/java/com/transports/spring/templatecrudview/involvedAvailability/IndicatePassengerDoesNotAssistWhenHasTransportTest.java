package com.transports.spring.templatecrudview.involvedAvailability;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.SelectOption;
import com.transports.spring.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static com.transports.spring.templatecrudview.TemplateCrudConstants.PASSENGER_TABLE_ID;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class IndicatePassengerDoesNotAssistWhenHasTransportTest extends AbstractPassengerAssists {

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
     * It has to be a passenger 'td' in which it:
     * <ul>
     *     <li>Assists.</li>
     *     <li>Needs transport.</li>
     *     <li>Has a transport.</li>
     * </ul>
     */
    @Test
    void indicatePassengerDoesNotAssistWhenHasTransport() {
        final String passengerTdSelector = "#" + PASSENGER_TABLE_ID + " tbody tr:nth-child(1) td:nth-child(2)";
        final String assistanceIconSelector = passengerTdSelector + ASSISTANCE_ICON_SELECTOR;
        final String driverSelectSelector = passengerTdSelector + DRIVER_SELECT_SELECTOR;

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(driverSelectSelector);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        if (!super.isEverythingOkWhenPassengerAssistsAndNeedsTransport(passengerTdSelector)) {
            fail();
        }

        //mark passenger does not assist
        this.page.click(assistanceIconSelector);

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(driverSelectSelector);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        if (super.isEverythingOkWhenPassengerDoesNotAssist(passengerTdSelector)) {
            this.resetContextWhenHasTransport(assistanceIconSelector, driverSelectSelector);
            assertTrue(true);
        } else {
            this.resetContextWhenHasTransport(assistanceIconSelector, driverSelectSelector);
            fail();
        }
    }

    private void resetContextWhenHasTransport(final String assistanceIconSelector, final String driverSelectSelector) {
        this.page.click(assistanceIconSelector);
        this.page.selectOption(driverSelectSelector, new SelectOption().setIndex(1));
    }
}
