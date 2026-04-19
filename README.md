# Periplus Automated Testing - Shopping Cart Suite

This project contains automated end-to-end tests for the shopping cart functionality on [Periplus](https://www.periplus.com). The tests are built using **Java**, **Selenium WebDriver**, **TestNG**, and **Maven**, following the Page Object Model (POM) architectural pattern.

## 1. Components of a Test Case
The following components define our automated testing process:

| No | Component | Description |
|---|---|---|
| 1 | Test Case ID | A unique identifier (e.g., TC-001). |
| 2 | Test Case Name | A short, descriptive title summarizing the test. |
| 3 | Objective | A concise statement of what the test is verifying. |
| 4 | Preconditions | Required system state before execution (e.g., user logged in). |
| 5 | Test Data | Specific inputs required to execute the test. |
| 6 | Test Steps | Ordered actions performed to execute the scenario. |
| 7 | Expected Result | The correct outcome if the system behaves as intended. |
| 8 | Actual Result | The outcome that actually occurred during execution. |
| 9 | Status | Pass or Fail result. |
| 10 | Comments | Additional notes or defect observations. |

---

## 2. Automated Test Results (`CartTest.java`)

### TC-001: Add single product to cart and verify its name and price
*   **Objective**: Verify that a single product can be successfully added to the cart, and its displayed name and total price match the unit price.
*   **Preconditions**: User is successfully logged in. Cart is completely empty.
*   **Test Data**: `search.keyword`
*   **Test Steps**:
    1. Search for a product using the designated keyword.
    2. Click on the first product from the search results.
    3. Retrieve and store the expected product name and product price.
    4. Click the "Add to Cart" button.
    5. Navigate to the Cart page.
*   **Expected Result**: The exact product should be present in the cart, and the cart's total price should match the product's unit price.
*   **Actual Result**: The product is present in the cart, and the total price matches the expected unit price.
*   **Status**: **PASS**

### TC-002: Adding same product twice results in total quantity of 2
*   **Objective**: Verify that adding the exact same product to the shopping cart twice increments its total quantity to 2.
*   **Preconditions**: User is successfully logged in. Cart is completely empty.
*   **Test Data**: `search.keyword`
*   **Test Steps**:
    1. Search for a product, select the first result, and click "Add to Cart".
    2. Navigate back to the search results page.
    3. Click the same product and click "Add to Cart" again.
    4. Navigate to the Cart page.
    5. Loop through the cart items to calculate the total quantity.
*   **Expected Result**: The total quantity of the product in the cart should be exactly 2.
*   **Actual Result**: The total quantity remained 1. The item quantity did not increase after the second addition attempt.
*   **Status**: **FAIL**
*   **Comments**: **Critical Bug**: The system does not increment the quantity of an existing item when the "Add to Cart" action is repeated.

### TC-003: Adding different products all appear in cart
*   **Objective**: Verify that adding multiple distinct products in a single session results in all of them appearing correctly in the cart.
*   **Preconditions**: User is successfully logged in. Cart is completely empty.
*   **Test Data**: `search.keyword`, `search.keyword2`
*   **Test Steps**:
    1. Search for the first keyword, select the product, and add it to the cart.
    2. Navigate back to the home page.
    3. Search for the second keyword, select the product, and add it to the cart.
    4. Navigate to the Cart page.
*   **Expected Result**: The cart should contain exactly 2 different items.
*   **Actual Result**: The cart successfully displays 2 distinct product items.
*   **Status**: **PASS**

### TC-004: Cart badge count updates correctly after adding product
*   **Objective**: Verify that the cart badge counter on the navigation bar correctly increments after a product is added.
*   **Preconditions**: User is successfully logged in. Cart is completely empty.
*   **Test Data**: `search.keyword`
*   **Test Steps**:
    1. Search for a product and add it to the cart.
    2. Retrieve the cart badge count text from the navigation bar.
*   **Expected Result**: The cart badge on the navbar should display the number "1".
*   **Actual Result**: The cart badge correctly updated to show the number "1".
*   **Status**: **PASS**

### TC-005: Remove one product from cart
*   **Objective**: Verify that a user can successfully remove a product from the shopping cart.
*   **Preconditions**: User is successfully logged in. Cart contains at least 1 product.
*   **Test Data**: `search.keyword`
*   **Test Steps**:
    1. Navigate to the Cart page.
    2. Record initial item count.
    3. Click "Remove" on the first product.
    4. Verify the new count and check if product name still exists.
*   **Expected Result**: The cart item count should decrease by 1, and the removed product should no longer be in the cart.
*   **Actual Result**: The cart item count decreased by 1, and the product was successfully removed.
*   **Status**: **PASS**

### TC-006: Increasing quantity updates total price correctly
*   **Objective**: Verify that manually increasing the quantity of a product in the cart dynamically updates the overall total price.
*   **Preconditions**: User is successfully logged in. Cart contains exactly 1 product.
*   **Test Steps**:
    1. Navigate to the Cart page and record initial total price.
    2. Click the "Increase Quantity" button (+).
    3. Retrieve the updated total price and compare.
*   **Expected Result**: The total price should change and reflect the increased quantity.
*   **Actual Result**: The total price successfully updated after the quantity was increased.
*   **Status**: **PASS**

### TC-007: Total price equals unit price times quantity
*   **Objective**: Verify that the cart's total price calculation is strictly accurate (Unit Price × Quantity).
*   **Preconditions**: User is successfully logged in. Cart contains 1 product.
*   **Test Data**: Target Quantity: 3
*   **Test Steps**:
    1. Record total price at quantity 1.
    2. Set quantity to 3 via JavaScript execution.
    3. Record new total price and verify calculation.
*   **Expected Result**: The total price at quantity 3 should be exactly 3 times the total price at quantity 1.
*   **Actual Result**: The total price correctly equaled the unit price multiplied by 3.
*   **Status**: **PASS**

### TC-008: Total price is correct sum of all different items
*   **Objective**: Verify that the overall cart total price is the correct cumulative sum of multiple distinct items.
*   **Preconditions**: User is logged in. Cart is empty.
*   **Test Data**: `search.keyword`, `search.keyword2`
*   **Test Steps**:
    1. Add first product (save price).
    2. Add second product (save price).
    3. Compare grand total in cart to the sum of saved prices.
*   **Expected Result**: The cart total should precisely equal the sum of both individual item prices.
*   **Actual Result**: The cart total accurately matched the sum of all distinct item prices.
*   **Status**: **PASS**

### TC-009: Cart persists after page refresh
*   **Objective**: Verify that the cart contents persist and are not lost when the user refreshes the browser page.
*   **Preconditions**: User is logged in. Cart contains 1 product.
*   **Test Steps**:
    1. Navigate to the Cart page and assert product presence.
    2. Refresh the browser page.
    3. Verify the product's presence again.
*   **Expected Result**: The product should still be present in the cart after the page refresh.
*   **Actual Result**: The cart contents remained intact and persisted after the page refresh.
*   **Status**: **PASS**

### TC-010: Cart persists after logout and re-login
*   **Objective**: Verify that the cart contents are permanently tied to the user's account and persist across sessions.
*   **Preconditions**: User is logged in. Cart contains 1 product.
*   **Test Data**: Valid user credentials.
*   **Test Steps**:
    1. Add product to cart.
    2. Logout and then log back in.
    3. Navigate to the Cart page and verify presence.
*   **Expected Result**: The product should still be in the cart after logging out and re-logging in.
*   **Actual Result**: The cart contents successfully persisted across the logout and re-login cycle.
*   **Status**: **PASS**

---

## 3. Test Execution Summary

| TC ID | Test Method | Type | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| TC-001 | addSingleProductToCart | Positive | Product in cart, total matches unit price | Matches | **PASS** |
| TC-002 | addSameProductTwiceIncreasesQuantity | Positive | Total quantity should be 2 | Total quantity remained 1 | **FAIL** |
| TC-003 | addMultipleDifferentProductsToCart | Positive | Cart contains 2 different items | 2 items present | **PASS** |
| TC-004 | cartBadgeCountUpdatesAfterAdd | Positive | Cart badge shows "1" | Shows "1" | **PASS** |
| TC-005 | removeOneProductFromCart | Positive | Item count decreases, product removed | Correctly removed | **PASS** |
| TC-006 | increaseQuantityUpdatesTotalPrice | Positive | Total price changes after increase | Updated correctly | **PASS** |
| TC-007 | totalPriceEqualsUnitPriceTimesQuantity | Positive | Total price equals unit price × 3 | Matches exactly | **PASS** |
| TC-008 | totalPriceCorrectForMultipleItems | Positive | Total price equals sum of both items | Matches sum | **PASS** |
| TC-009 | cartPersistsAfterPageRefresh | Positive | Cart contents persist after refresh | Remained intact | **PASS** |
| TC-010 | cartPersistsAfterRelogin | Positive | Cart contents persist after re-login | Persisted | **PASS** |

---

## How to Run
1. Ensure you have **Java 11+** and **Maven** installed.
2. Update `src/main/resources/config.properties` with valid credentials if necessary.
3. Run the following command in the project root:
   ```bash
   mvn test
   ```
