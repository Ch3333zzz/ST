package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.example.pages.MainPage;
import org.example.pages.searchPages.HotelSearchPage;
import org.example.pages.searchPages.records.AccommodationType;
import org.example.pages.searchPages.records.HotelResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class HotelSearchPageTest {

    private WebDriver driver;
    private HotelSearchPage searchPage;
    private MainPage mainPage;

    private final String city = "Москва";
    private final String checkIn = "29.06.2026";
    private final String checkOut = "12.07.2026";
    private final int adults = 2;

    @BeforeEach
    public void setUp() {

        ChromeOptions options = new ChromeOptions();

        // options.addArguments("--headless=new");

        driver = new ChromeDriver(options);

        driver.get(PropertyReader.getProperty("mainpage"));
        mainPage = new MainPage(driver);

        searchPage = mainPage.searchHotel(city, checkIn, checkOut, adults);

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testFilterByHotelType() {
        searchPage.selectFilter("Отели");

        List<HotelResult> results = searchPage.getResults(3);
        assertFalse(results.isEmpty());
        results.forEach(h -> assertEquals(AccommodationType.HOTEL, h.type()));
    }

    @Test
    public void testSortingByPriceAsc() {
        searchPage.sortBy("Сначала дешёвые");

        List<HotelResult> results = searchPage.getResults(5);
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).price() <= results.get(i + 1).price());
        }
    }

    @Test
    public void testSortingByPriceDesc() {
        searchPage.sortBy("Сначала дорогие");

        List<HotelResult> results = searchPage.getResults(5);
        assertFalse(results.isEmpty());

        for (int i = 0; i < results.size() - 1; i++) {
            int currentPrice = results.get(i).price();
            int nextPrice = results.get(i + 1).price();
            assertTrue(currentPrice >= nextPrice);
        }
    }

    @Test
    public void testPriceRangeFilter() {
        int minPrice = 5000;
        int maxPrice = 15000;

        searchPage.selectPriceRange(minPrice, maxPrice);

        List<HotelResult> results = searchPage.getResults(5);
        assertFalse(results.isEmpty());

        for (HotelResult hotel : results) {
            int pricePerNight = hotel.price() / hotel.nights();

            assertTrue(pricePerNight >= minPrice && pricePerNight <= maxPrice);
        }
    }
}