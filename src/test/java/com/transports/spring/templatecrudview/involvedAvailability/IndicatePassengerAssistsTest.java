package com.transports.spring.templatecrudview.involvedAvailability;

import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.SelectOption;
import com.transports.spring.TestConstants;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
class IndicatePassengerAssistsTest extends TestConstants {

    private ElementHandle driverSelectElement;
    private ElementHandle assistanceSpanElement;
    private ElementHandle needsTransportSpanElement;
    private ElementHandle assistanceIconDivElement;
    private ElementHandle assistanceIconElement;

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

    @Test
    void test() {
        final String tdSelector = "#passengerTransportsTable tbody tr td";
        final String driverSelectSelector = tdSelector + " select";
        final String assistanceIconSelector = tdSelector + " i[class*=fa-calendar-check]";

        this.assistanceIconElement = this.page.querySelector(assistanceIconSelector);
        this.driverSelectElement = this.page.querySelector(driverSelectSelector);
        this.assistanceSpanElement = this.page.querySelector(tdSelector + " span[id*=doesNotAssist]");
        this.needsTransportSpanElement = this.page.querySelector(tdSelector + " span[id*=doesNotNeedTransport]");
        this.assistanceIconDivElement = this.page.querySelector(tdSelector + " div[id*=needsTransportIcon]");

        this.failIfTestContextIsNotOk();

        final SelectOption firstDriverOption = new SelectOption().setIndex(1);
        super.page.selectOption(driverSelectSelector, firstDriverOption);

        this.page.click(assistanceIconSelector);

        final boolean isAssistanceIconStillShowingActiveColor = this.assistanceIconElement.getAttribute("class").contains("text-primary");
        final boolean isAssistanceIconStillIndicatingCreate = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("1");
        final boolean isAssistanceIconNotChanging = isAssistanceIconStillShowingActiveColor || isAssistanceIconStillIndicatingCreate;
        final boolean isDriverSelectNotHidding = !this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isTransportSpanNotHidding = !this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanNotShowing = this.assistanceSpanElement.getAttribute("class").contains("d-none");

        if (isDriverSelectNotHidding || isTransportSpanNotHidding || isDoesNotAssistSpanNotShowing || isAssistanceIconNotChanging) {
            fail();
        } else {
            assertTrue(true);
        }

    }

    /**
     * Checks that drivers select is available, assistance icon is available and spans are not showing
     */
    private void failIfTestContextIsNotOk() {
        final boolean isAssistanceIconShowingDissabledColor = this.assistanceIconElement.getAttribute("class").contains("text-muted");
        final boolean isAssistanceIconIndicatingDeleteAssistance = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("0");
        final boolean isAssistanceIconNotOk = isAssistanceIconShowingDissabledColor || isAssistanceIconIndicatingDeleteAssistance;
        final boolean isDriverSelectUnavailable = this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isTransportSpanShowing = !this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanShowing = !this.assistanceSpanElement.getAttribute("class").contains("d-none");
        final boolean isAssistanceIconHidden = this.assistanceIconDivElement.getAttribute("class").contains("d-none");

        if (isDriverSelectUnavailable || isTransportSpanShowing || isDoesNotAssistSpanShowing || isAssistanceIconHidden || isAssistanceIconNotOk) {
            fail();
        }
    }
}
