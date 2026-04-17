package org.example.pages;

import org.example.PropertyReader;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;


public class MainPage {
    private final WebDriver driver;
    private final  WebDriverWait wait;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    //hotel
    private final By hotelButton = getByWithProperty("xpath.hotelButton");
    private final By hotelWhereField = getByWithProperty("xpath.hotelWhereField");
    private final By hotelWhenField = getByWithProperty("xpath.hotelWhenField");
    private final By caledarNextBottun = getByWithProperty("xpath.hotelNextMonth");
    private final By hotelChooseDate = getByWithProperty("xpath.hotelChooseDate");
    private final By hotelSelectGuests = getByWithProperty("xpath.hotelSelectGuests");
    private final By hotelDecreaseGuests = getByWithProperty("xpath.hotelDecreaseGuests");
    private final By hotelIncreaseGuests = getByWithProperty("xpath.hotelIncreaseGuests");
    private final By addChildButton = getByWithProperty("xpath.addChildButton");
    private final By searchHotelButton = getByWithProperty("xpath.searchHotelButton");

    //airplaines
    private final By airplanesButton = getByWithProperty("xpath.airplanesButton");
    private final By airplanesFrom = getByWithProperty("xpath.airplanesFrom");
    private final By airplanesTo = getByWithProperty("xpath.airplanesTo");
    private final By airplaintChooseWhenTo = getByWithProperty("xpath.airplaintChooseWhenTo");
    private final By airplanesPassagers = getByWithProperty("xpath.airplanesPassagers");
    private final By searchAirplanes = getByWithProperty("xpath.searchAirplanes");

    public MainPage(WebDriver driver) {
        this.driver = driver;

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void SelectCityFromList(int i) {
        if (i <= 0) throw new IllegalArgumentException("Count cities starting from one!");

        wait.until(ExpectedConditions.elementToBeClickable(hotelButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(hotelWhereField)).click();

        String arg = "(//div[@data-ti='dropdown-item'])[%d]".formatted(i);
        By selectedCity = By.xpath(arg);

        wait.until(ExpectedConditions.visibilityOfElementLocated(selectedCity)).click();
    }

    public void SearchCityByName(String city) {
        if (city == null)
            throw new IllegalArgumentException("city can't be null!");

        wait.until(ExpectedConditions.elementToBeClickable(hotelButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(hotelWhereField)).click();

        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(hotelWhereField));

        inputField.clear();
        inputField.sendKeys(city + Keys.ENTER);
    }

    public void SelectDates(String FirstDate, String SecondDate) {
        if (FirstDate == null || SecondDate == null)
            throw new IllegalArgumentException("Dates cannot be null");

        LocalDate dateFrom = LocalDate.parse(FirstDate, formatter);
        LocalDate dateTo = LocalDate.parse(SecondDate, formatter);

        long daysBetween = ChronoUnit.DAYS.between(dateFrom, dateTo);

        if (daysBetween > 30)
            throw new IllegalArgumentException("Difference between days can't be more than 30");
        
        if (dateFrom.isAfter(dateTo))
            throw new IllegalArgumentException("First date must be earlier than second");
        if (dateFrom.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("First date cannot be in the past");

        wait.until(ExpectedConditions.elementToBeClickable(hotelButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(hotelWhenField)).click();

        String monthContainerXpath = "//div[@data-month-date='%s']";
        String dayXpathTemplate = "//div[@data-month-date='%s']//span[text()='%d']";

        String fromMonthValue = FirstDate.substring(3); // "MM.yyyy"
        
        while (driver.findElements(By.xpath(monthContainerXpath.formatted(fromMonthValue))).isEmpty()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(caledarNextBottun));
            nextBtn.click();
            
            wait.until(ExpectedConditions.stalenessOf(nextBtn)); 
        }

        By buttonFromDay = By.xpath(dayXpathTemplate.formatted(fromMonthValue, dateFrom.getDayOfMonth()));
        wait.until(ExpectedConditions.elementToBeClickable(buttonFromDay)).click();

        String toMonthValue = SecondDate.substring(3); // "MM.yyyy"

        while (driver.findElements(By.xpath(monthContainerXpath.formatted(toMonthValue))).isEmpty()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(caledarNextBottun));
            nextBtn.click();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(caledarNextBottun));
        }

        By buttonToDay = By.xpath(dayXpathTemplate.formatted(toMonthValue, dateTo.getDayOfMonth()));
        wait.until(ExpectedConditions.elementToBeClickable(buttonToDay)).click();

        wait.until(ExpectedConditions.elementToBeClickable(hotelChooseDate)).click();
    }

    public void SelectGuests(short adult, float[] kids) {
        if (adult < 1 || adult > 6)
            throw new IllegalArgumentException("Adults count must be between 1 and 6");
        if (kids != null && kids.length > 4)
            throw new IllegalArgumentException("Kids count can't be more than 4");

        for (float kidAge : kids) {
            if (kidAge > 18 || kidAge < 0)
                throw new IllegalArgumentException("Age of kids must be lower, than 18 and positive");
        }
        wait.until(ExpectedConditions.elementToBeClickable(hotelButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(hotelSelectGuests)).click();


        By adultCountLocator = By.xpath("//div[@data-ti='count']"); 

        while (true) {
            String currentText = wait.until(ExpectedConditions.visibilityOfElementLocated(adultCountLocator)).getText();
            int currentAdults = Integer.parseInt(currentText);

            if (currentAdults == adult) {
                break;
            } 
            
            if (currentAdults < adult) {
                driver.findElement(hotelIncreaseGuests).click();
            } else {
                driver.findElement(hotelDecreaseGuests).click();
            }
            
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(adultCountLocator, currentText)));
        }

        if (kids != null && kids.length > 0) {
            for (float kidAge : kids) {
                wait.until(ExpectedConditions.elementToBeClickable(addChildButton)).click();
                
            int roundedAge = (int) kidAge;
            String suffix = (roundedAge == 1) ? "год" : (roundedAge >= 2 && roundedAge <= 4) ? "года" : "лет";

            String xpath;
            if (kidAge < 1) {
                xpath = "//div[@data-ti='dropdown-item']//span[contains(., 'Меньше')]";
            } else {
                xpath = "//div[@data-ti='dropdown-item']//span[contains(., '%d') and contains(., '%s')]".formatted(roundedAge, suffix);
            }

            By ageOption = By.xpath(xpath);
            wait.until(ExpectedConditions.elementToBeClickable(ageOption)).click();
            }
        }
    }

