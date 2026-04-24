package org.example;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.example.pages.MainPage;
import org.example.pages.searchPages.RailwaySearchPage;
import org.example.pages.searchPages.records.RailwayResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class RailwaySearchPageTest {

    public List<WebDriver> driverList;

    private final String from = "Москва";
    private final String to = "Санкт-Петербург";
    private final String date = "30.06.2026";

    @BeforeEach
    public void setUp() {
        driverList = new ArrayList<>();

        driverList.add(new ChromeDriver());

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--width=1136");
        options.addArguments("--height=741");

        driverList.add(new FirefoxDriver(options));
    }

    @AfterEach
    public void tearDown() {
        if (driverList != null) {
            driverList.forEach(WebDriver::quit);
        }
    }

    @Test
    public void earlyDepartmentFirst() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.sortBy("По\u00a0времени отправления");
            List<RailwayResult> earlyOnes = searchPage.getRailwayResults(5);
            for (int i = 0; i < earlyOnes.size() - 1; i++) {
                assertTrue(earlyOnes.get(i).departureTime().compareTo(earlyOnes.get(i + 1).departureTime()) <= 0);
            }
        });
    }

    @Test
    public void earlyArrivalFirst() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.sortBy("По\u00a0времени прибытия");
            List<RailwayResult> earlyOnes = searchPage.getRailwayResults(5);
            for (int i = 0; i < earlyOnes.size() - 1; i++) {
                assertTrue(earlyOnes.get(i).arrivalTime().compareTo(earlyOnes.get(i + 1).arrivalTime()) <= 0);
            }
        });
    }

    @Test
    public void cheapFirst() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.sortBy("Сначала дешёвые");
            List<RailwayResult> cheapOnes = searchPage.getRailwayResults(5);
            for (int i = 0; i < cheapOnes.size() - 1; i++) {
                assertTrue(cheapOnes.get(i).price() <= cheapOnes.get(i + 1).price());
            }
        });
    }

    @ParameterizedTest
    @ValueSource(strings = { "Сапсан", "Ласточка" })
    public void testTrainTypeFilters(String trainType) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.selectFilter(trainType);

            List<RailwayResult> results = searchPage.getRailwayResults(3);
            assertFalse(results.isEmpty());

            results.forEach(train -> assertTrue(train.trainName().contains(trainType)));
        });
    }

    @Test
    public void selectFilterOption() {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.selectFilter("Сапсан");
        });
    }

    @ParameterizedTest
    @MethodSource("wagonTypeProvider")
    public void testMultipleWagonTypesFilter(String[] targetTypes) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage searchPage = mainPage.searchTrain(from, to, date, 1);

            searchPage.selectWagonTypes(targetTypes);

            List<RailwayResult> results = searchPage.getRailwayResults(2);
            assertFalse(results.isEmpty());

            for (RailwayResult train : results) {
                boolean hasMatchingWagon = train.wagonTypes().stream()
                        .anyMatch(actualWagon -> Arrays.stream(targetTypes)
                                .anyMatch(target -> actualWagon.equalsIgnoreCase(target)));

                assertTrue(hasMatchingWagon);
            }
        });
    }

    static Stream<Arguments> wagonTypeProvider() {
        return Stream.of(
                Arguments.of((Object) new String[] { "Плацкарт", "Купе" }),
                Arguments.of((Object) new String[] { "СВ", "Люкс" }),
                Arguments.of((Object) new String[] { "Сидячий" }));
    }
}