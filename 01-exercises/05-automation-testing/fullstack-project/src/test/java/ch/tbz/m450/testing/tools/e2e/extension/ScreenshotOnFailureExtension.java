package ch.tbz.m450.testing.tools.e2e.extension;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.nio.file.Files;
import java.nio.file.Path;



/**
 * JUnit-5-Extension: Schlägt ein Test fehl, wird ein Screenshot des Browsers unter {@code target/screenshots/} abgelegt.
**/
public class ScreenshotOnFailureExtension implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        Object testInstance = context.getRequiredTestInstance();
        if (testInstance instanceof WebDriverProvider provider
                && provider.getDriver() instanceof TakesScreenshot camera) {
            try {
                Path dir = Path.of("target", "screenshots");
                Files.createDirectories(dir);
                String fileName = context.getRequiredTestClass().getSimpleName() + "_"
                        + context.getRequiredTestMethod().getName() + "_" + System.currentTimeMillis() + ".png";
                Path file = dir.resolve(fileName);
                Files.write(file, camera.getScreenshotAs(OutputType.BYTES));
                System.out.println("Screenshot des Fehlers gespeichert: " + file.toAbsolutePath());
            } catch (Exception e) {
                throwable.addSuppressed(e);
            }
        }
        throw throwable;
    }
}