    public void searchHotel(int i, String dateFrom, String dateTo, short adult, float[] kids) {
        SelectCityFromList(i);
        SelectDates(dateTo, dateTo);
        SelectGuests(adult, kids);

        wait.until(ExpectedConditions.elementToBeClickable(searchHotelButton)).click();
    }

    public void searchHotel(String city, String dateFrom, String dateTo, short adult, float[] kids) {
        SearchCityByName(city);
        SelectDates(dateTo, dateTo);
        SelectGuests(adult, kids);

        wait.until(ExpectedConditions.elementToBeClickable(searchHotelButton)).click();
    }

    public void selectFromAirplanes(int i) {
        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(airplanesFrom)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesFrom)).click();

        String arg = "(//div[@data-ti='dropdown-item'])[%d]".formatted(i);
        By selectedFrom = By.xpath(arg);

        wait.until(ExpectedConditions.visibilityOfElementLocated(selectedFrom)).click();
    }

    public void searchFromAirplanesByName(String city) {
        if (city == null)
            throw new IllegalArgumentException("city can't be null!");

        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesFrom)).click();

        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(airplanesFrom));

        inputField.clear();
        inputField.sendKeys(city + Keys.ENTER);
    }


    public void selectToAirplanes(int i) {
        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();

        wait.until(ExpectedConditions.elementToBeClickable(airplanesTo)).clear();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesTo)).click();

        String arg = "(//div[@data-ti='dropdown-item'])[%d]".formatted(i);
        By selectedFrom = By.xpath(arg);

        wait.until(ExpectedConditions.visibilityOfElementLocated(selectedFrom)).click();
    }

    public void searchToAirplanesByName(String city) {
        if (city == null)
            throw new IllegalArgumentException("city can't be null!");

        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesTo)).click();

        WebElement inputField = wait.until(ExpectedConditions.elementToBeClickable(airplanesTo));

        inputField.clear();
        inputField.sendKeys(city + Keys.ENTER);
    }

    public void selectDatesAirplain(String FirstDate, String SecondDate) {
        if (FirstDate == null)
            throw new IllegalArgumentException("First date cannot be null");

        LocalDate dateFrom = LocalDate.parse(FirstDate, formatter);
        if (dateFrom.isBefore(LocalDate.now()))
            throw new IllegalArgumentException("First date cannot be in the past");

        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(airplaintChooseWhenTo)).click();

        String monthContainerXpath = "//div[@data-month-date='%s']";
        String dayXpathTemplate = "//div[@data-month-date='%s']//span[text()='%d']";

        String fromMonthValue = FirstDate.substring(3); // "MM.yyyy"

        By submitButton = By.xpath("//button[@data-ti='calendar-popper-footer-select-button']");

        
        while (driver.findElements(By.xpath(monthContainerXpath.formatted(fromMonthValue))).isEmpty()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(caledarNextBottun));
            nextBtn.click();
            
            wait.until(ExpectedConditions.stalenessOf(nextBtn)); 
        }

        By buttonFromDay = By.xpath(dayXpathTemplate.formatted(fromMonthValue, dateFrom.getDayOfMonth()));
        wait.until(ExpectedConditions.elementToBeClickable(buttonFromDay)).click();

        if (SecondDate == null) {
            wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
            return;
        }

        LocalDate dateTo = LocalDate.parse(SecondDate, formatter);
        
        if (dateFrom.isAfter(dateTo))
            throw new IllegalArgumentException("First date must be earlier than second");


        String toMonthValue = SecondDate.substring(3); // "MM.yyyy"

        while (driver.findElements(By.xpath(monthContainerXpath.formatted(toMonthValue))).isEmpty()) {
            WebElement nextBtn = wait.until(ExpectedConditions.elementToBeClickable(caledarNextBottun));
            nextBtn.click();
            
            wait.until(ExpectedConditions.presenceOfElementLocated(caledarNextBottun));
        }

        By buttonToDay = By.xpath(dayXpathTemplate.formatted(toMonthValue, dateTo.getDayOfMonth()));
        wait.until(ExpectedConditions.elementToBeClickable(buttonToDay)).click();

        wait.until(ExpectedConditions.elementToBeClickable(submitButton)).click();
    }

    public void selectPassagersForAirplain(short adult, float[] kids) {
        if (adult + kids.length > 9)
            throw new IllegalArgumentException("Max people is 9");

        int babyCount = 0;
        for (float kidAge : kids) {
            if (kidAge > 12 || kidAge < 0)
                throw new IllegalArgumentException("Age of kids must be lower, than 18 and positive");
            if (kidAge < 2)
                babyCount++;
        }
        if (babyCount > adult)
            throw new IllegalArgumentException("Baby count can't be bigger, than adult count");

        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesPassagers)).click();


        By adultCountLocator = By.xpath("//div[@data-ti='count']"); 

        while (true) {
            String currentText = wait.until(ExpectedConditions.visibilityOfElementLocated(adultCountLocator)).getText();
            int currentAdults = Integer.parseInt(currentText);

            if (currentAdults == adult) {
                break;
            } 
            
            if (currentAdults < adult) {
                driver.findElement(hotelIncreaseGuests).click();
            } else {
                driver.findElement(hotelDecreaseGuests).click();
            }
            
            wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(adultCountLocator, currentText)));
        }

        if (kids != null && kids.length > 0) {
            for (float kidAge : kids) {
                wait.until(ExpectedConditions.elementToBeClickable(addChildButton)).click();
                
            int roundedAge = (int) kidAge;
            String suffix = (roundedAge == 1) ? "год" : (roundedAge >= 2 && roundedAge <= 4) ? "года" : "лет";

            String xpath;
            if (kidAge < 1) {
                xpath = "//div[@data-ti='dropdown-item']//span[contains(., 'Меньше')]";
            } else {
                xpath = "//div[@data-ti='dropdown-item']//span[contains(., '%d') and contains(., '%s')]".formatted(roundedAge, suffix);
            }

            By ageOption = By.xpath(xpath);
            wait.until(ExpectedConditions.elementToBeClickable(ageOption)).click();
            }
        }
    }

    public void selectBuisnessClass() {

        wait.until(ExpectedConditions.elementToBeClickable(airplanesButton)).click();
        wait.until(ExpectedConditions.elementToBeClickable(airplanesPassagers)).click();

        By buisness = By.xpath("(//button[@data-ti='segment_control_button'])[2]");

        wait.until(ExpectedConditions.elementToBeClickable(buisness)).click();

    }

    public void searchAirplaneDefault(int i, int j, String firstDate, String secondDate, short adult, float[] kids) {
        selectFromAirplanes(i);
        selectToAirplanes(j);
        selectDatesAirplain(firstDate, secondDate);
        selectPassagersForAirplain(adult, kids);

        wait.until(ExpectedConditions.elementToBeClickable(searchAirplanes)).click();
    }

    public void searchAirplaneBuisness(String from, String to, String firstDate, String secondDate, short adult, float[] kids) {
        searchFromAirplanesByName(from);
        searchToAirplanesByName(to);
        selectDatesAirplain(firstDate, secondDate);
        selectPassagersForAirplain(adult, kids);
        selectBuisnessClass();

        wait.until(ExpectedConditions.elementToBeClickable(searchAirplanes)).click();
    }


    private By getByWithProperty(String key) {
        return By.xpath(PropertyReader.getProperty(key));
    }

    private class Calendar {
        private By inputFirstDate;
    }
}
