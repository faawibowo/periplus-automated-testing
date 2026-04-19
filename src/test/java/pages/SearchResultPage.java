package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Page object representing the Periplus search results page. */
public class SearchResultPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By productCards   = By.cssSelector("div.single-product");
    private By emptyResultMsg = By.cssSelector("div.results");

    private By preloader = By.xpath("//div[@class='preloader']");

    public SearchResultPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }

    /**
     * Clicks the first product card and navigates to its detail page.
     *
     * @return {@link ProductPage} for the first result
     */
    public ProductPage clickFirstProduct() {
        List<WebElement> products = wait
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        products.get(0).click();
        return new ProductPage(driver);
    }

    /**
     * Clicks the product card at the given zero-based position in the result list.
     *
     * @param index zero-based index of the product card to click
     * @return {@link ProductPage} for the selected product
     */
    public ProductPage clickProductAt(int index) {
        List<WebElement> products = wait
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(productCards));
        products.get(index).click();
        return new ProductPage(driver);
    }

    /** @return {@code true} if at least one product card is visible on the page */
    public boolean hasResults() {
        return !driver.findElements(productCards).isEmpty();
    }

    /** @return {@code true} if the "no results" message element is present in the DOM */
    public boolean isEmptyResultDisplayed() {
        try {
            String resultsText = driver.findElement(emptyResultMsg).getText();
            return resultsText.contains("0 (0 Pages)");
        } catch (Exception e) {
            return false;
        }
    }

    /** @return total number of product cards currently visible on the page */
    public int getResultCount() {
        return driver.findElements(productCards).size();
    }
}
