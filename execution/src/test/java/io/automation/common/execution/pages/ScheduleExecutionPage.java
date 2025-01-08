package io.automation.common.execution.pages;

import io.automation.common.helpers.WebHelper;
import io.automation.common.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class ScheduleExecutionPage implements BasePage {

    @Autowired
    private WebHelper webHelper;

    public void verifySystemIsLoggedIn() {
        String homePageTitle = webHelper.verifyOnDashboardPage();
        assertEquals("Dashboard - automation.io", homePageTitle);
    }

    public void verifyOnConfigurationPage() {
        webHelper.getDriver().navigate().to("https://localhost:3000/#/robots/a8e265da-8aa5-4e45-8dfe-e30f184577e6/runs/0b1e7584-cc77-439a-b00b-0f85af93947b/edit/configuration");
        //Force a refresh to get to executions page
        webHelper.getDriver().navigate().refresh();
        webHelper.waitForElement(By.cssSelector("#content > section > h2 > span:nth-child(2)"));
        String configurationName = webHelper.readText(By.cssSelector("#content > section > h2 > span:nth-child(2)"));

        assertThat(configurationName, containsString("Webshop Demo"));
    }

    public void scheduleAnExecution() {
        webHelper.getDriver().findElement(By.linkText("Configuration")).click();

        String executionsTabName = webHelper.readText(By.linkText("Configuration"));
        assertEquals("Configuration", executionsTabName);

        String scheduledElement = "div.tab-pane.filled > div.tab-page > run-settings > div:nth-child(4) > input";

        // Schedule an execution
        WebElement scheduledCheckbox = webHelper.getDriver().findElement(By.cssSelector(scheduledElement));
        boolean isScheduled = scheduledCheckbox.isSelected();

        //remove scheduling if already scheduled
        if (isScheduled) {
            webHelper.waitForPageLoad();
            scheduledCheckbox.click();
            saveConfiguration();
        }

        webHelper.waitForPageLoad();
        webHelper.getDriver().findElement(By.cssSelector(scheduledElement)).click();

        //select the every 5th minute interval
        webHelper.getDriver().findElement(By.cssSelector("div.tab-page > run-settings > div:nth-child(5) > div.form-row > div.input.cron > span.cron-period > select > optgroup:nth-child(2) > option:nth-child(1)")).click();
        webHelper.waitForPageLoad();

        // save configuration
        saveConfiguration();
        webHelper.getDriver().navigate().refresh();
    }

    public void assertExecutionIsScheduled() {
        //driver.navigate().to();
        webHelper.getDriver().findElement(By.linkText("Dashboard")).click();
        webHelper.waitForPageLoad();

        // check execution is scheduled on dashboard
        WebElement scheduledExecution = webHelper.getDriver().findElement(By.cssSelector("div.execution.body.no-hover.row.scheduled.done:nth-child(2) > div.cell.created > span > span"));

        assertThat(scheduledExecution.getText(), notNullValue());
    }

    public void verifyExecutionRunsAtScheduledTIme() throws InterruptedException {
        // check execution runs under execution page
        webHelper.getDriver().findElement(By.cssSelector("div.execution.body.no-hover.row.scheduled.done:nth-child(2) > div.cell.actions > a:nth-child(3)")).click();
        webHelper.waitForPageLoad();

        webHelper.getDriver().findElement(By.linkText("Executions")).click();
        webHelper.waitForPageLoad();
        String scheduledExecutionState = "div.execution.body.no-hover.row.running.active:nth-of-type(2) > div.cell.progress";

        while (!webHelper.isPresent(scheduledExecutionState)) {
            Thread.sleep(10000);
        }

        assertEquals("Running", webHelper.getDriver().findElement(By.cssSelector(scheduledExecutionState)).getText());
    }

    private void saveConfiguration() {
        WebElement element = webHelper.getDriver().findElement(By.cssSelector("section > h2 > div > a.button.primary.small"));

        Actions actions = new Actions(webHelper.getDriver());
        actions.moveToElement(element).click().perform();

        webHelper.waitForPageLoad();
    }

    public void successfullyUnScheduleExecution() {
        webHelper.getDriver().findElement(By.linkText("Configuration")).click();
        webHelper.waitForPageLoad();

        // unschedule execution on dashboard
        webHelper.getDriver().findElement(By.cssSelector("div.tab-pane.filled > div.tab-page > run-settings > div:nth-child(4) > input")).click();
        saveConfiguration();
    }
}