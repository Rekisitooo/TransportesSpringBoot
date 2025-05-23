import { test, expect } from '@playwright/test';
import { AbstractPassengerAssists } from './AbstractPassengeAssists';
import { PassengerAssists } from './PassengerAssists';

const PASSENGER_TABLE_ID = 'passengerTransportsTable';

test.describe('Indicate Passenger Does Not Assist When No Transport Needed', () => {
  
  test.beforeEach(async ({ page }) => {
    // Navigate to the template page
    await page.goto('http://localhost:8080/template/openTemplate?id=5');
  });

  test('indicate passenger does not assist when no transport needed', async ({ page }) => {
    const passengerAssists = new PassengerAssists(page);
    const passengerTdSelector = `#${PASSENGER_TABLE_ID} tbody tr:nth-child(1) td:nth-child(4)`;
    const assistanceIconSelector = passengerTdSelector + AbstractPassengerAssists.ASSISTANCE_ICON_SELECTOR;
    const needsTransportIconSelector = passengerTdSelector + AbstractPassengerAssists.NEEDS_TRANSPORT_ICON_SELECTOR;

    // Verify initial state
    const isInitialStateOk = await passengerAssists.isContextWhenNoTransportNeededOk(passengerTdSelector);
    expect(isInitialStateOk).toBeTruthy();

    // Mark passenger does not assist
    await page.click(assistanceIconSelector);

    // Verify final state
    const isFinalStateOk = await passengerAssists.isEverythingOkWhenPassengerDoesNotAssist(passengerTdSelector);
    expect(isFinalStateOk).toBeTruthy();

    // Reset context
    await page.click(assistanceIconSelector);
    await page.click(needsTransportIconSelector);
  });
}); 