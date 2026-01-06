package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AffiliatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.RetryAnalyzer;

public class TC06_AddAffiliate extends BaseClass {

    @Test(
        groups = { "regression" },
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    void testAddAffiliate() throws Exception {

        log.info("===== TC06_AddAffiliate STARTED =====");

        try {
            // Step 1: Login
            log.debug("Initializing HomePage");
            HomePage hp = new HomePage(getDriver());

            log.info("Clicking My Account");
            hp.clickMyAccount();

            log.info("Navigating to Login page");
            hp.goToLogin();

            log.debug("Initializing LoginPage");
            LoginPage lp = new LoginPage(getDriver());

            log.info("Entering email");
            lp.setEmail("sid@cloudberry.services");

            log.info("Entering password");
            lp.setPwd("Test123");

            log.info("Submitting login");
            lp.clickLogin();

            // Step 2: Affiliate form
            log.debug("Initializing AffiliatePage");
            AffiliatePage ap = new AffiliatePage(getDriver());

            log.info("Navigating to Affiliate form");
            ap.navigateToAffiliateForm();

            log.info("Filling Affiliate details");
            ap.fillAffiliateDetails(
                    "CloudBerry",
                    "cloudberry.services",
                    "123456",
                    "Shadab Siddiqui"
            );

            boolean affiliateStatus = ap.isAffiliateAdded();
            log.info("Affiliate added status: {}", affiliateStatus);

            // Assertion with logging + screenshot
            try {
                log.debug("Asserting affiliate addition");
                Assert.assertTrue(affiliateStatus, "Affiliate details not added successfully.");
                log.info("✅ Assertion PASSED: Affiliate added successfully");

            } catch (AssertionError ae) {
                log.error("❌ Assertion FAILED: Affiliate was not added successfully", ae);

                String screenshotPath = captureScreen("TC06_AddAffiliate");
                log.info("Screenshot captured at: {}", screenshotPath);

                throw ae; // required for RetryAnalyzer
            }

        } catch (Exception e) {
            log.error("Unexpected exception occurred during TC06_AddAffiliate", e);

            String screenshotPath = captureScreen("TC06_AddAffiliate_EXCEPTION");
            log.info("Screenshot captured at: {}", screenshotPath);

            throw e;

        } finally {
            log.info("===== TC06_AddAffiliate FINISHED =====");
        }
    }
}
