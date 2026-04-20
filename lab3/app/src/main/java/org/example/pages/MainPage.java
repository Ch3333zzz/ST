package org.example.pages;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.example.PropertyReader;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final DateTimeFormatter formatter =
    DateTimeFormatter.ofPattern("dd.MM.yyyy");

    private final CalendarComponent calendar = new CalendarComponent();
    private final LocationComponent lComponent = new LocationComponent();
    private final PassengerComponent pComponent = new PassengerComponent();
    private final NightsComponent nComponent = new NightsComponent();

    private final By searchButton = getByWithProperty("xpath.searchButton");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void selectDate(String date) {
        calendar.selectDate(date);
    }

    public void selectDates(String firstDate, String secondDate) {
        calendar.selectDates(firstDate, secondDate);
    }

    public void inputFirstDuration(String duration) {
        lComponent.inputFirstDuration(duration);
    }

    public void inputSecondDuration(String duration) {
        lComponent.inputSecondDuration(duration);
    }

    public void setAdults(int count) {
        pComponent.setAdults(count);
    }

    public void addChildren(String... ages) {
        pComponent.addChildren(ages);
    }

    public void setTravelClass(String travelClass) {
        pComponent.setTravelClass(travelClass);
    }

    public void setNightsRange(int from, int to) {
        nComponent.setNightsRange(from, to);
    }


    public void search() {
        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
    }

    private By getByWithProperty(String key) {
        return By.xpath(PropertyReader.getProperty(key));
    }

    private class CalendarComponent {
        private final By inputFirstDate =
            getByWithProperty("xpath.calendarFirstDate");
        private final By nextButton = getByWithProperty("xpath.calendarNextButton");
        private final By monthDiv = getByWithProperty("xpath.calendarMonthDiv");
        private final By selectButton =
            getByWithProperty("xpath.calendarSelectButton");

        private final String monthAttributeName = "data-month-date";
        private final DateTimeFormatter attributeFormatter =
            DateTimeFormatter.ofPattern("MM.yyyy");

        private void selectDate(String dateStr) {
            wait.until(ExpectedConditions.elementToBeClickable(inputFirstDate))
                .click();

            pickDate(LocalDate.parse(dateStr, formatter));

            wait.until(ExpectedConditions.elementToBeClickable(selectButton)).click();
        }

        private void selectDates(String startDateStr, String endDateStr) {
            LocalDate startDate = LocalDate.parse(startDateStr, formatter);
            LocalDate endDate = LocalDate.parse(endDateStr, formatter);

            wait.until(ExpectedConditions.elementToBeClickable(inputFirstDate))
                .click();

            pickDate(startDate);

            pickDate(endDate);

            wait.until(ExpectedConditions.elementToBeClickable(selectButton)).click();
        }

        private void pickDate(LocalDate targetDate) {
            String targetMonthValue = targetDate.format(attributeFormatter);
            int targetDay = targetDate.getDayOfMonth();

            WebElement currentMonth =
                wait.until(ExpectedConditions.presenceOfElementLocated(monthDiv));
            String currentMonthVal = currentMonth.getAttribute(monthAttributeName);

            while (!currentMonthVal.equals(targetMonthValue)) {
            wait.until(ExpectedConditions.elementToBeClickable(nextButton)).click();

            currentMonth =
                wait.until(ExpectedConditions.presenceOfElementLocated(monthDiv));
            currentMonthVal = currentMonth.getAttribute(monthAttributeName);
            }

            String dayXpath =
                "//div[@data-month-date='%s']//span[@data-ti='calendar-day-cell-info-container']//span[text()='%d']"
                    .formatted(targetMonthValue, targetDay);

            wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dayXpath)))
                .click();
        }
    }

    private class LocationComponent {
        private final By fromInput = getByWithProperty("xpath.locationFrom");
        private final By toInput = getByWithProperty("xpath.locationTo");

        private final String listCellXPath = "//div[@data-ti=\"dropdown-item\"]//div[@data-ti=\"cell\"]//span[text()='%s']";

        private void inputFirstDuration(String duration) {
            fillDuration(toInput, duration);
        }

        private void inputSecondDuration(String duration) {
            fillDuration(fromInput, duration);
        }

        private void fillDuration(By locator, String duration) {
            WebElement input = wait.until(ExpectedConditions.elementToBeClickable(locator));
            
            input.click();
            input.clear();
            input.sendKeys(duration);

            By xPath = By.xpath(listCellXPath.formatted(duration));
            wait.until(ExpectedConditions.elementToBeClickable(xPath)).click();

            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//div[@data-ti='dropdown-item']")));
        }
    }
    private class PassengerComponent {
        private final By menuTrigger = getByWithProperty("xpath.passengerMenu");
        private final By adultPlus = getByWithProperty("xpath.passengerAdultPlus");
        private final By adultMinus = getByWithProperty("xpath.passengerAdultMinus");
        private final By adultCounterValue = getByWithProperty("xpath.passengerAdultCounterValue");
        private final By addChildButton = getByWithProperty("xpath.passengerAddChildButton");

        private void setAdults(int targetCount) {
            ensureMenuOpened();
            
            WebElement counter = wait.until(ExpectedConditions.visibilityOfElementLocated(adultCounterValue));
            int current = Integer.parseInt(counter.getText());

            while (current < targetCount) {
                driver.findElement(adultPlus).click();
                current++;
            }

            while (current > targetCount && current > 1) {
                driver.findElement(adultMinus).click();
                current--;
            }
        }

        private void addChildren(String... ages) {
            ensureMenuOpened();

            for (String age : ages) {
                wait.until(ExpectedConditions.elementToBeClickable(addChildButton)).click();

                String xpath = "//div[@data-ti='suggest-container']//span[" +       //space issue
                            "normalize-space(translate(., '\u00a0', ' ')) = '%s'" +
                            "]";
                
                By childAgeLocator = By.xpath(xpath.formatted(age));

                wait.until(ExpectedConditions.elementToBeClickable(childAgeLocator)).click();
            }
        }

        private void setTravelClass(String travelClass) {
            ensureMenuOpened();
            By classLocator = By.xpath("//button[@data-ti='segment_control_button' and text()='%s']".formatted(travelClass));
            wait.until(ExpectedConditions.elementToBeClickable(classLocator)).click();
        }

        private void ensureMenuOpened() {
            if (driver.findElements(adultPlus).isEmpty()) {
                wait.until(ExpectedConditions.elementToBeClickable(menuTrigger)).click();
            }
        }
    }

    private class NightsComponent {
        private final By nightsTrigger = By.xpath("//div[@data-ti='nights_input-root']");
        private final By fromSlider = By.xpath("//input[@data-ti='fromInput']");
        private final By toSlider = By.xpath("//input[@data-ti='toInput']");

        public void setNightsRange(int from, int to) {
            wait.until(ExpectedConditions.elementToBeClickable(nightsTrigger)).click();

            WebElement fromInput = wait.until(ExpectedConditions.presenceOfElementLocated(fromSlider));
            WebElement toInput = wait.until(ExpectedConditions.presenceOfElementLocated(toSlider));

            setSliderValueJS(fromInput, from);
            setSliderValueJS(toInput, to);
        }

        private void setSliderValueJS(WebElement slider, int value) {
            String script = 
                "var input = arguments[0];" +
                "var value = arguments[1];" +
                "var setter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                "setter.call(input, value);" +
                "input.dispatchEvent(new Event('input', { bubbles: true }));" +
                "input.dispatchEvent(new Event('change', { bubbles: true }));";
            
            ((JavascriptExecutor) driver).executeScript(script, slider, value);
        }
    }
}