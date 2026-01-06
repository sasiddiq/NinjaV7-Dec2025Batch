package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.CheckoutPage;
import pageObjects.ConfirmationPage;
import pageObjects.LoginPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC04_CompletePurchase extends BaseClass {

    @Test(
        groups = { "sanity", "regression" },
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    public void testCompletePurchase() {

        log.info("===== TC04_CompletePurchase STARTED =====");

        try {
            // Step 1: Navigate to product category
            log.debug("Initializing CategoryPage");
            CategoryPage cp = new CategoryPage(getDriver());

            log.info("Clicking Laptops & Notebooks category");
            cp.clickLaptopsAndNotebooks();

            log.info("Clicking Show All products");
            cp.clickShowAll();

            log.debug("Waiting briefly for products to load");
            Thread.sleep(500);

            log.info("Selecting HP product");
            cp.selectHPProduct();

            // Step 2: Product page actions
            log.debug("Initializing ProductPage");
            ProductPage pp = new ProductPage(getDriver());

            log.info("Setting delivery date");
            pp.setDeliveryDate();

            log.info("Adding product to cart");
            pp.clickAddToCart();

            log.info("Proceeding to checkout");
            pp.clickCheckout();

            // Step 3: Checkout login
            log.debug("Initializing CheckoutPage");
            CheckoutPage cop = new CheckoutPage(getDriver());

            log.info("Clicking Login on checkout page");
            cop.clickLogin();

            log.debug("Initializing LoginPage");
            LoginPage lp = new LoginPage(getDriver());

            log.info("Entering email");
            lp.setEmail("sid@cloudberry.services");

            log.info("Entering password");
            lp.setPwd("Test123");

            log.info("Submitting login");
            lp.clickLogin();

            // Step 4: Complete checkout
            log.info("Completing checkout process");
            cop.completeCheckout();

            // Step 5: Confirmation validation
            log.debug("Initializing ConfirmationPage");
            ConfirmationPage confirmationPage = new ConfirmationPage(getDriver());

            boolean orderStatus = confirmationPage.isOrderPlaced();
            log.info("Order placement status: {}", orderStatus);

            // Assertion with logging + screenshot
            try {
                log.debug("Asserting order placement");
                Assert.assertTrue(orderStatus, "Order placement failed!");
                log.info("✅ Assertion PASSED: Order placed successfully");

            } catch (AssertionError ae) {
                log.error("❌ Assertion FAILED: Order was not placed successfully", ae);

                String screenshotPath = captureScreen("TC04_CompletePurchase");
                log.info("Screenshot captured at: {}", screenshotPath);

                throw ae; // required for RetryAnalyzer
            }

        } catch (InterruptedException ie) {
            log.error("Thread interrupted during wait in TC04_CompletePurchase", ie);

            String screenshotPath = captureScreen("TC04_CompletePurchase_INTERRUPTED");
            log.info("Screenshot captured at: {}", screenshotPath);

            Thread.currentThread().interrupt();
            throw new RuntimeException(ie);

        } catch (Exception e) {
            log.error("Unexpected exception occurred during TC04_CompletePurchase", e);

            String screenshotPath = captureScreen("TC04_CompletePurchase_EXCEPTION");
            log.info("Screenshot captured at: {}", screenshotPath);

            throw e;

        } finally {
            log.info("===== TC04_CompletePurchase FINISHED =====");
        }
    }
}
