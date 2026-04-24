package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.example.pages.MainPage;
import org.example.pages.searchPages.AviaSearchPage;
import org.example.pages.searchPages.records.AviaResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class AviaSearchPageTest {

    private List<WebDriver> driverList;

    private final String from = "Москва";
    private final String to = "Сочи";
    private final String date = "29.06.2026";

    @BeforeEach
    public void setUp() {
        driverList = new ArrayList<>();

        driverList.add(new ChromeDriver());

        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--width=1136");
        options.addArguments("--height=741");

        WebDriver driver = new FirefoxDriver(options);

        driverList.add(driver);
    }

    @AfterEach
    public void tearDown() {
        if (driverList != null) {
            driverList.forEach(WebDriver::quit);
        }
    }

    @Test
    public void testAviaFilters() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage searchPage = mainPage.searchAvia(from, to, date, null, 1, "Эконом", new String[] {});

            searchPage.selectFilter("Прямой");
            searchPage.getAviaResults(5)
                    .forEach(ticket -> assertTrue(ticket.segments().get(0).isDirect()));

            searchPage.selectFilter("С багажом");
            searchPage.getAviaResults(5)
                    .forEach(ticket -> assertTrue(ticket.withBaggage()));
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "S7 Airlines", "Аэрофлот" })
    public void testAirlineFilter(String airlineName) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage searchPage = mainPage.searchAvia(from, to, date, null, 1, "Эконом", new String[] {});

            searchPage.selectFilter(airlineName);
            searchPage.getAviaResults(3).forEach(ticket -> assertTrue(ticket.airlines().contains(airlineName)));
        });
    }

    @Test
    public void firstEarly() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage searchPage = mainPage.searchAvia(from, to, date, null, 1, "Эконом", new String[] {});

            searchPage.sortBy("Сначала ранние");
            List<AviaResult> earlyResults = searchPage.getAviaResults(5);
            for (int i = 0; i < earlyResults.size() - 1; i++) {
                String timeCurrent = earlyResults.get(i).segments().get(0).departureTime();
                String timeNext = earlyResults.get(i + 1).segments().get(0).departureTime();
                assertTrue(timeCurrent.compareTo(timeNext) <= 0);
            }
        });
    }

    @Test
    public void testLate() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage searchPage = mainPage.searchAvia(from, to, date, null, 1, "Эконом", new String[] {});

            searchPage.sortBy("Сначала поздние");
            List<AviaResult> lateResults = searchPage.getAviaResults(5);
            for (int i = 0; i < lateResults.size() - 1; i++) {
                String timeCurrent = lateResults.get(i).segments().get(0).departureTime();
                String timeNext = lateResults.get(i + 1).segments().get(0).departureTime();
                assertTrue(timeCurrent.compareTo(timeNext) >= 0);
            }
        });
    }

    @Test
    public void testAviaPriceSorting() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage searchPage = mainPage.searchAvia(from, to, date, null, 1, "Эконом", new String[] {});

            searchPage.sortBy("Сначала дешёвые");
            List<AviaResult> results = searchPage.getAviaResults(5);
            for (int i = 0; i < results.size() - 1; i++) {
                assertTrue(results.get(i).price() <= results.get(i + 1).price());
            }
        });
    }
}