package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Page object representing the Periplus shopping cart page. */
public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    private By preloader    = By.xpath("//div[@class='preloader']");
    private By cartItems = By.xpath("//div[contains(@class,'row-cart-product') and .//a[contains(@class,'btn-cart-remove')]]");
    private By productNames = By.cssSelector("div.row-cart-product p.product-name a");
    private By quantityInput = By.cssSelector("input.input-number");
    private By plusButton   = By.cssSelector("button.btn-number[data-type='plus']");
    private By minusButton  = By.cssSelector("button.btn-number[data-type='minus']");
    private By removeButton = By.cssSelector("a.btn-cart-remove");
    private By emptyCartMsg = By.xpath("//div[contains(@class,'content') and contains(text(),'Your shopping cart is empty')]");
    private By totalPrice   = By.id("sub_total");
    private By checkoutButton = By.cssSelector("a[onclick*='beginCheckout']");

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(15));
        this.wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }


    /**
     * Navigates directly to the cart page via URL.
     */
    public void open(String baseUrl) {
        driver.get(baseUrl + "/checkout/cart");
    }


    /**
     * Checks whether any cart row contains the given product name.
     */
    public boolean containsProduct(String productName) {
        List<WebElement> names = driver.findElements(productNames);
        return names.stream()
                .anyMatch(el -> el.getText().trim()
                        .equalsIgnoreCase(productName.trim()));
    }

    /** @return number of product rows currently in the cart */
    public int getItemCount() {
        return driver.findElements(cartItems).size();
    }

    /** @return true if the empty-cart message is present */
    public boolean isEmpty() {
        return !driver.findElements(emptyCartMsg).isEmpty();
    }

    /** @return total price text as displayed, e.g. "Rp 205.000" */
    public String getTotalPrice() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(totalPrice))
                .getText().trim();
    }

    /**
     * Reads the quantity value from the input field of the specified row.
     *
     * @param index zero-based row index
     */
    public int getQuantityAt(int index) {
        List<WebElement> inputs = driver.findElements(quantityInput);
        return Integer.parseInt(inputs.get(index).getAttribute("value").trim());
    }
    /**
     * Clicks the (+) button once for the specified cart row.
     * The page recalculates total automatically via onchange.
     *
     * @param index zero-based row index
     */
    public void increaseQuantityAt(int index) {
        List<WebElement> plusBtns = wait
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(plusButton));
        plusBtns.get(index).click();
        waitForTotalToUpdate();
    }

    public void decreaseQuantityAt(int index) {
        List<WebElement> minusBtns = wait
                .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(minusButton));
        minusBtns.get(index).click();
        waitForTotalToUpdate();
    }


    /**
     * Updates the quantity of a product at a specific index using JavaScript.
     * 
     * @param index The zero-based index of the cart item.
     * @param newQuantity The new quantity value to set.
     */
    public void setQuantityAt(int index, int newQuantity) {
        List<WebElement> inputs = driver.findElements(quantityInput);
        WebElement input = inputs.get(index);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].value = arguments[1];", input, String.valueOf(newQuantity));
        js.executeScript("arguments[0].dispatchEvent(new Event('change'));", input);

        try {
            driver.switchTo().alert().accept();
        } catch (Exception ignored) {}

        try { Thread.sleep(1500); } catch (InterruptedException ignored) {}
    }

    /**
     * Clicks the Remove button of the first product in the cart.
     */
    public void removeFirstProduct() {
        WebElement firstRemoveBtn = wait
                .until(ExpectedConditions.elementToBeClickable(removeButton));
        firstRemoveBtn.click();
        waitForCartToReload();
    }

    /**
     * Removes every item from the cart one by one.
     * Safe to call even when cart is already empty.
     */
    public void clearCart() {
        wait.until(d -> !d.findElements(cartItems).isEmpty() || !d.findElements(emptyCartMsg).isEmpty());

        List<WebElement> removeBtns = driver.findElements(removeButton);
        while (!removeBtns.isEmpty()) {
            WebElement btn = removeBtns.get(0);
            btn.click();
            wait.until(ExpectedConditions.stalenessOf(btn));
            wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
            wait.until(d -> !d.findElements(cartItems).isEmpty() || !d.findElements(emptyCartMsg).isEmpty());
            removeBtns = driver.findElements(removeButton);
        }
    }

    private void waitForTotalToUpdate() {
        String textBefore = driver.findElement(totalPrice).getText().trim();
        wait.until(d -> !d.findElement(totalPrice).getText().trim().equals(textBefore));
    }

    private void waitForCartToReload() {
        List<WebElement> items = driver.findElements(cartItems);
        if (!items.isEmpty()) {
            wait.until(ExpectedConditions.stalenessOf(items.get(0)));
        }
        wait.until(ExpectedConditions.invisibilityOfElementLocated(preloader));
    }
}