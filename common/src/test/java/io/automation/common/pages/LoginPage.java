package io.automation.common.pages;


import io.automation.common.helpers.WebHelper;
import io.automation.common.page.BasePage;
import org.openqa.selenium.By;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.junit.Assert.assertEquals;


@Component
public class LoginPage implements BasePage {
    @Autowired
    private WebHelper webHelper;


    public void goToLoginPage() {
        webHelper.getWebDriver().get("https://localhost:3000/login");
        webHelper.waitForElement(By.className("onboarding-font-header"));

        String actualString = webHelper.readText(By.className("onboarding-font-header"));
        assertEquals("Sign in", actualString);
    }

    public void loginInToTheUI(String username, String password) {
        webHelper.writeText(By.name("email"), username);
        webHelper.writeText(By.name("password"), password);
        webHelper.getDriver().findElement(By.cssSelector("button[class='nonuser-primary tst-login-button']")).click();
    }

    public void verifySuccessfulLogin() {
        String dashboard = webHelper.getDriver().findElement(By.cssSelector("h2 > span")).getText();
        assertEquals("Dashboard", dashboard);
    }
}