package org.example;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.example.pages.MainPage;
import org.example.pages.searchPages.AviaSearchPage;
import org.example.pages.searchPages.BusSearchPage;
import org.example.pages.searchPages.HotelSearchPage;
import org.example.pages.searchPages.RailwaySearchPage;
import org.example.pages.searchPages.records.AviaResult;
import org.example.pages.searchPages.records.BusResult;
import org.example.pages.searchPages.records.FlightSegment;
import org.example.pages.searchPages.records.HotelResult;
import org.example.pages.searchPages.records.RailwayResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class MainPageTest {
    public List<WebDriver> driverList;

    private final static String startDate = "29.06.2026";
    private final static String endDate = "12.07.2026";

    private final static String city1 = "Москва";
    private final static String city2 = "Сочи";

    private final static String[] emptyKidAges = new String[] {};
    private final static String[] toddlerKidAges = new String[] { "Меньше года", "1 год" };
    private final static String[] elderKidAges = new String[] { "3 года", "5 лет" };
    private final static String[] kidAgesBothTypes = new String[] { "Меньше года", "1 год", "2 года", "5 лет" };

    private final static int adultCount = 3;

    private final static String economClass = "Эконом";
    private final static String buisnessClass = "Бизнес";

    private final static int expectedNights = 13;

    @BeforeEach
    public void setUp() {
        driverList = new ArrayList<>();

        driverList.add(new ChromeDriver());

        // FirefoxOptions options = new FirefoxOptions();
        // options.addArguments("--width=1136");
        // options.addArguments("--height=741");

        // driverList.add(new FirefoxDriver(options));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("hotelSearchDataProvider")
    public void searchHotelTest(String testName, String city, String checkIn, String checkOut, int adults,
            String[] kids) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            HotelSearchPage searchPage = mainPage.searchHotel(city, checkIn, checkOut, adults, kids);

            int cardsToCheck = 1;
            List<HotelResult> results = searchPage.getResults(cardsToCheck);
            int expectedTotalGuests = adults + (kids != null ? kids.length : 0);

            assertAll("Checking results of search hotels",
                    () -> assertFalse(results.isEmpty()),
                    () -> {
                        for (int i = 0; i < results.size(); i++) {
                            HotelResult hotel = results.get(i);
                            int index = i + 1;
                            assertAll("Params " + index,
                                    () -> assertTrue(hotel.location().contains(city)),
                                    () -> assertEquals(expectedTotalGuests, hotel.guests()),
                                    () -> assertEquals(expectedNights, hotel.nights()));
                        }
                    });
        });
    }

    private static Stream<Arguments> hotelSearchDataProvider() {
        return Stream.of(
                Arguments.of("ID-1: without kids", city1, startDate, endDate, 3,
                        emptyKidAges),
                Arguments.of("ID-2: with kids", city1, startDate, endDate, 3, kidAgesBothTypes));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("aviaSearchDataProvider")
    public void aviaSearchTest(String testName, String to, String from, String depDate,
            String retDate, int adults, String travelClass, String[] kids) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            AviaSearchPage resultsPage = mainPage.searchAvia(to, from, depDate, retDate, adults, travelClass, kids);

            List<AviaResult> results = resultsPage.getAviaResults(5);
            int expectedTotalPassengers = adults + kids.length;

            assertAll("Checking results of search avia",
                    () -> assertFalse(results.isEmpty()),
                    () -> {
                        for (int i = 0; i < results.size(); i++) {
                            AviaResult ticket = results.get(i);
                            assertAll(
                                    () -> {
                                        FlightSegment outward = ticket.segments().get(0);
                                        assertAll(
                                                () -> assertTrue(outward.departure().city().contains(from)),
                                                () -> assertTrue(outward.arrival().city().contains(to)));
                                    },
                                    () -> {
                                        if (retDate != null && ticket.segments().size() > 1) {
                                            FlightSegment returnTrip = ticket.segments().get(1);
                                            assertAll(
                                                    () -> assertTrue(returnTrip.departure().city().contains(to)),
                                                    () -> assertTrue(returnTrip.arrival().city().contains(from)));
                                        }
                                    },
                                    () -> assertEquals(expectedTotalPassengers, ticket.passengers()));
                        }
                    });
        });
    }

    static Stream<Arguments> aviaSearchDataProvider() {
        return Stream.of(
                Arguments.of("ID-3: Simple Search", city1, city2, startDate, null, adultCount, economClass,
                        emptyKidAges),
                Arguments.of("ID-4: Business Class", city1, city2, startDate, null, adultCount, buisnessClass,
                        emptyKidAges),
                Arguments.of("ID-5: Round Trip", city1, city2, startDate, endDate, adultCount, economClass,
                        emptyKidAges),
                Arguments.of("ID-6: Infant (no place)", city1, city2, startDate, null, adultCount, economClass,
                        toddlerKidAges),
                Arguments.of("ID-7: Child (with place)", city1, city2, startDate, null, adultCount, economClass,
                        elderKidAges),
                Arguments.of("ID-8: both child types", city2, city1, startDate, null, adultCount, economClass,
                        kidAgesBothTypes));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("railwaySearchDataProvider")
    public void railwaySearchTest(String testName, String from, String to, String date, int adults, String[] kids) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            RailwaySearchPage resultsPage = mainPage.searchTrain(from, to, date, adults, kids);

            List<RailwayResult> results = resultsPage.getRailwayResults(3);
            int expectedTotalPassengers = adults + kids.length;

            assertAll("Checking results of search railway",
                    () -> assertFalse(results.isEmpty()),
                    () -> {
                        for (int i = 0; i < results.size(); i++) {
                            RailwayResult train = results.get(i);
                            assertAll(
                                    () -> assertTrue(train.departure().city().contains(from)),
                                    () -> assertTrue(train.arrival().city().contains(to)),
                                    () -> assertEquals(expectedTotalPassengers, train.passengers()),
                                    () -> assertFalse(train.wagonTypes().isEmpty()));
                        }
                    });
        });
    }

    static Stream<Arguments> railwaySearchDataProvider() {
        return Stream.of(
                Arguments.of("ID-9: Simple Train Search", city1, city2, startDate, adultCount, emptyKidAges),
                Arguments.of("ID-10: Train Search with Kids", city1, city2, startDate, adultCount, kidAgesBothTypes));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("busSearchDataProvider")
    public void busSearchTest(String testName, String from, String to, String date, int adults, String[] kids) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            BusSearchPage resultsPage = mainPage.searchBus(from, to, date, adults, kids);

            List<BusResult> results = resultsPage.getBusResults(3);
            int expectedTotalPassengers = adults + kids.length;

            assertAll("Checking results of search bus",
                    () -> assertFalse(results.isEmpty()),
                    () -> {
                        for (int i = 0; i < results.size(); i++) {
                            BusResult bus = results.get(i);
                            assertAll(
                                    () -> assertTrue(bus.departure().city().contains(from)),
                                    () -> assertTrue(bus.arrival().city().contains(to)),
                                    () -> assertEquals(expectedTotalPassengers, bus.passengers()));
                        }
                    });
        });
    }

    private static Stream<Arguments> busSearchDataProvider() {
        return Stream.of(
                Arguments.of("ID-11: Simple Bus Search", city1, city2, startDate, adultCount, emptyKidAges),
                Arguments.of("ID-12: Bus Search with Kids", city1, city2, startDate, adultCount, kidAgesBothTypes));
    }

    @AfterEach
    public void tearDown() {
        // if (driverList != null) {
        // driverList.forEach(WebDriver::quit);
        // }
    }
}