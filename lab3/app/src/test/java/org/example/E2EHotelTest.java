package org.example;

import org.example.pages.MainPage;
import org.example.pages.searchPages.HotelSearchPage;
import org.example.pages.specificPage.HotelPage;
import org.example.pages.paymentPages.HotelPaymentPage;
import org.example.pages.paymentPages.records.BookingDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class E2EHotelTest {

    private List<WebDriver> driverList;

    private final String city = "Москва";
    private final String checkInDate = "29.06.2026";
    private final String checkOutDate = "12.07.2026";

    private final String expectedCheckIn = "29 июня";
    private final String expectedCheckOut = "12 июля";

    private final int adultsCount = 3;
    private final static String[] kidAgesBothTypes = new String[] { "Меньше года", "1 год", "2 года", "5 лет" };
    private final int nightsCount = 13;

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

    @ParameterizedTest
    @CsvSource({
            "Гостиничный комплекс \"FISHERIX\", ДУПЛЕКС"
    })
    public void testHotelBookingFlow(String targetHotelName, String targetRoomName) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            HotelSearchPage searchPage = mainPage.searchHotel(city, checkInDate, checkOutDate, adultsCount,
                    kidAgesBothTypes);

            HotelPage hotelPage = searchPage.selectHotel(targetHotelName);
            HotelPaymentPage paymentPage = hotelPage.book(targetRoomName);
            verifyBookingDetails(paymentPage, targetHotelName);
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Дизайнерская трёхкомнатная квартира в стиле семейного лофта"
    })
    public void testApartmentBookingFlow(String targetApartmentName) {
        driverList.forEach(driver -> {
            driver.get(PropertyReader.getProperty("mainpage"));
            MainPage mainPage = new MainPage(driver);
            HotelSearchPage searchPage = mainPage.searchHotel(city, checkInDate, checkOutDate, adultsCount,
                    kidAgesBothTypes);

            HotelPage hotelPage = searchPage.selectHotel(targetApartmentName);
            HotelPaymentPage paymentPage = hotelPage.book();
            verifyBookingDetails(paymentPage, targetApartmentName);
        });
    }

    private void verifyBookingDetails(HotelPaymentPage paymentPage, String expectedName) {
        BookingDetails details = paymentPage.getBookingDetails();
        String info = details.guestsAndNightsInfo();

        assertAll(
                () -> assertTrue(details.hotelName().contains(expectedName)),
                () -> assertTrue(details.city().contains(city)),
                () -> assertTrue(details.checkInDate().contains(expectedCheckIn)),
                () -> assertTrue(details.checkOutDate().contains(expectedCheckOut)),
                () -> assertTrue(info.contains(String.valueOf(adultsCount) + " взросл")),
                () -> assertTrue(info.contains(String.valueOf(kidAgesBothTypes.length) + " дет")),
                () -> assertTrue(info.contains(String.valueOf(nightsCount + " ноч"))));
    }

    @AfterEach
    public void tearDown() {
        if (driverList != null) {
            driverList.forEach(WebDriver::quit);
        }
    }
}