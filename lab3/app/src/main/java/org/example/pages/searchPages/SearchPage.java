package org.example.pages.searchPages;

import org.example.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public abstract class SearchPage extends Page {
    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public void sortBy(String option) {
        clickOnElement(getSortElement());
        clickOnElement(getSortedByElement(option));
    }

    public void selectOffer(String offerName) {
        clickOnElement(getOfferElement(offerName));
    }

    public void selectFilter(String filterName) {
        clickOnElement(getFilterElement(filterName));
    }

    protected void clickOnElement(By Element) {
        wait.until(ExpectedConditions.elementToBeClickable(Element)).click();
    }

    protected abstract By getSortElement();

    protected abstract By getSortedByElement(String option);

    protected abstract By getOfferElement(String offerName);

    protected abstract By getFilterElement(String filterName);
}