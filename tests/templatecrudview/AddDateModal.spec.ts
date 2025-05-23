import { test, expect } from '@playwright/test';

test.describe('Add Date Modal Tests', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the template page
    await page.goto('http://localhost:8080/template/openTemplate?id=3');
    await page.click('#showAddDateModal');
  });

  test('cannot select other month or year', async ({ page }) => {
    await page.click('#addDateCardDateInput');
    
    // Check if previous and next month buttons are disabled
    const isSelectPreviousMonthDisabled = await page.$('.datepicker-days .table-condensed thead tr:nth-child(2) .prev.disabled') !== null;
    const isSelectNextMonthDisabled = await page.$('.datepicker-days .table-condensed thead tr:nth-child(2) .next.disabled') !== null;

    // Click on month/year switch to show year selection
    await page.click('.datepicker-switch');
    
    // Check if previous and next year buttons are disabled
    const isSelectPreviousYearDisabled = await page.$('.datepicker-months .table-condensed thead tr:nth-child(2) .prev.disabled') !== null;
    const isSelectNextYearDisabled = await page.$('.datepicker-months .table-condensed thead tr:nth-child(2) .next.disabled') !== null;

    expect(isSelectPreviousMonthDisabled && isSelectNextMonthDisabled && 
           isSelectPreviousYearDisabled && isSelectNextYearDisabled).toBeTruthy();
  });

  test('transport dates cannot be selected', async ({ page }) => {
    // Get all transport days from the passenger table heading
    const monthTransportDays: string[] = [];
    const passengerTableHeadingDateThElements = await page.$$('#passengerTransportsTable thead tr:nth-child(2) th:nth-child(n+2)');
    
    for (const tableHeadingElement of passengerTableHeadingDateThElements) {
      const monthDate = await tableHeadingElement.getAttribute('data-date');
      if (monthDate) {
        const monthDay = monthDate.substring(8);
        monthTransportDays.push(monthDay);
      }
    }

    // Open date picker
    await page.click('#addDateCardDateInput');

    // Check each day in the date picker
    const dateInputDayTdElements = await page.$$('.datepicker-days .table-condensed tbody tr td');
    
    for (const dateInputDayTdElement of dateInputDayTdElements) {
      const dayOfTheMonth = await dateInputDayTdElement.textContent();
      if (dayOfTheMonth && monthTransportDays.includes(dayOfTheMonth)) {
        const classAttribute = await dateInputDayTdElement.getAttribute('class');
        if (classAttribute !== 'old disabled day' && classAttribute !== 'disabled disabled-date day') {
          throw new Error('Date should be disabled');
        }
      }
    }
  });
}); 