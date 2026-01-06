package testCases;

import org.testng.Assert;
import org.testng.annotations.Test;

import pageObjects.AccountPage;
import pageObjects.HomePage;
import pageObjects.LoginPage;
import testBase.BaseClass;
import utilities.DataProviders;
import utilities.RetryAnalyzer;

public class TC02_Login extends BaseClass {

    @Test(
        groups = { "sanity", "regression", "datadriven" },
        dataProvider = "LoginData",
        dataProviderClass = DataProviders.class,
        retryAnalyzer = utilities.RetryAnalyzer.class
    )
    void testLogin(String email, String pwd) {

        log.info("===== TC02_Login STARTED =====");
        log.debug("Test data received -> Email: {}, Password length: {}",
                email, (pwd == null ? 0 : pwd.length()));

        try {
            // Step 1: Home Page actions
            log.debug("Initializing HomePage");
            HomePage hp = new HomePage(getDriver());

            log.info("Clicking My Account");
            hp.clickMyAccount();

            log.info("Navigating to Login page");
            hp.goToLogin();

            // Step 2: Login Page actions
            log.debug("Initializing LoginPage");
            LoginPage lp = new LoginPage(getDriver());

            log.info("Entering email");
            lp.setEmail(email);

            log.info("Entering password");
            lp.setPwd(pwd);

            log.info("Clicking Login button");
            lp.clickLogin();

            // Step 3: Account Page validation
            log.debug("Initializing AccountPage");
            AccountPage ap = new AccountPage(getDriver());

            boolean status = ap.getMyAccountConfirmation().isDisplayed();
            log.info("MyAccount confirmation displayed: {}", status);

            // Assertion with logging + screenshot
            try {
                log.debug("Asserting login status");
                Assert.assertTrue(status, "Login failed: MyAccount not displayed");
                log.info("✅ Login assertion PASSED");

            } catch (AssertionError ae) {
                log.error("❌ Login assertion FAILED for email: {}", email, ae);

                String screenshotPath = captureScreen("TC02_Login");
                log.info("Screenshot captured at: {}", screenshotPath);

                throw ae; // Required for RetryAnalyzer
            }

            // Step 4: Logout (only if login is successful)
            try {
                log.info("Performing logout");
                ap.clickMyAccountDropDown();
                ap.clickLogout();
                log.info("Logout successful");

            } catch (Exception e) {
                log.warn("Logout failed, but login was successful", e);
            }

        } catch (Exception e) {
            log.error("Unexpected exception occurred during TC02_Login", e);

            String screenshotPath = captureScreen("TC02_Login_EXCEPTION");
            log.info("Screenshot captured at: {}", screenshotPath);

            throw e;
        } finally {
            log.info("===== TC02_Login FINISHED =====");
        }
    }
}
