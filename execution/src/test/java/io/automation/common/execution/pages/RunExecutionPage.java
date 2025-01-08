package io.automation.common.execution.pages;

import io.automation.common.helpers.WebHelper;
import io.automation.common.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Component
public class RunExecutionPage implements BasePage {

    @Autowired
    private WebHelper webHelper;


    public void verifyOnHomePage() {
        String homePageTitle = webHelper.getHomePageTitle();
        assertEquals("Dashboard - automation.io", homePageTitle);
    }

    public void verifyOnNavigationPage() {
        webHelper.getDriver().navigate().to("https://localhost:3000/#/robots/a8e265da-8aa5-4e45-8dfe-e30f184577e6/runs/0b1e7584-cc77-439a-b00b-0f85af93947b/edit/configuration");
        //Force a refresh to get to executions page
        webHelper.getDriver().navigate().refresh();
        webHelper.waitForElement(By.cssSelector("#content > section > h2 > span:nth-child(2)"));
        String configurationName = webHelper.readText(By.cssSelector("#content > section > h2 > span:nth-child(2)"));

        assertThat(configurationName, containsString("Webshop Demo"));
    }

    public void startAndVerifyRunningExecution() {
        webHelper.getDriver().findElement(By.linkText("Executions")).click();

        String executionsTabName = webHelper.readText(By.linkText("Executions"));
        assertEquals("Executions", executionsTabName);

        // Run execution
        webHelper.getDriver().findElement(By.cssSelector("#content > section > h2 > div > a:nth-child(3)")).click();

        // Verify running Execution
        String executionElement = "div.execution.body.no-hover.row.running.active:nth-of-type(2) > div.cell.progress";
        webHelper.waitForElement(By.cssSelector(executionElement));
        String executionState = webHelper.getDriver().findElement(By.cssSelector(executionElement)).getText();

        // give execution enough time to be laoded on page
        webHelper.waitForPageLoad();
        assertEquals("Running", executionState);
    }

    public void verifyExecutionCompletesSuccessfully() throws InterruptedException {
        String stateElement = "div.execution.body.no-hover.row.running.active:nth-of-type(2) > div.cell.progress";

        webHelper.waitForElement(By.cssSelector(stateElement));

        // period checking for execution completion
        while (webHelper.isPresent(stateElement)) {
            Thread.sleep(5000);
        }

        webHelper.waitForPageLoad();

        // verify execution run successfully
        State completedExecutionState = State.valueOf(webHelper.getDriver().findElement(By.cssSelector("div.execution.body.row.ok.done:nth-of-type(2) > div.cell.progress")).getText());
        assertEquals(State.Ok, completedExecutionState);
    }

    public void assertResultsAreExtracted() {
        // go to execution page
        webHelper.getDriver().findElement(By.cssSelector("div.execution.body.no-hover.row.ok.done:nth-of-type(2) > div.cell.actions > a:nth-child(2)")).click();
        webHelper.waitForPageLoad();

        //go to results page
        webHelper.getDriver().findElement(By.cssSelector("section > div > div.tab-pane.filled > div.tab-buttons > a:nth-child(2)")).click();
        webHelper.waitForPageLoad();

        // verify result count  is 7
        List<WebElement> resultElements = webHelper.getDriver().findElements(By.cssSelector("div.grid-pane:nth-of-type(1) div.grid-body div.row-container > div.row"));
        assertThat(resultElements.size(), equalTo(7));
    }

    enum State {
        Pending(false),
        Running(false),
        Ok(true),
        Failed(true);

        boolean complete;

        State(boolean complete) {
            this.complete = complete;
        }

        public boolean isComplete() {
            return this.complete;
        }
    }
}