package org.example.pages.searchPages;

import java.util.ArrayList;
import java.util.List;

import org.example.pages.searchPages.records.BusResult;
import org.example.pages.searchPages.records.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class BusSearchPage extends SearchPage {
    public BusSearchPage(WebDriver driver) {
        super(driver);
    }

    public List<BusResult> getBusResults(int count) {
        By cardLocator = By.xpath("//div[@data-ti-product='bus']");

        wait.until(ExpectedConditions.visibilityOfElementLocated(cardLocator));

        List<BusResult> results = new ArrayList<>();
        int availableCards = driver.findElements(cardLocator).size();
        int limit = Math.min(count, availableCards);

        for (int i = 0; i < limit; i++) {
            final int index = i;
            WebElement card = wait.until(d -> d.findElements(cardLocator).get(index));

            String depTime = card.findElement(By.xpath(".//p[@data-ti='departure-time']")).getText();
            String arrTime = card.findElement(By.xpath(".//p[@data-ti='arrival-time']")).getText();

            String depCity = card.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='city']"))
                    .getText();
            String depPlace = card.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='place']"))
                    .getText();

            String arrCity = card.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='city']")).getText();
            String arrPlace = card.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='place']"))
                    .getText();

            String busName = "Рейсовый автобус";
            List<WebElement> badges = card.findElements(By.xpath(".//div[@data-ti='card-badges']//span"));
            if (!badges.isEmpty()) {
                busName = badges.get(0).getText();
            }

            int passengers = parseNumeralToInt(card.getText());

            results.add(new BusResult(
                    new Location(depCity, depPlace),
                    new Location(arrCity, arrPlace),
                    depTime,
                    arrTime,
                    busName,
                    passengers));
        }
        return results;
    }

    private int parseNumeralToInt(String text) {
        String lowerText = text.toLowerCase();
        if (lowerText.contains("одного"))
            return 1;
        if (lowerText.contains("двоих"))
            return 2;
        if (lowerText.contains("троих"))
            return 3;
        if (lowerText.contains("четверых"))
            return 4;
        if (lowerText.contains("пятерых"))
            return 5;
        if (lowerText.contains("шестерых"))
            return 6;
        if (lowerText.contains("семерых"))
            return 7;
        if (lowerText.contains("восьмерых"))
            return 8;

        return 9;
    }

    @Override
    protected By getSortElement() {
        return By.xpath("//div[@data-ti=\"sorting\"]//span[@data-ti=\"rich-icon\"]");
    }

    @Override
    protected By getSortedByElement(String option) {
        return By.xpath(
                "//div[@data-ti=\"dropdown\"]//div[@data-ti=\"dropdown-item\" or @data-ti=\"dropdown-item-selected\"]//span[text() = '%s']"
                        .formatted(option));
    }

    @Override
    protected By getOfferElement(String offerName) {
        return By.xpath(
                "//div[contains(@data-ti, 'offer-card')]//span[contains(text(), '%s')]"
                        .formatted(offerName));
    }

    @Override
    protected By getFilterElement(String filterName) {
        String universalFilterXPath = "(//div[@data-ti='shortcuts']//span[text()='%s']/ancestor::div[@data-ti][1])[2]";

        return By.xpath(universalFilterXPath.formatted(filterName));
    }
}
