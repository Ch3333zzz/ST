package org.example.pages.searchPages;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.example.pages.searchPages.records.AccommodationType;
import org.example.pages.searchPages.records.HotelResult;
import org.example.specificPage.HotelPage;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HotelSearchPage extends SearchPage {

    public HotelSearchPage(WebDriver driver) {
        super(driver);
    }

    public void selectPriceRange(int priceFrom, int priceTo) {
        By priceButton = By.xpath("//div[@data-ti=\"filters\"]//span[text() = 'Цена за ночь']");

        String inputXPath = "(//div[@data-ti=\"price\"]//input[@data-ti=\"input\"])[%d]";

        fillPriceInput(priceButton, By.xpath(inputXPath.formatted(1)), priceFrom);

        fillPriceInput(priceButton, By.xpath(inputXPath.formatted(2)), priceTo);
    }

    private void fillPriceInput(By openButton, By inputLocator, int value) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                if (inputLocator != null && (driver.findElements(inputLocator).isEmpty()
                        || !driver.findElement(inputLocator).isDisplayed())) {
                    wait.until(ExpectedConditions.elementToBeClickable(openButton)).click();
                }

                WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(inputLocator));

                input.sendKeys(Keys.chord(Keys.CONTROL, "a"),
                        Keys.BACK_SPACE);

                input.sendKeys(String.valueOf(value));

                return;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
    }

    public List<HotelResult> getResults(int count) {

        By cardLocator = By.xpath("//div[@data-ti='offer-card']");

        wait.until(ExpectedConditions.urlContains("hotel.tutu.ru"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
                By.xpath("//div[@data-ti='offer-card']//div[@data-ti='hotel-name']//span")));
        wait.until(ExpectedConditions.visibilityOfElementLocated(cardLocator));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(cardLocator));

        List<WebElement> allCards = driver.findElements(cardLocator);
        List<HotelResult> results = new ArrayList<>();

        int limit = Math.min(count, allCards.size());

        for (int i = 0; i < limit; i++) {
            WebElement card = allCards.get(i);

            String name = card.findElement(By.xpath(".//div[@data-ti='hotel-name']//span")).getText();
            String city = card.findElement(By.xpath("(.//div[@itemprop='address']//span)[1]")).getText();

            String infoText = card.findElement(By.xpath(".//div[@data-ti='price-for-guest']//p[@data-ti='p']"))
                    .getText();
            int nights = parseNumber(infoText, "ноч");
            int guests = parseNumber(infoText, "гост");

            String priceRaw = card.findElement(By.xpath(".//span[@data-ti='price']")).getText();
            int price = Integer.parseInt(priceRaw.replaceAll("[^0-9]", ""));

            AccommodationType accommodationType = AccommodationType.APARTMENT;
            List<WebElement> typeElements = card.findElements(By.xpath(".//span[@data-ti='label-value-label']//span"));
            if (!typeElements.isEmpty()) {
                String typeText = typeElements.get(0).getText();
                if (typeText.equalsIgnoreCase("Отель")) {
                    accommodationType = AccommodationType.HOTEL;
                }
            }

            boolean hasFreeCancellation = !card
                    .findElements(By.xpath(".//ul[@data-ti='hotels-params']//span[contains(text(), 'отмена')]"))
                    .isEmpty();

            boolean isBreakfastIncluded = !card
                    .findElements(By.xpath(
                            ".//ul[@data-ti='hotels-params']//span[contains(translate(text(), 'З', 'з'), 'завтрак')]"))
                    .isEmpty();

            int stars = 0;
            List<WebElement> starIcons = card.findElements(By.xpath(".//i[contains(@class, 'oim-star')]"));
            String fullTypeText = card.getText();

            if (!starIcons.isEmpty()) {
                stars = starIcons.size();
            } else {
                stars = parseNumber(fullTypeText, "★");
            }

            results.add(new HotelResult(
                    name,
                    accommodationType,
                    city,
                    nights,
                    guests,
                    hasFreeCancellation,
                    isBreakfastIncluded,
                    stars,
                    price));
        }

        return results;
    }

    private int parseNumber(String text, String keyword) {
        Pattern pattern = Pattern.compile("(\\d+)\\s+" + keyword);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? Integer.parseInt(matcher.group(1)) : 0;
    }

    public HotelPage selectHotel(String hotelOrApartmentName) {
        String hotelXPath = "//div[@data-ti='hotel-name']//span[contains(text(),'%s')]".formatted(hotelOrApartmentName);
        WebElement hotelLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(hotelXPath)));

        int initialWindowsCount = driver.getWindowHandles().size();

        hotelLink.click();

        wait.until(d -> d.getWindowHandles().size() > initialWindowsCount);

        for (String windowHandle : driver.getWindowHandles()) {
            driver.switchTo().window(windowHandle);
        }

        return new HotelPage(driver);
    }

    @Override
    protected By getSortElement() {
        String selectSortXPath = "//div[@data-ti=\"list-header\"]//span[@data-ti=\"rich-icon\"]";

        return By.xpath(selectSortXPath);
    }

    @Override
    protected By getSortedByElement(String option) {
        String optionXPath = "//div[@data-ti=\"dropdown\"]//div[@data-ti=\"dropdown-item\" or @data-ti=\"dropdown-item-selected\"]//span[text() = '%s']"
                .formatted(option);

        return By.xpath(optionXPath);
    }

    @Override
    protected By getOfferElement(String offerName) {
        String offerNameXPath = "//div[@data-ti=\"offer-card\"]//div[@data-ti=\"hotel-name\"]//span[text() = '%s']"
                .formatted(offerName);

        return By.xpath(offerNameXPath);
    }

    @Override
    protected By getFilterElement(String filterName) {
        String filterNameXPath = "//div[@data-ti=\"chip-row\"]//span[text() = '%s']".formatted(filterName);

        return By.xpath(filterNameXPath);
    }

}
