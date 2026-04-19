package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page object representing the Periplus login/logout flow. */
public class LoginPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By preloader       = By.xpath("//div[@class='preloader']");
    private By loginMenuButton = By.id("nav-signin-text");
    private By emailField      = By.name("email");
    private By passwordField   = By.name("password");
    private By loginButton     = By.id("button-login");
    private By logoutButton    = By.xpath("//div[@class='shopping-item']//a[text()='Logout']");
    private By accountMenu     = By.id("nav-signin-text");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    /**
     * Authenticates the user with the provided credentials.
     * After login, it redirects the driver to the home page.
     * 
     * @param email The user's email address.
     * @param password The user's password.
     * @return A new instance of the HomePage.
     */
    public HomePage login(String email, String password) {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        wait.until(ExpectedConditions.elementToBeClickable(loginMenuButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailField)).sendKeys(email);
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField)).sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        wait.until(ExpectedConditions.urlContains("Your-Account"));
        driver.get(driver.getCurrentUrl().replaceAll("/account/.*", "/"));
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        return new HomePage(driver);
    }

    public void logout() {
        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(accountMenu));
        new Actions(driver).moveToElement(menu).perform();
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton)).click();
    }

    public boolean isLoggedIn() {
        return !driver.findElements(logoutButton).isEmpty();
    }
}
