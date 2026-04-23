package org.example.pages.searchPages;

import java.util.ArrayList;
import java.util.List;

import org.example.pages.searchPages.records.AviaResult;
import org.example.pages.searchPages.records.FlightSegment;
import org.example.pages.searchPages.records.Location;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AviaSearchPage extends SearchPage {

  public AviaSearchPage(WebDriver driver) {
    super(driver);
  }

  public List<AviaResult> getAviaResults(int count) {
    By cardLocator = By.xpath("//div[@data-ti-product='avia']");

    wait.until(ExpectedConditions.visibilityOfElementLocated(cardLocator));

    List<AviaResult> results = new ArrayList<>();
    int availableCards = driver.findElements(cardLocator).size();
    int limit = Math.min(count, availableCards);

    for (int i = 0; i < limit; i++) {
      final int index = i;
      WebElement card = wait.until(d -> d.findElements(cardLocator).get(index));

      List<FlightSegment> segments = new ArrayList<>();
      List<WebElement> routeBlocks = card.findElements(By.xpath(".//div[starts-with(@data-ti, 'route-')]"));

      for (WebElement route : routeBlocks) {
        String depTime = route.findElement(By.xpath(".//p[@data-ti='departure-time']")).getText();
        String arrTime = route.findElement(By.xpath(".//p[@data-ti='arrival-time']")).getText();
        boolean isDirect = !route.findElements(By.xpath(".//span[text()='Прямой']")).isEmpty();

        String depCityText = route.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='city']"))
            .getText();
        String depPlaceText = route.findElement(By.xpath(".//div[@data-ti='departure']//span[@data-ti='place']"))
            .getText();

        String arrCityText = route.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='city']")).getText();
        String arrPlaceText = route.findElement(By.xpath(".//div[@data-ti='arrival']//span[@data-ti='place']"))
            .getText();

        segments.add(new FlightSegment(
            new Location(depCityText, depPlaceText),
            new Location(arrCityText, arrPlaceText),
            depTime, arrTime, isDirect));
      }

      List<String> airlines = card.findElements(By.xpath(".//div[@data-ti='carrier-badge']//span[not(@data-ti)]"))
          .stream()
          .map(WebElement::getText)
          .filter(text -> !text.isBlank())
          .distinct()
          .toList();

      String priceRaw = card.findElement(By.xpath(".//span[@data-ti='price']")).getText();
      int price = Integer.parseInt(priceRaw.replaceAll("[^0-9]", ""));

      String cardFullText = card.getText().toLowerCase();
      boolean withBaggage = cardFullText.contains("с багажом") || (!cardFullText.contains("без багажа"));
      int passengers = parseAviaPassengers(cardFullText);

      boolean isBookable = !card.findElements(By.xpath(".//span[contains(text(), 'Забронировать')]")).isEmpty();

      results.add(new AviaResult(segments, passengers, withBaggage, isBookable, airlines, price));
    }

    return results;
  }

  private int parseAviaPassengers(String text) {
    if (text.contains("одного"))
      return 1;
    if (text.contains("двоих"))
      return 2;
    if (text.contains("троих"))
      return 3;
    if (text.contains("четверых"))
      return 4;
    if (text.contains("пятерых"))
      return 5;
    if (text.contains("шестерых"))
      return 6;
    if (text.contains("семерых"))
      return 7;
    if (text.contains("восьмерых"))
      return 8;
    return 1;
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