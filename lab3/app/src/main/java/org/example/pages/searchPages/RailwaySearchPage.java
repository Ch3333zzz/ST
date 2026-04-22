package org.example.pages.searchPages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RailwaySearchPage extends SearchPage {
    public RailwaySearchPage(WebDriver driver) { super(driver); }

  @Override
  protected By getSortElement() {
    return By.xpath("//div[@data-ti=\"sorting\"]");
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
      String universalFilterXPath = "//div[contains(@data-ti, 'filter') and .//span[text()='%s']]";
      
      return By.xpath(universalFilterXPath.formatted(filterName));
  }
}
