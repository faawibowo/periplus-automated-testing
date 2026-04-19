package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

/**
 * Base class for all TestNG test classes.
 * Handles configuration loading, WebDriver lifecycle, and shared utility methods.
 */
public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected Properties config;

    /** Loads {@code config.properties} and sets up WebDriverManager once before the suite runs. */
    @BeforeSuite
    public void loadConfig() {
        config = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found!");
            }
            config.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties: " + e.getMessage());
        }
        WebDriverManager.chromedriver().setup();
    }

    /**
     * Initializes a new ChromeDriver instance before each test method.
     * Configures browser options such as maximized window and disabled notifications.
     */
    @BeforeMethod
    public void setupDriver() {

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        driver.get(config.getProperty("base.url"));
    }

    /** Quits the browser after each test to free resources. */
    @AfterMethod
    public void teardownDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Retrieves a value from the loaded properties file.
     *
     * @param key property key (e.g. {@code "base.url"})
     * @return the corresponding value, or {@code null} if the key is absent
     */
    protected String getConfig(String key) {
        return config.getProperty(key);
    }
}
