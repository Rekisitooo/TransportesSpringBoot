import { test, expect } from '@playwright/test';
import { AbstractPassengerAssists } from '../../involvedavailability/AbstractPassengeAssists';
import { TemplateCrudConstants } from '../../../utils/templateCrudConstants';

test.describe('Indicate Driver Knows His Transport In Date', () => {

    test.beforeEach(async ({ page }) => {
        // Navigate to the template page
        await page.goto('http://localhost:8080/template/openTemplate?id=5');
    });

    test('indicate driver knows does not pick up any passenger in date', async ({ page }) => {
        //second driver first date
        const driverTdSelector = `#${TemplateCrudConstants.DRIVER_TABLE_ID} tbody tr:nth-child(2) td:nth-child(1)`;
        const communicationIconSelector = driverTdSelector + AbstractPassengerAssists.COMMUNICATION_ICON_SELECTOR;
        const communicationIconElement = await page.$(communicationIconSelector);

        // Verify initial state
        const isInitialStateOk = !(await communicationIconElement?.getAttribute('class') ?? '').includes('d-none');
        expect(isInitialStateOk).toBeTruthy();
    
        // Click on the communication icon
        await communicationIconElement?.click();

        // Verify final state
        const isFinalStateOk = (await communicationIconElement?.getAttribute('class') ?? '').includes('d-none');
        expect(isFinalStateOk).toBeTruthy();
        //TODO CHECK if the communication icon is saved
        //TODO reset the communication icon
    });

    test('indicate driver knows transport with one passenger in date', async ({ page }) => {
        //second driver second date
        const driverTdSelector = `#${TemplateCrudConstants.DRIVER_TABLE_ID} tbody tr:nth-child(2) td:nth-child(2)`;
        const communicationIconSelector = driverTdSelector + AbstractPassengerAssists.COMMUNICATION_ICON_SELECTOR;
        const communicationIconElement = await page.$(communicationIconSelector);

        // Verify initial state
        const isInitialStateOk = !(await communicationIconElement?.getAttribute('class') ?? '').includes('d-none');
        expect(isInitialStateOk).toBeTruthy();
    
        // Click on the communication icon
        await communicationIconElement?.click();

        // Verify final state
        const isFinalStateOk = (await communicationIconElement?.getAttribute('class') ?? '').includes('d-none');
        expect(isFinalStateOk).toBeTruthy();
        //TODO CHECK if the communication icon is saved
        //TODO reset the communication icon and add Julio as a passenger
    });
    
});