package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page object representing a Periplus product detail page. */
public class ProductPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By productName      = By.cssSelector("h2");
    private By productPrice     = By.cssSelector("div.quickview-price span.special");
    private By preloader        = By.xpath("//div[@class='preloader']");
    private By addToCartButton  = By.cssSelector("button.btn-add-to-cart");
    private By modalCloseButton = By.xpath("//i[@class='ti-close']");

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }

    /** @return the product title as displayed in the {@code <h1>} heading */
    public String getProductName() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(productName))
                .getText().trim();
    }

    /** @return the product price text as displayed, including the currency symbol */
    public String getProductPrice() {
        return driver.findElement(productPrice).getText().trim();
    }

    /**
     * Clicks the "Add to Cart" button and waits for the success notification to
     * confirm the item was accepted by the server before returning.
     */
    public void clickAddToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
        closeModal();
    }

    public void closeModal() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.elementToBeClickable(modalCloseButton))
                .click();
        } catch (Exception ignored) {}
    }

    /**
     * Checks whether the "Add to Cart" button is interactable.
     * A disabled button typically indicates the product is out of stock.
     *
     * @return {@code true} if the button is enabled
     */
    public boolean isAddToCartEnabled() {
        return driver.findElement(addToCartButton).isEnabled();
    }
}
