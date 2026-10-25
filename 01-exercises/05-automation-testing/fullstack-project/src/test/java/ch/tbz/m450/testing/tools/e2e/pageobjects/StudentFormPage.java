package ch.tbz.m450.testing.tools.e2e.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;



public class StudentFormPage {
    private static final By NAME_INPUT = By.id("name");
    private static final By EMAIL_INPUT = By.id("email");
    private static final By SUBMIT_BUTTON = By.cssSelector("app-student-form button[type='submit']");
    private static final By NAME_HINT = hint("Name is required");
    private static final By EMAIL_HINT = hint("Email is required");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public StudentFormPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



    public StudentFormPage open() {
        driver.get(baseUrl + "/addstudents");
        return waitUntilVisible();
    }

    public StudentFormPage waitUntilVisible() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(NAME_INPUT));
        return this;
    }

    public StudentFormPage enterName(String name) {
        driver.findElement(NAME_INPUT).sendKeys(name);
        return this;
    }

    public StudentFormPage enterEmail(String email) {
        driver.findElement(EMAIL_INPUT).sendKeys(email);
        return this;
    }

    /**
     * Löscht das Namensfeld wie ein Benutzer mit der Backspace-Taste.
     * (WebElement.clear() löst bei Angular nicht zuverlässig das input-Event aus.)
     */
    public StudentFormPage clearName() {
        WebElement input = driver.findElement(NAME_INPUT);
        int length = input.getDomProperty("value").length();
        for (int i = 0; i < length; i++) {
            input.sendKeys(Keys.BACK_SPACE);
        }
        return this;
    }

    public void submit() {
        driver.findElement(SUBMIT_BUTTON).click();
    }

    public boolean isSubmitEnabled() {
        return driver.findElement(SUBMIT_BUTTON).isEnabled();
    }

    public boolean isNameHintVisible() {
        return driver.findElement(NAME_HINT).isDisplayed();
    }

    public boolean isEmailHintVisible() {
        return driver.findElement(EMAIL_HINT).isDisplayed();
    }

    private static By hint(String text) {
        return By.xpath("//app-student-form//div[contains(@class,'alert') and normalize-space()='" + text + "']");
    }
}