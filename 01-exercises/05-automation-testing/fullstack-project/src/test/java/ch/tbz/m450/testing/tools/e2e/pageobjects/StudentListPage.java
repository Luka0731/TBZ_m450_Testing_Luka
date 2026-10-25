package ch.tbz.m450.testing.tools.e2e.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;



public class StudentListPage {
    private static final By TABLE_HEADERS = By.cssSelector("app-student-list table thead th");
    private static final By TABLE_ROWS = By.cssSelector("app-student-list table tbody tr");
    private final WebDriver driver;
    private final WebDriverWait wait;
    private final String baseUrl;

    public StudentListPage(WebDriver driver, String baseUrl) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }



    public StudentListPage open() {
        driver.get(baseUrl + "/students");
        return this;
    }

    /** Die Daten kommen asynchron vom Backend – daher explizit warten statt Thread.sleep(). */
    public StudentListPage waitForAtLeastRows(int minRows) {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TABLE_ROWS, minRows - 1));
        return this;
    }

    public StudentListPage waitForStudent(String name) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(rowOf(name)));
        return this;
    }

    public List<String> headers() {
        return driver.findElements(TABLE_HEADERS).stream().map(WebElement::getText).toList();
    }

    public List<String> names() {
        return driver.findElements(TABLE_ROWS).stream()
                .map(row -> row.findElement(By.cssSelector("td:nth-child(2)")).getText())
                .toList();
    }

    public String emailOf(String name) {
        return driver.findElement(rowOf(name)).findElement(By.cssSelector("td:nth-child(3)")).getText();
    }

    public String mailtoLinkOf(String name) {
        return driver.findElement(rowOf(name)).findElement(By.cssSelector("td:nth-child(3) a")).getDomAttribute("href");
    }

    public boolean isTableVisible() {
        return driver.findElement(By.cssSelector("app-student-list table")).isDisplayed();
    }

    /** Tabellenzeile, deren 2. Spalte (Name) genau dem gesuchten Namen entspricht. */
    private static By rowOf(String name) {
        return By.xpath("//app-student-list//tbody/tr[td[2][normalize-space()='" + name + "']]");
    }
}