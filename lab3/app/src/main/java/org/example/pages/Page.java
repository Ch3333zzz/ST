package org.example.pages;

import java.time.Duration;

import org.example.PropertyReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class Page {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    public Page(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    protected By getByWithProperty(String key) {
        return By.xpath(PropertyReader.getProperty(key));
    }
}
