package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.example.pages.MainPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import dev.failsafe.internal.util.Assert;

public class MainPageTest {
    public WebDriver driver;
    private MainPage mainPage;

    private LocalDate startDate = LocalDate.now();

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        
        String url = PropertyReader.getProperty("mainpage");
        driver.get(url);
        
        mainPage = new MainPage(driver);
    }

    @Test
    public void searchHotel() {
        int i = 1; //Moscow
        String dateFrom = "29.06.2026";
        String dateTo = "13.07.2026";
        short adults = 3;
        float[] kids = new float[]{0.6f, 1, 2, 3};

        mainPage.searchHotel(i, dateFrom, dateTo, adults, kids);

        String actualUrl = driver.getCurrentUrl();
        String decodedUrl = URLDecoder.decode(actualUrl, StandardCharsets.UTF_8);

        assertTrue(actualUrl.contains("check_in=2026-07-13"), "Incorrect date from in  URL!");
        assertTrue(actualUrl.contains("check_out=2026-07-14"), "Incorrect date to in URL!");

        assertTrue(decodedUrl.contains("geo_name=Москва"), "URL does not consist \"Москва - first in list\"");

        assertTrue(actualUrl.contains("room[0]=3.0-1-2-3"), "Guests parameters does not match!");
    }

    //ЫХВХАХЫАХХАЫВХ
    @Test
    @Disabled
    public void searchHotelwithName() {
        // String city = "Санкт-Петербург";
        String city = "Москва";
        String dateFrom = "29.06.2026";
        String dateTo = "13.07.2026";
        short adults = 3;
        float[] kids = new float[]{0.6f, 1, 2, 3};

        mainPage.searchHotel(city, dateFrom, dateTo, adults, kids);

        String actualUrl = driver.getCurrentUrl();

        assertTrue(actualUrl.contains("check_in=2026-07-13"), "Incorrect date from in  URL!");
        assertTrue(actualUrl.contains("check_out=2026-07-14"), "Incorrect date to in URL!");

        // assertTrue(actualUrl.contains("geo_name=Санкт-Петербург"), "URL does not consist \"moscow - first in list\"");
        assertTrue(actualUrl.contains("geo_name=Москва"), "URL does not consist \"moscow - first in list\"");

        assertTrue(actualUrl.contains("room[0]=3.0-1-2-3"), "Guests parameters does not match!");
    }

    @Test
    public void searchAirplane() {
        int i = 1; //Moscow
        int j = 2;
        String dateFrom = "29.06.2026";
        String dateTo = "13.07.2026";
        short adults = 3;
        float[] kids = new float[]{0.6f, 1, 2, 3};

        mainPage.searchAirplaneDefault(i, j, dateFrom, dateTo, adults, kids);

        String actualUrl = driver.getCurrentUrl();

        assertTrue(actualUrl.contains("Moskva"), "Incorrect date from in  URL!");
        assertTrue(actualUrl.contains("Sankt-peterburg"), "Incorrect date to in URL!");

        assertTrue(actualUrl.contains("class=Y"), "URL does not consist \"Москва - first in list\"");

        assertTrue(actualUrl.contains("travelers=3.0-1-2-3"), "Guests parameters does not match!");
    }
    
    @Test
    // @Disabled
    public void searchAirplaneBuisness() {
        String from = "Москва";
        String to = "Краснодар";
        String dateFrom = "29.06.2026";
        String dateTo = "13.07.2026";
        short adults = 3;
        float[] kids = new float[]{0.6f, 1, 2, 3};

        mainPage.searchAirplaneBuisness(from, to, dateFrom, dateTo, adults, kids);

        String actualUrl = driver.getCurrentUrl();
        String decodedUrl = URLDecoder.decode(actualUrl, StandardCharsets.UTF_8);

        assertTrue(actualUrl.contains("check_in=2026-07-13"), "Incorrect date from in  URL!");
        assertTrue(actualUrl.contains("check_out=2026-07-14"), "Incorrect date to in URL!");

        assertTrue(decodedUrl.contains("geo_name=Москва"), "URL does not consist \"Москва - first in list\"");

        assertTrue(actualUrl.contains("room[0]=3.0-1-2-3"), "Guests parameters does not match!");
    }
    
    

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            //driver.quit(); // Всегда закрывай браузер после теста
        }
    }
}
