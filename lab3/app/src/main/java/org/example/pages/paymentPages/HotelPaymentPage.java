package org.example.pages.paymentPages;

import org.example.pages.Page;
import org.example.pages.paymentPages.records.BookingDetails;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HotelPaymentPage extends Page {

    private final By hotelCard = By.xpath("//div[@data-ti='hotel-card']");

    public HotelPaymentPage(WebDriver driver) {
        super(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(hotelCard));
    }

    public BookingDetails getBookingDetails() {
        return new BookingDetails(
                getHotelName(),
                getCity(),
                getCheckInDate(),
                getCheckOutDate(),
                getGuestsAndNightsInfo());
    }

    private String getHotelName() {
        return driver.findElement(By.xpath("//div[@data-ti='hotel-card']//span[@data-ti='hotel-name']//span"))
                .getText();
    }

    private String getCity() {
        return driver.findElement(By.xpath("//div[@data-ti='hotel-card']//span[@data-ti='summary-location']//span"))
                .getText();
    }

    private String getCheckInDate() {
        return driver
                .findElement(By.xpath(
                        "//div[@data-ti='hotel-card']//div[@data-ti='checkin']//span[@data-ti='block-title']//span"))
                .getText();
    }

    private String getCheckOutDate() {
        return driver
                .findElement(By.xpath(
                        "//div[@data-ti='hotel-card']//div[@data-ti='checkout']//span[@data-ti='block-title']//span"))
                .getText();
    }

    private String getGuestsAndNightsInfo() {
        return driver.findElement(By.xpath("//div[@data-ti='hotel-card']//span[@data-ti='hotel-info']//span"))
                .getText();
    }
}