package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.HomePage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;   // <-- Make sure this package/class exists

public class TC01_LaunchApplication extends BaseClass {

    @Test(
        groups = { "sanity", "regression" },
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    void testLaunchApplication() {

        log.info("===== TC01_LaunchApplication STARTED =====");
        log.debug("Initializing HomePage object using ThreadLocal driver");

        try {
            HomePage hp = new HomePage(getDriver()); // Just initializing (kept for framework consistency)
            log.debug("HomePage object created: {}", hp.getClass().getSimpleName());

            log.debug("Fetching page title...");
            String actualTitle = getDriver().getTitle();
            log.info("Page title captured: {}", actualTitle);

            // Assertion with try-catch for logging + screenshot
            try {
                log.debug("Validating page title matches expected value...");
                Assert.assertEquals(actualTitle, "Your store of fun", "Title mismatch!");
                log.info("✅ Assertion PASSED: Title matched expected value");

            } catch (AssertionError ae) {
                log.error("❌ Assertion FAILED: Title did not match. Actual: [{}], Expected: [{}]",
                        actualTitle, "Your store of fun", ae);

                String screenshotPath = captureScreen("TC01_LaunchApplication");
                log.info("Screenshot captured for failure: {}", screenshotPath);

                throw ae; // important: rethrow so TestNG marks it as failed (RetryAnalyzer will kick in)
            }

        } catch (Exception e) {
            log.error("Unexpected exception occurred in TC01_LaunchApplication", e);

            // Screenshot for unexpected exception too
            String screenshotPath = captureScreen("TC01_LaunchApplication_EXCEPTION");
            log.info("Screenshot captured for exception: {}", screenshotPath);

            throw e; // rethrow so test fails properly
        } finally {
            log.info("===== TC01_LaunchApplication FINISHED =====");
        }
    }
}
