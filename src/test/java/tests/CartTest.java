package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductPage;
import pages.SearchResultPage;

/**
 * End-to-end test suite for the shopping cart feature on periplus.com.
 * Covers: add to cart, remove, quantity updates, price calculation, and cart persistence.
 *
 * <p>Each test starts with a clean cart (cleared in {@code @BeforeMethod}) and
 * leaves a clean cart (cleared in {@code @AfterMethod}).</p>
 */
public class CartTest extends BaseTest {

    private LoginPage loginPage;
    private HomePage homePage;
    private CartPage cartPage;

    @BeforeMethod
    public void precondition() {
        loginPage = new LoginPage(driver);
        homePage  = loginPage.login(
                getConfig("test.email"),
                getConfig("test.password")
        );
        cartPage = homePage.goToCart();
        cartPage.clearCart();
        driver.get(getConfig("base.url"));
        homePage = new HomePage(driver);
    }

    /**
     * Searches for {@code keyword}, adds the first result to the cart, and returns
     * the product name for use in assertions.
     */
    private String addOneProductToCart(String keyword) {
        SearchResultPage searchPage = homePage.searchProduct(keyword);
        ProductPage productPage = searchPage.clickFirstProduct();
        String productName = productPage.getProductName();
        productPage.clickAddToCart();
        return productName;
    }


    @Test(description = "Add single product to cart and verify its name and price")
    public void addSingleProductToCart() {
        SearchResultPage searchPage = homePage.searchProduct(getConfig("search.keyword"));
        ProductPage productPage = searchPage.clickFirstProduct();
        
        String expectedName = productPage.getProductName();
        String expectedPrice = productPage.getProductPrice();
        
        productPage.clickAddToCart();

        cartPage = homePage.goToCart();

        Assert.assertTrue(
                cartPage.containsProduct(expectedName),
                "Product '" + expectedName + "' should be in the cart"
        );
        
        Assert.assertEquals(
                cartPage.getTotalPrice(), expectedPrice,
                "Cart total price should match the product unit price for a single item"
        );
    }

    @Test(description = "Adding same product twice results in total quantity of 2")
    public void addSameProductTwiceIncreasesQuantity() {
        SearchResultPage searchPage = homePage.searchProduct(getConfig("search.keyword"));
        ProductPage productPage = searchPage.clickFirstProduct();
        productPage.clickAddToCart();

        // Navigate back to search results to add the same product again
        driver.navigate().back();
        searchPage = new SearchResultPage(driver);
        productPage = searchPage.clickFirstProduct();
        productPage.clickAddToCart();

        cartPage = homePage.goToCart();

        int itemCount = cartPage.getItemCount();
        int totalQty = 0;
        for (int i = 0; i < itemCount; i++) {
            totalQty += cartPage.getQuantityAt(i);
        }

        Assert.assertEquals(
                totalQty, 2,
                "Total quantity should be 2 after adding same product twice"
        );
    }

    @Test(description = "Adding different products all appear in cart")
    public void addMultipleDifferentProductsToCart() {
        SearchResultPage searchPage = homePage.searchProduct(getConfig("search.keyword"));
        searchPage.clickFirstProduct().clickAddToCart();

        driver.get(getConfig("base.url"));
        homePage = new HomePage(driver);
        searchPage = homePage.searchProduct(getConfig("search.keyword2"));
        searchPage.clickFirstProduct().clickAddToCart();

        cartPage = homePage.goToCart();

        Assert.assertEquals(
                cartPage.getItemCount(), 2,
                "Cart should contain 2 different items"
        );
    }

    @Test(description = "Cart badge count updates correctly after adding product")
    public void cartBadgeCountUpdatesAfterAdd() {
        addOneProductToCart(getConfig("search.keyword"));

        int badgeCount = homePage.getCartBadgeCount();

        Assert.assertEquals(
                badgeCount, 1,
                "Cart badge in navbar should show 1 after adding 1 product"
        );
    }


