package ch.tbz.m450.testing.tools.e2e;

import ch.tbz.m450.testing.tools.e2e.extension.ScreenshotOnFailureExtension;
import ch.tbz.m450.testing.tools.e2e.extension.WebDriverProvider;
import ch.tbz.m450.testing.tools.e2e.pageobjects.StudentFormPage;
import ch.tbz.m450.testing.tools.e2e.pageobjects.StudentListPage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;



@Tag("e2e")
@ExtendWith(ScreenshotOnFailureExtension.class)
@DisplayName("Übung 2 – E2E-Tests Angular-Frontend (Selenium)")
class StudentsE2ETest implements WebDriverProvider {
    private static final String FRONTEND_URL = System.getProperty("frontendUrl", "http://localhost:4200");
    private static final String BACKEND_URL = System.getProperty("backendUrl", "http://localhost:8081");
    private static final String BROWSER = System.getProperty("browser", "chrome");
    private static final boolean HEADLESS = Boolean.parseBoolean(System.getProperty("headless", "false"));
    private WebDriver driver;
    private WebDriverWait wait;
    private StudentListPage listPage;
    private StudentFormPage formPage;



    // |----- setup -----|

    @BeforeEach
    void startBrowser() {
        driver = createDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        listPage = new StudentListPage(driver, FRONTEND_URL);
        formPage = new StudentFormPage(driver, FRONTEND_URL);
    }

    @AfterEach
    void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    private static WebDriver createDriver() {
        return switch (BROWSER.toLowerCase()) {
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (HEADLESS) options.addArguments("-headless");
                yield new FirefoxDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                if (HEADLESS) options.addArguments("--headless=new");
                options.addArguments("--window-size=1280,900");
                yield new EdgeDriver(options);
            }
            default -> {
                ChromeOptions options = new ChromeOptions();
                if (HEADLESS) options.addArguments("--headless=new");
                options.addArguments("--window-size=1280,900");
                yield new ChromeDriver(options);
            }
        };
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }



    // |----- tests -----|

    @Test
    @DisplayName("Startseite zeigt Titel, Logo und Navigation")
    void startseiteZeigtLogoUndNavigation() {
        driver.get(FRONTEND_URL);

        wait.until(ExpectedConditions.titleIs("TBZ Students"));
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("img[alt='Responsive image']")));
        assertThat(logo.getDomAttribute("src")).contains("tbz_logo.png");
        assertThat(driver.findElement(By.linkText("List Students")).isDisplayed()).isTrue();
        assertThat(driver.findElement(By.linkText("Add Students")).isDisplayed()).isTrue();
    }

    @Test
    @DisplayName("Navigation über die Buttons wechselt zwischen Formular und Liste")
    void navigationUeberLinks() {
        driver.get(FRONTEND_URL);

        driver.findElement(By.linkText("Add Students")).click();
        wait.until(ExpectedConditions.urlMatches(".*/addstudents$"));
        formPage.waitUntilVisible();

        driver.findElement(By.linkText("List Students")).click();
        wait.until(ExpectedConditions.urlMatches(".*/students$"));
        listPage.waitForAtLeastRows(1);
        assertThat(listPage.isTableVisible()).isTrue();
    }

    @Test
    @DisplayName("Liste zeigt Spaltentitel und die 5 Seed-Studenten")
    void listeZeigtSeedStudenten() {
        listPage.open().waitForAtLeastRows(5);

        assertThat(listPage.headers()).containsExactly("#", "Name", "Email");
        assertThat(listPage.names()).contains("Jonas", "Patrick", "Yves", "Peter", "Ann");
        assertThat(listPage.emailOf("Jonas")).isEqualTo("jonas@tbz.ch");
        assertThat(listPage.mailtoLinkOf("Jonas")).isEqualTo("mailto:jonas@tbz.ch");
    }

    @Test
    @DisplayName("Submit-Button ist deaktiviert, bis Name UND Email ausgefüllt sind")
    void submitErstAktivWennFormularGueltig() {
        formPage.open();
        assertThat(formPage.isSubmitEnabled()).as("leeres Formular").isFalse();

        formPage.enterName("Nur Name");
        assertThat(formPage.isSubmitEnabled()).as("nur Name ausgefüllt").isFalse();

        formPage.enterEmail("nur.name@tbz.ch");
        assertThat(formPage.isSubmitEnabled()).as("Name und Email ausgefüllt").isTrue();
    }

    @ParameterizedTest(name = "Student \"{0}\" anlegen → erscheint in Liste und im Backend")
    @CsvSource({
            "Selenium Anna, anna.selenium@tbz.ch",
            "Selenium Beat, beat.selenium@tbz.ch",
    })
    @DisplayName("Neuer Student wird über das Formular angelegt (data-driven)")
    void neuerStudentErscheintInListe(String baseName, String email) throws Exception {
        String name = baseName + " " + System.currentTimeMillis(); // eindeutig pro Testlauf

        formPage.open()
                .enterName(name)
                .enterEmail(email)
                .submit();

        // Nach dem Speichern leitet Angular auf die Liste weiter
        wait.until(ExpectedConditions.urlMatches(".*/students$"));
        listPage.waitForStudent(name);
        assertThat(listPage.emailOf(name)).isEqualTo(email);

        // Zusätzlich direkt im Backend prüfen, dass der Datensatz wirklich gespeichert wurde
        HttpResponse<String> response = HttpClient.newHttpClient().send(
                HttpRequest.newBuilder(URI.create(BACKEND_URL + "/students")).GET().build(),
                HttpResponse.BodyHandlers.ofString());
        assertThat(response.statusCode()).isEqualTo(200);
        assertThat(response.body()).contains("\"name\":\"" + name + "\"");
    }

    @Test
    @DisplayName("BEFUND: Pflichtfeld-Hinweis verschwindet nach Eingabe und kommt beim Leeren nicht zurück")
    void befundPflichtfeldHinweis() {
        formPage.open();
        assertThat(formPage.isNameHintVisible()).as("Hinweis beim Öffnen").isTrue();
        assertThat(formPage.isEmailHintVisible()).as("Hinweis beim Öffnen").isTrue();

        formPage.enterName("Max");
        assertThat(formPage.isNameHintVisible()).as("Hinweis nach Eingabe").isFalse();

        formPage.clearName();
        // Ist-Verhalten: [hidden]="!name.pristine" prüft nur, ob das Feld berührt wurde, nicht ob es leer ist.
        // Soll-Verhalten wäre: Hinweis wieder anzeigen (z.B. [hidden]="name.valid || name.pristine").
        assertThat(formPage.isNameHintVisible()).as("Hinweis nach Leeren (Bug)").isFalse();
        assertThat(formPage.isSubmitEnabled()).as("Button nach Leeren").isFalse();
    }
}