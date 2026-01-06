package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.CategoryPage;
import pageObjects.ProductPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC03_AddToCart extends BaseClass {

    @Test(
        groups = { "sanity", "regression" },
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    public void testAddToCart() {

        log.info("===== TC03_AddToCart STARTED =====");

        try {
            log.debug("Initializing CategoryPage");
            CategoryPage cp = new CategoryPage(getDriver());

            log.info("Clicking Laptops & Notebooks category");
            cp.clickLaptopsAndNotebooks();

            log.info("Clicking Show All");
            cp.clickShowAll();

            // Optional: better than Thread.sleep, but keeping your original flow
            log.debug("Waiting briefly for products to load (Thread.sleep)");
            Thread.sleep(500);

            log.info("Selecting HP product");
            cp.selectHPProduct();

            log.debug("Initializing ProductPage");
            ProductPage pp = new ProductPage(getDriver());

            log.info("Setting delivery date");
            pp.setDeliveryDate();

            log.info("Clicking Add to Cart");
            pp.clickAddToCart();

            boolean isSuccess = pp.isSuccessMessageDisplayed();
            log.info("Success message displayed: {}", isSuccess);

            // Assertion with try-catch for better logging + screenshot
            try {
                log.debug("Asserting Add to Cart success message is displayed");
                Assert.assertTrue(isSuccess, "Add to Cart Failed!");
                log.info("✅ Assertion PASSED: Product added to cart successfully");

            } catch (AssertionError ae) {
                log.error("❌ Assertion FAILED: Add to Cart success message not displayed", ae);

                String screenshotPath = captureScreen("TC03_AddToCart");
                log.info("Screenshot captured at: {}", screenshotPath);

                throw ae; // Required so TestNG marks it failed and RetryAnalyzer can retry
            }

        } catch (InterruptedException ie) {
            log.error("Thread interrupted during wait in TC03_AddToCart", ie);

            String screenshotPath = captureScreen("TC03_AddToCart_INTERRUPTED");
            log.info("Screenshot captured at: {}", screenshotPath);

            Thread.currentThread().interrupt(); // good practice
            throw new RuntimeException(ie);

        } catch (Exception e) {
            log.error("Unexpected exception occurred during TC03_AddToCart", e);

            String screenshotPath = captureScreen("TC03_AddToCart_EXCEPTION");
            log.info("Screenshot captured at: {}", screenshotPath);

            throw e;

        } finally {
            log.info("===== TC03_AddToCart FINISHED =====");
        }
    }
}
