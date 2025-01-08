package io.automation.common.helpers;

import io.automation.common.driver.DriverHook;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.RetryException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetrySynchronizationManager;
import org.springframework.stereotype.Component;

@EnableRetry
@Slf4j
@Component
public class WebHelper {

    @Autowired
    private DriverHook hooks;

    public WebDriverWait getWebDriverWait() {
        return hooks.getWait();
    }

    public WebDriver getWebDriver() {
        return hooks.getDriver();
    }

    public <T> void writeText(T elementAttr, String text) {
        waitForElement(elementAttr);
        if (elementAttr
                .getClass()
                .getName()
                .contains("By")) {
            getWebDriverWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy((By) elementAttr));
            getWebDriver().findElement((By) elementAttr).sendKeys(text);
        } else {
            getWebDriverWait().until(ExpectedConditions.visibilityOf((WebElement) elementAttr));
            ((WebElement) elementAttr).sendKeys(text);
        }
    }

    public <T> String readText(T elementAttr) {
        if (elementAttr
                .getClass()
                .getName()
                .contains("By")) {
            return getWebDriver().findElement((By) elementAttr).getText();
        } else {
            return ((WebElement) elementAttr).getText();
        }
    }

    public boolean isPresent(String element) {
        try {
            getWebDriver().findElement(By.cssSelector(element)).isDisplayed();
            return true;
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 500), include = {RetryException.class})
    public <T> void waitForElement(T elementAttr) {
        try {
        if (elementAttr
                .getClass()
                .getName()
                .contains("By")) {
            getWebDriverWait().until(ExpectedConditions.presenceOfElementLocated((By) elementAttr));
        } else {
            getWebDriverWait().until(ExpectedConditions.visibilityOf((WebElement) elementAttr));
        }
        } catch (Exception exception) {
            String errorMessage = "Unable to find element, Retrying attempt ";
            int retryCount = RetrySynchronizationManager.getContext().getRetryCount() + 1;
            log.error("{}: {}",errorMessage, retryCount + 1);
            throw new RetryException(errorMessage + retryCount);
        }
    }

    public void waitForPageLoad() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public WebDriver getDriver() {
        return getWebDriver();
    }

    public void login() {
        getWebDriver().get("https://localhost:3000/login");
        waitForElement(By.className("onboarding-font-header"));

        // could make these configurable
        writeText(By.name("email"), "system@automation.io");
        writeText(By.name("password"), "systemsecret");

        getWebDriver().findElement(By.cssSelector(".nonuser-primary")).click();
    }

    public String getHomePageTitle() {
        if (!getDriver().getCurrentUrl().equals("https://localhost:3000/#/")) {
            login();
        }
        return getDriver().getTitle();
    }

    public String verifyOnDashboardPage() {
        try {
            getDriver().findElement(By.linkText("Dashboard")).click();
        } catch (NoSuchElementException e) {
            login();
        }
        waitForPageLoad();

        return getDriver().getTitle();
    }
}
