package com.transports.spring.templatecrudview.involvedAvailability;

import com.microsoft.playwright.ElementHandle;
import com.transports.spring.TestConstants;

import java.util.List;

abstract class AbstractPassengerAssists extends TestConstants {

    protected static final String DRIVER_SELECT_SELECTOR = " select[name=driverInTransportSelect]";
    protected static final String DRIVER_OPTIONS_SELECTOR = DRIVER_SELECT_SELECTOR + " option";
    protected static final String ASSISTANCE_ICON_SELECTOR = " i[class*=fa-calendar-check]";
    protected static final String ASSISTANCE_SPAN_SELECTOR = " span[id*=doesNotAssist]";
    protected static final String NEEDS_TRANSPORT_SPAN_SELECTOR = " span[id*=doesNotNeedTransport]";
    protected static final String NEEDS_TRANSPORT_ICON_SELECTOR = " i[class*=fa-car]";
    protected static final String NEEDS_TRANSPORT_ICON_DIV_SELECTOR = " div[id*=needsTransportIcon]";

    protected ElementHandle driverSelectElement;
    protected ElementHandle assistanceSpanElement;
    protected ElementHandle needsTransportSpanElement;
    protected ElementHandle needsTransportIconDivElement;
    protected ElementHandle assistanceIconElement;

    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is showing.</li>
     *      <li>Assistance icon is unavailable</li>
     *      <li>Needs transport span is hidden</li>
     *      <li>Does not assist span is showing</li>
     *       <li>Does not assist span is showing</li>
     *  </ul>
     */
    protected boolean isEverythingOkWhenPassengerAssistsAndNeedsTransport(final String passengerTdSelector) {
        final boolean isAssistanceIconShowingActiveColor = this.assistanceIconElement.getAttribute("class").contains("text-primary");
        final boolean isAssistanceIconIndicatingDelete = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("1");
        final boolean isAssistanceIconOk = isAssistanceIconIndicatingDelete && isAssistanceIconShowingActiveColor;
        final boolean isDriverSelectShowing = !this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isNeedsTransportSpanHidden = this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanHidden = this.assistanceSpanElement.getAttribute("class").contains("d-none");
        final boolean isDriverOptionElementsActionCorrect = this.isDriverOptionElementsActionCorrect(passengerTdSelector);

        return isDriverSelectShowing && isNeedsTransportSpanHidden && isDoesNotAssistSpanHidden && isAssistanceIconOk && isDriverOptionElementsActionCorrect;
    }

    /**
     * Checks that driver options perform the right action when selected
     * @param passengerTdSelector
     * @return boolean
     */
    private boolean isDriverOptionElementsActionCorrect(final String passengerTdSelector) {
        final List<ElementHandle> driverOptions = this.page.querySelectorAll(passengerTdSelector + DRIVER_OPTIONS_SELECTOR);

        for (int i = 0; i < driverOptions.size(); i++) {
            final ElementHandle driverOption = driverOptions.get(i);
            final String nameOptionAttr = driverOption.getAttribute("name");
            final boolean isDriverOptionNotIndicatingCreate = (i == 0  && !nameOptionAttr.equalsIgnoreCase("d"));
            final boolean isFirstOptionNotIndicatingDelete = i > 0 &&
                    (!nameOptionAttr.equalsIgnoreCase("c") && !nameOptionAttr.equalsIgnoreCase("u") );

            if (isDriverOptionNotIndicatingCreate || isFirstOptionNotIndicatingDelete) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is hidden.</li>
     *      <li>Assistance icon is unavailable</li>
     *      <li>Needs transport span is hidden</li>
     *      <li>Does not assist span is shown</li>
     *  </ul>
     */
    protected boolean isEverythingOkWhenPassengerDoesNotAssist(final String passengerTdSelector) {
        final boolean isAssistanceIconShowingDisabledColor = this.assistanceIconElement.getAttribute("class").contains("text-muted");
        final boolean isAssistanceIconIndicatingCreateAssistance = this.assistanceIconElement.getAttribute("data-passenger-assist").equals("0");
        final boolean isAssistanceIconOk = isAssistanceIconShowingDisabledColor && isAssistanceIconIndicatingCreateAssistance;
        final boolean isDriverSelectHidden = this.driverSelectElement.getAttribute("class").contains("d-none");
        final boolean isNeedsTransportSpanHidden = this.needsTransportSpanElement.getAttribute("class").contains("d-none");
        final boolean isDoesNotAssistSpanShowing = !this.assistanceSpanElement.getAttribute("class").contains("d-none");
        final boolean isNeedsTransportDivIconHidden = this.needsTransportIconDivElement.getAttribute("class").contains("d-none");
        final boolean isPassengerEliminatedFromDriversTd = isPassengerEliminatedFromDriversTd(passengerTdSelector);

        return isDriverSelectHidden && isNeedsTransportSpanHidden && isDoesNotAssistSpanShowing && isNeedsTransportDivIconHidden && isAssistanceIconOk && isPassengerEliminatedFromDriversTd;
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