    @Test(description = "Remove one product from cart")
    public void removeOneProductFromCart() {
        String productName = addOneProductToCart(getConfig("search.keyword"));

        cartPage = homePage.goToCart();
        int countBefore = cartPage.getItemCount();
        cartPage.removeFirstProduct();

        Assert.assertEquals(
                cartPage.getItemCount(), countBefore - 1,
                "Cart item count should decrease by 1 after removal"
        );
        Assert.assertFalse(
                cartPage.containsProduct(productName),
                "Product '" + productName + "' should no longer be in the cart"
        );
    }


    /**
     * Increases the quantity of a product and verifies the total price updates correctly.
     */
    @Test(description = "Increasing quantity updates total price correctly")
    public void increaseQuantityUpdatesTotalPrice() {
        addOneProductToCart(getConfig("search.keyword"));

        cartPage = homePage.goToCart();
        String totalBefore = cartPage.getTotalPrice();

        cartPage.increaseQuantityAt(0);

        String totalAfter = cartPage.getTotalPrice();

        Assert.assertNotEquals(
                totalAfter, totalBefore,
                "Total price should change after quantity is increased"
        );
    }


    @Test(description = "Total price equals unit price times quantity")
    public void totalPriceEqualsUnitPriceTimesQuantity() {
        addOneProductToCart(getConfig("search.keyword"));

        cartPage = homePage.goToCart();
        String priceQty1 = cartPage.getTotalPrice();

        cartPage.setQuantityAt(0, 3);
        String priceQty3 = cartPage.getTotalPrice();

        double price1 = parsePrice(priceQty1);
        double price3 = parsePrice(priceQty3);

        Assert.assertEquals(
                price3, price1 * 3, 1.0, // tolerance for rounding
                "Total price at qty 3 should be 3x the price at qty 1"
        );
    }

    @Test(description = "Total price is correct sum of all different items")
    public void totalPriceCorrectForMultipleItems() {
        SearchResultPage searchPage = homePage.searchProduct(getConfig("search.keyword"));
        ProductPage productPage1 = searchPage.clickFirstProduct();
        double price1 = parsePrice(productPage1.getProductPrice());
        productPage1.clickAddToCart();

        driver.get(getConfig("base.url"));
        homePage = new HomePage(driver);
        searchPage = homePage.searchProduct(getConfig("search.keyword2"));
        ProductPage productPage2 = searchPage.clickFirstProduct();
        double price2 = parsePrice(productPage2.getProductPrice());
        productPage2.clickAddToCart();

        cartPage = homePage.goToCart();
        double total = parsePrice(cartPage.getTotalPrice());

        Assert.assertEquals(
                total, price1 + price2, 1.0,
                "Cart total should equal sum of both item prices"
        );
    }


    @Test(description = "Cart persists after page refresh")
    public void cartPersistsAfterPageRefresh() {
        String productName = addOneProductToCart(getConfig("search.keyword"));

        cartPage = homePage.goToCart();
        Assert.assertTrue(cartPage.containsProduct(productName));

        driver.navigate().refresh();
        cartPage = new CartPage(driver);

        Assert.assertTrue(
                cartPage.containsProduct(productName),
                "Product should still be in the cart after page refresh"
        );
    }

    @Test(description = "Cart persists after logout and re-login")
    public void cartPersistsAfterRelogin() {
        String productName = addOneProductToCart(getConfig("search.keyword"));

        loginPage.logout();

        homePage = loginPage.login(
                getConfig("test.email"),
                getConfig("test.password")
        );
        cartPage = homePage.goToCart();

        Assert.assertTrue(
                cartPage.containsProduct(productName),
                "Product should still be in the cart after logout and re-login"
        );
    }


    /**
     * Strips all non-digit characters from a price string and parses it as a double.
     * Example: {@code "Rp 150.000"} → {@code 150000.0}
     */
    private double parsePrice(String priceStr) {
        String cleaned = priceStr.replaceAll("[^\\d]", "");
        return Double.parseDouble(cleaned);
    }
}