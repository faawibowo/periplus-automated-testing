package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/** Page object representing the Periplus home page and its global navbar actions. */
public class HomePage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By preloader    = By.xpath("//div[@class='preloader']");
    private By searchBar    = By.id("filter_name_desktop");
    private By searchButton = By.cssSelector("button.btnn[type='submit']");
    private By cartIcon     = By.id("show-your-cart");
    private By cartBadge    = By.id("cart_total");

    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }

    /**
     * Clears the search bar, types the given keyword, and submits the search form.
     *
     * @param keyword search term to enter
     * @return {@link SearchResultPage} with the resulting product list
     */
    public SearchResultPage searchProduct(String keyword) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchBar))
                .clear();
        driver.findElement(searchBar).sendKeys(keyword);
        driver.findElement(searchButton).click();
        return new SearchResultPage(driver);
    }

    /**
     * Clicks the cart icon in the navbar.
     *
     * @return {@link CartPage} for the shopping cart
     */
    public CartPage goToCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartIcon)).click();
        return new CartPage(driver);
    }

    /**
     * Reads the numeric badge displayed on the cart icon.
     *
     * @return item count shown on the badge, or {@code 0} if the badge is not visible
     */
    public int getCartBadgeCount() {
        try {
            String countText = wait
                    .until(ExpectedConditions.visibilityOfElementLocated(cartBadge))
                    .getText().trim();
            return Integer.parseInt(countText);
        } catch (Exception e) {
            return 0; // badge absent means empty cart
        }
    }
}
