import { test, expect } from '@playwright/test';
import { TestConstants } from '../../utils/testConstants';
import { TemplateCrudConstants } from '../../utils/templateCrudConstants';

test.describe('Indicate Passenger Does Not Assist When Has Transport', () => {
  test.beforeEach(async ({ page }) => {
    // TODO: change this url to:
    // set the settings for the transport dates
    // create a passenger and a driver with their availability
    // create a new template for a month
    await page.goto(`${TestConstants.APP_URL}/template/openTemplate?id=5`);
  });

  test('should indicate passenger does not assist when has transport', async ({ page }) => {
    const passengerTdSelector = `#${TemplateCrudConstants.PASSENGER_TABLE_ID} tbody tr:nth-child(1) td:nth-child(2)`;
    const assistanceIconSelector = `${passengerTdSelector} .assistance-icon`;
    const driverSelectSelector = `${passengerTdSelector} .driver-select`;
    const assistanceSpanSelector = `${passengerTdSelector} .assistance-span`;
    const needsTransportSpanSelector = `${passengerTdSelector} .needs-transport-span`;
    const needsTransportIconDivSelector = `${passengerTdSelector} .needs-transport-icon-div`;

    // Verify initial state
    const assistanceIcon = await page.locator(assistanceIconSelector);
    const driverSelect = await page.locator(driverSelectSelector);
    const assistanceSpan = await page.locator(assistanceSpanSelector);
    const needsTransportSpan = await page.locator(needsTransportSpanSelector);
    const needsTransportIconDiv = await page.locator(needsTransportIconDivSelector);

    // Verify passenger assists and needs transport
    await expect(assistanceIcon).toBeVisible();
    await expect(driverSelect).toBeVisible();
    await expect(assistanceSpan).toHaveText('Assists');
    await expect(needsTransportSpan).toHaveText('Needs Transport');
    await expect(needsTransportIconDiv).toBeVisible();

    // Mark passenger does not assist
    await assistanceIcon.click();

    // Verify state after marking does not assist
    await expect(assistanceSpan).toHaveText('Does Not Assist');
    await expect(driverSelect).toBeDisabled();
    await expect(needsTransportSpan).toHaveText('Does Not Need Transport');
    await expect(needsTransportIconDiv).not.toBeVisible();

    // Reset context
    await assistanceIcon.click();
    await driverSelect.selectOption({ index: 1 });
  });
}); 