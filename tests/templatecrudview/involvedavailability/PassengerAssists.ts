import { Page } from '@playwright/test';
import { AbstractPassengerAssists } from './AbstractPassengeAssists';

export class PassengerAssists extends AbstractPassengerAssists {
    constructor(page: Page) {
        super(page);
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
    public async isContextWhenNoTransportNeededOk(passengerTdSelector: string): Promise<boolean> {
        const assistanceIconElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_ICON_SELECTOR);
        const driverSelectElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.DRIVER_SELECT_SELECTOR);
        const assistanceSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_SPAN_SELECTOR);
        const needsTransportSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.NEEDS_TRANSPORT_SPAN_SELECTOR);

        const isAssistanceIconShowingActiveColor = (await assistanceIconElement?.getAttribute('class'))?.includes('text-primary') ?? false;
        const isAssistanceIconIndicatingDeleteAssistance = (await assistanceIconElement?.getAttribute('data-passenger-assist')) === '1';
        const isAssistanceIconOk = isAssistanceIconShowingActiveColor || isAssistanceIconIndicatingDeleteAssistance;
        const isDriverSelectHidden = (await driverSelectElement?.getAttribute('class') ?? '').includes('d-none');
        const isNeedsTransportSpanShowing = !(await needsTransportSpanElement?.getAttribute('class') ?? '').includes('d-none');
        const isDoesNotAssistSpanHidden = (await assistanceSpanElement?.getAttribute('class') ?? '').includes('d-none');

        return isAssistanceIconOk && isDriverSelectHidden && isNeedsTransportSpanShowing && isDoesNotAssistSpanHidden;
    }
} 