package org.example.pages.specificPage;

import org.example.pages.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.example.pages.paymentPages.HotelPaymentPage;

import java.util.List;

public class HotelPage extends Page {

    private final By mainBookButton = By.xpath("//button[@data-ti='book-button']");

    public HotelPage(WebDriver driver) {
        super(driver);
    }

    public HotelPaymentPage book() {
        wait.until(ExpectedConditions.elementToBeClickable(mainBookButton)).click();
        return new HotelPaymentPage(driver);
    }

    public HotelPaymentPage book(String roomName) {
        By roomLocator = By.xpath("//div[@data-ti='room']");

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(roomLocator));
        List<WebElement> rooms = driver.findElements(roomLocator);

        for (WebElement room : rooms) {
            WebElement titleElement = room.findElement(By.xpath(".//div[@data-ti='label-value-group-header']//span"));

            if (titleElement.getText().contains(roomName)) {
                WebElement bookBtn = room.findElement(By.xpath(".//button[@data-ti='book-button']"));

                ((org.openqa.selenium.JavascriptExecutor) driver)
                        .executeScript("arguments[0].scrollIntoView({block: 'center'});", bookBtn);

                wait.until(ExpectedConditions.elementToBeClickable(bookBtn)).click();
                return new HotelPaymentPage(driver);
            }
        }

        throw new RuntimeException("No such room " + roomName);
    }
}