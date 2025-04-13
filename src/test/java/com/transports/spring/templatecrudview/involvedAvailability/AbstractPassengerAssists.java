package com.transports.spring.templatecrudview.involvedAvailability;

import com.microsoft.playwright.ElementHandle;
import com.transports.spring.TestConstants;

abstract class AbstractPassengerAssists extends TestConstants {

    protected static final String DRIVER_SELECT_SELECTOR = " select";
    protected static final String ASSISTANCE_ICON_SELECTOR = " i[class*=fa-calendar-check]";
    protected static final String ASSISTANCE_SPAN_SELECTOR = " span[id*=doesNotAssist]";
    protected static final String NEEDS_TRANSPORT_SPAN_SELECTOR = " span[id*=doesNotNeedTransport]";
    protected static final String NEEDS_TRANSPORT_ICON_DIV_SELECTOR = " div[id*=needsTransportIcon]";

    protected ElementHandle driverSelectElement;
    protected ElementHandle assistanceSpanElement;
    protected ElementHandle needsTransportSpanElement;
    protected ElementHandle needsTransportIconDivElement;
    protected ElementHandle assistanceIconElement;
    
    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is hidden.</li>
     *      <li>Assistance icon is unavailable</li>
     *      <li>Needs transport span is hidden</li>
     *      <li>Does not assist span is showing</li>
     *       <li>Does not assist span is showing</li>
     *  </ul>
     */
    protected boolean isEverythingOkWhenPassengerAssists() {
        final boolean isAssistanceIconStillShowingActiveColor = this.assistanceIconElement.getAttribute("class").contains("text-primary");
        final boolean isAssistanceIconStillIndicatingDelete = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("1");
        final boolean isAssistanceIconNotChanging = isAssistanceIconStillShowingActiveColor || isAssistanceIconStillIndicatingDelete;
        final boolean isDriverSelectNotHiding = !this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isTransportSpanNotHiding = !this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanNotShowing = this.assistanceSpanElement.getAttribute("class").contains("d-none");

        return isDriverSelectNotHiding || isTransportSpanNotHiding || isDoesNotAssistSpanNotShowing || isAssistanceIconNotChanging;
    }

    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is shown.</li>
     *      <li>Assistance icon is available</li>
     *      <li>Needs transport span is hidden</li>
     *      <li>Does not assist span is hidden</li>
     *  </ul>
     */
    protected boolean isEverythingOkWhenPassengerDoesNotAssist(final String passengerTdSelector) {
        final boolean isAssistanceIconShowingDisabledColor = this.assistanceIconElement.getAttribute("class").contains("text-muted");
        final boolean isAssistanceIconIndicatingCreateAssistance = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("0");
        final boolean isAssistanceIconNotOk = isAssistanceIconShowingDisabledColor || isAssistanceIconIndicatingCreateAssistance;
        final boolean isDriverSelectUnavailable = this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isTransportSpanShowing = !this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanShowing = !this.assistanceSpanElement.getAttribute("class").contains("d-none");
        final boolean isAssistanceIconHidden = this.needsTransportIconDivElement.getAttribute("class").contains("d-none");
        final boolean isPassengerEliminatedFromDriversTd = isPassengerEliminatedFromDriversTd(passengerTdSelector);

        return ( (isDriverSelectUnavailable || isTransportSpanShowing || isDoesNotAssistSpanShowing || isAssistanceIconHidden || isAssistanceIconNotOk) && isPassengerEliminatedFromDriversTd);
    }

    /**
     * Checks that the passenger's name is no longer among the driver's passengers for a date
     * @param passengerTdSelector
     * @return boolean
     */
    protected boolean isPassengerEliminatedFromDriversTd(final String passengerTdSelector){
        boolean isPassengerEliminatedFromDriversTd;
        final String selectedDriverIdForTransport = this.driverSelectElement.evaluate("select => select.options[select.selectedIndex].value").toString();

        if (selectedDriverIdForTransport != null && !selectedDriverIdForTransport.isBlank() && !selectedDriverIdForTransport.isEmpty()) {
            final ElementHandle passengerTd = this.page.querySelector(passengerTdSelector);
            final String passengerId = passengerTd.getAttribute("data-t");
            final String dateId = passengerTd.getAttribute("data-y");
            final ElementHandle passengerSpanInDriversTd = this.page.querySelector("#driverTransportsTable tbody tr td span[id*=" + dateId + "]");
            final String driversTdPassengerId = passengerSpanInDriversTd.getAttribute("data-p-span-id");
            isPassengerEliminatedFromDriversTd = driversTdPassengerId.equalsIgnoreCase(passengerId);
        } else {
            isPassengerEliminatedFromDriversTd = true;
        }

        return isPassengerEliminatedFromDriversTd;
    }
}
