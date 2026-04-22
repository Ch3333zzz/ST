package org.example.pages.searchPages;

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
                if (inputLocator != null && (driver.findElements(inputLocator).isEmpty() || !driver.findElement(inputLocator).isDisplayed())) {
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

    @Override
    protected By getSortElement() {
        String selectSortXPath = "//div[@data-ti=\"list-header\"]//span[@data-ti=\"rich-icon\"]";

        return By.xpath(selectSortXPath);
    }

    @Override
    protected By getSortedByElement(String option) {
        String optionXPath = "//div[@data-ti=\"dropdown\"]//div[@data-ti=\"dropdown-item\" or @data-ti=\"dropdown-item-selected\"]//span[text() = '%s']".formatted(option);

        return By.xpath(optionXPath);
    }

    @Override
    protected By getOfferElement(String offerName) {
        String offerNameXPath = "//div[@data-ti=\"offer-card\"]//div[@data-ti=\"hotel-name\"]//span[text() = '%s']".formatted(offerName);

        return By.xpath(offerNameXPath);
    }

    @Override
    protected By getFilterElement(String filterName) {
        String filterNameXPath = "//div[@data-ti=\"chip-row\"]//span[text() = '%s']".formatted(filterName);

        return By.xpath(filterNameXPath);
    }

}

