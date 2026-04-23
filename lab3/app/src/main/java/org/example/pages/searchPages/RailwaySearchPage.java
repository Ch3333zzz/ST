package org.example.pages.searchPages;

import java.util.ArrayList;
import java.util.List;

import org.example.pages.searchPages.records.Location;
import org.example.pages.searchPages.records.RailwayResult;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class RailwaySearchPage extends SearchPage {
  public RailwaySearchPage(WebDriver driver) {
    super(driver);
  }

  public void selectWagonTypes(String... wagonTypes) {
    By filterButtonLocator = By.xpath("//div[@data-ti='filter-carType']");

    WebElement filterButton = wait.until(ExpectedConditions.elementToBeClickable(filterButtonLocator));
    filterButton.click();

    for (String type : wagonTypes) {

      String optionXPath = "//div[@data-ti='filter']//span[contains(translate(text(), 'АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ', 'абвгдеёжзийклмнопрстуфхцчшщъыьэюя'), '"
          + type.toLowerCase() + "')]";

      int attempts = 0;
      while (attempts < 3) {
        try {
          WebElement option = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));
          option.click();

          break;
        } catch (StaleElementReferenceException e) {
          attempts++;
        }
      }
    }

    try {
      filterButton.click();
    } catch (Exception ignored) {

    }
  }

  public List<RailwayResult> getRailwayResults(int count) {
    By cardLocator = By.xpath("//div[@data-ti='offer-card']");
    wait.until(ExpectedConditions.visibilityOfElementLocated(cardLocator));

    List<RailwayResult> results = new ArrayList<>();
    int availableCards = driver.findElements(cardLocator).size();
    int limit = Math.min(count, availableCards);

    for (int i = 0; i < limit; i++) {
      final int index = i;
      WebElement card = wait.until(d -> d.findElements(cardLocator).get(index));

      String depTime = card.findElement(By.xpath(".//p[@data-ti='departure-time']")).getText();
      String arrTime = card.findElement(By.xpath(".//p[@data-ti='arrival-time']")).getText();

      String depCity = card.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='city']")).getText();
      String depPlace = card.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='place']")).getText();

      String arrCity = card.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='city']")).getText();
      String arrPlace = card.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='place']")).getText();

      String trainName = "Обычный поезд";
      List<WebElement> badges = card.findElements(By.xpath(".//div[@data-ti='card-badges']//span"));
      if (!badges.isEmpty()) {
        trainName = badges.get(0).getText();
      }

      List<String> wagonTypes = card.findElements(By.xpath(".//button[@data-ti='offer-tariff']//span"))
          .stream()
          .map(WebElement::getText)
          .filter(text -> text.matches(".*(Сидячий|Плацкарт|Купе|СВ|Люкс).*"))
          .distinct()
          .toList();

      String priceRaw = card.findElement(By.xpath("//div[@data-ti='offer-card']//span[@data-ti='price']")).getText();
      int price = Integer.parseInt(priceRaw.replaceAll("[^0-9]", ""));

      String fullCardText = card.getText().toLowerCase();
      int passengers = parseNumeralToInt(fullCardText);

      results.add(new RailwayResult(
          new Location(depCity, depPlace),
          new Location(arrCity, arrPlace),
          depTime,
          arrTime,
          trainName,
          wagonTypes,
          passengers,
          price));
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

    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(universalFilterXPath.formatted(filterName))));
    return By.xpath(universalFilterXPath.formatted(filterName));
  }
}
