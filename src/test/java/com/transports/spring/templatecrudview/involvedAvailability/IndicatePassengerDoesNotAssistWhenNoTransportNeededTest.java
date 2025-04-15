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
class IndicatePassengerDoesNotAssistWhenNoTransportNeededTest extends AbstractPassengerAssists {

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
     *     <li>Does not need transport.</li>
     * </ul>
     */
    @Test
    void indicatePassengerDoesNotAssistWhenNoTransportNeeded() {
        final String passengerTdSelector = "#" + PASSENGER_TABLE_ID + " tbody tr:nth-child(1) td:nth-child(4)";
        final String assistanceIconSelector = passengerTdSelector + ASSISTANCE_ICON_SELECTOR;

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(passengerTdSelector + DRIVER_SELECT_SELECTOR);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        if (!isContextWhenNoTransportNeededOk()) {
            fail();
        }

        //mark passenger does not assist
        this.page.click(assistanceIconSelector);

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(passengerTdSelector + DRIVER_SELECT_SELECTOR);
        this.assistanceSpanElement = this.page.querySelector(passengerTdSelector + ASSISTANCE_SPAN_SELECTOR);
        this.needsTransportSpanElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_SPAN_SELECTOR);
        this.needsTransportIconDivElement = this.page.querySelector(passengerTdSelector + NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        final String needsTransportIconSelector = passengerTdSelector + NEEDS_TRANSPORT_ICON_SELECTOR;
        if (super.isEverythingOkWhenPassengerDoesNotAssist(passengerTdSelector)) {
            this.resetContextWhenNoTransportNeeded(assistanceIconSelector, needsTransportIconSelector);
            assertTrue(true);
        } else {
            this.resetContextWhenNoTransportNeeded(assistanceIconSelector, needsTransportIconSelector);
            fail();
        }
    }

    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is hidden.</li>
     *      <li>Assistance icon is available</li>
     *      <li>Needs transport span is showing</li>
     *      <li>Does not assist span is hidden</li>
     *  </ul>
     */
    private boolean isContextWhenNoTransportNeededOk() {
        final boolean isAssistanceIconShowingActiveColor = this.assistanceIconElement.getAttribute("class").contains("text-primary");
        final boolean isAssistanceIconIndicatingDeleteAssistance = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("1");
        final boolean isAssistanceIconOk = isAssistanceIconShowingActiveColor || isAssistanceIconIndicatingDeleteAssistance;
        final boolean isDriverSelectHidden = this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isNeedsTransportSpanShowing = !this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanHidden = this.assistanceSpanElement.getAttribute("class").contains("d-none");

        return isAssistanceIconOk && isDriverSelectHidden && isNeedsTransportSpanShowing && isDoesNotAssistSpanHidden;
    }

    private void resetContextWhenNoTransportNeeded(final String assistanceIconSelector, final String needsTransportIconSelector) {
        this.page.click(assistanceIconSelector);
        this.page.click(needsTransportIconSelector);
    }
}
