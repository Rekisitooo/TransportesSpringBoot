import { Page } from '@playwright/test';

export abstract class AbstractPassengerAssists {
    public static readonly DRIVER_SELECT_SELECTOR = ' select[name=driverInTransportSelect]';
    public static readonly DRIVER_OPTIONS_SELECTOR = AbstractPassengerAssists.DRIVER_SELECT_SELECTOR + ' option';
    public static readonly ASSISTANCE_ICON_SELECTOR = ' i[class*=fa-calendar-check]';
    public static readonly ASSISTANCE_SPAN_SELECTOR = ' span[id*=doesNotAssist]';
    public static readonly NEEDS_TRANSPORT_SPAN_SELECTOR = ' span[id*=doesNotNeedTransport]';
    public static readonly NEEDS_TRANSPORT_ICON_SELECTOR = ' i[class*=fa-car]';
    public static readonly NEEDS_TRANSPORT_ICON_DIV_SELECTOR = ' div[id*=needsTransportIcon]';
    public static readonly COMMUNICATION_ICON_SELECTOR = ' i[class*=fa-exclamation-circle]';

    protected page: Page;

    constructor(page: Page) {
        this.page = page;
    }

    /**
     * Checks that:
     *  <ul>
     *      <li>Driver's select is showing.</li>
     *      <li>Assistance icon is unavailable</li>
     *      <li>Needs transport span is hidden</li>
     *      <li>Does not assist span is showing</li>
     *  </ul>
     */
    public async isEverythingOkWhenPassengerAssistsAndNeedsTransport(passengerTdSelector: string): Promise<boolean> {
        const assistanceIconElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_ICON_SELECTOR);
        const driverSelectElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.DRIVER_SELECT_SELECTOR);
        const assistanceSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_SPAN_SELECTOR);
        const needsTransportSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.NEEDS_TRANSPORT_SPAN_SELECTOR);

        const isAssistanceIconShowingActiveColor = (await assistanceIconElement?.getAttribute('class'))?.includes('text-primary') ?? false;
        const isAssistanceIconIndicatingDelete = (await assistanceIconElement?.getAttribute('data-passenger-assist')) === '1';
        const isAssistanceIconOk = isAssistanceIconIndicatingDelete && isAssistanceIconShowingActiveColor;
        const isDriverSelectShowing = !(await driverSelectElement?.getAttribute('class') ?? '').includes('d-none');
        const isNeedsTransportSpanHidden = (await needsTransportSpanElement?.getAttribute('class') ?? '').includes('d-none');
        const isDoesNotAssistSpanHidden = (await assistanceSpanElement?.getAttribute('class') ?? '').includes('d-none');
        const isDriverOptionElementsActionCorrect = await this.isDriverOptionElementsActionCorrect(passengerTdSelector);

        return isDriverSelectShowing && isNeedsTransportSpanHidden && isDoesNotAssistSpanHidden && isAssistanceIconOk && isDriverOptionElementsActionCorrect;
    }

    /**
     * Checks that driver options perform the right action when selected
     */
    private async isDriverOptionElementsActionCorrect(passengerTdSelector: string): Promise<boolean> {
        const driverOptions = await this.page.$$(passengerTdSelector + AbstractPassengerAssists.DRIVER_OPTIONS_SELECTOR);

        for (let i = 0; i < driverOptions.length; i++) {
            const driverOption = driverOptions[i];
            const nameOptionAttr = await driverOption.getAttribute('name');
            const isDriverOptionNotIndicatingCreate = (i === 0 && nameOptionAttr?.toLowerCase() !== 'd');
            const isFirstOptionNotIndicatingDelete = i > 0 && 
                (nameOptionAttr?.toLowerCase() !== 'c' && nameOptionAttr?.toLowerCase() !== 'u');

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
    public async isEverythingOkWhenPassengerDoesNotAssist(passengerTdSelector: string): Promise<boolean> {
        const assistanceIconElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_ICON_SELECTOR);
        const driverSelectElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.DRIVER_SELECT_SELECTOR);
        const assistanceSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_SPAN_SELECTOR);
        const needsTransportSpanElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.NEEDS_TRANSPORT_SPAN_SELECTOR);
        const needsTransportIconDivElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.NEEDS_TRANSPORT_ICON_DIV_SELECTOR);

        const isAssistanceIconShowingDisabledColor = (await assistanceIconElement?.getAttribute('class'))?.includes('text-muted') ?? false;
        const isAssistanceIconIndicatingCreateAssistance = (await assistanceIconElement?.getAttribute('data-passenger-assist')) === '0';
        const isAssistanceIconOk = isAssistanceIconShowingDisabledColor && isAssistanceIconIndicatingCreateAssistance;
        const isDriverSelectHidden = (await driverSelectElement?.getAttribute('class') ?? '').includes('d-none');
        const isNeedsTransportSpanHidden = (await needsTransportSpanElement?.getAttribute('class') ?? '').includes('d-none');
        const isDoesNotAssistSpanShowing = !(await assistanceSpanElement?.getAttribute('class') ?? '').includes('d-none');
        const isNeedsTransportDivIconHidden = (await needsTransportIconDivElement?.getAttribute('class') ?? '').includes('d-none');
        const isPassengerEliminatedFromDriversTd = await this.isPassengerEliminatedFromDriversTd(passengerTdSelector);

        return isDriverSelectHidden && isNeedsTransportSpanHidden && isDoesNotAssistSpanShowing && 
               isNeedsTransportDivIconHidden && isAssistanceIconOk && isPassengerEliminatedFromDriversTd;
    }

    /**
     * Checks that the passenger's name is no longer among the driver's passengers for a date
     */
    protected async isPassengerEliminatedFromDriversTd(passengerTdSelector: string): Promise<boolean> {
        const driverSelectElement = await this.page.$(passengerTdSelector + AbstractPassengerAssists.DRIVER_SELECT_SELECTOR);
        const selectedDriverIdForTransport = await driverSelectElement?.evaluate('select => select.options[select.selectedIndex].value') as string;

        if (selectedDriverIdForTransport && selectedDriverIdForTransport.trim() !== '') {
            const passengerTd = await this.page.$(passengerTdSelector);
            const passengerId = await passengerTd?.getAttribute('data-t');
            const dateId = await passengerTd?.getAttribute('data-y');
            const passengerSpanInDriversTd = await this.page.$(`#driverTransportsTable tbody tr td span[id*=${dateId}]`);
            const driversTdPassengerId = await passengerSpanInDriversTd?.getAttribute('data-p-span-id');
            return driversTdPassengerId?.toLowerCase() === passengerId?.toLowerCase();
        }

        return true;
    }
} 