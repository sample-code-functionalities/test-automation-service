package io.automation.common.execution.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.automation.common.execution.pages.RunExecutionPage;
import org.springframework.beans.factory.annotation.Autowired;


public class RunExecutionSteps {

    @Autowired
    private RunExecutionPage executionPage;

    @Given("I am logged into the ui")
    public void i_am_logged_into_the_ui() {
        executionPage.verifyOnHomePage();
    }


    @And("I navigate to the configuration page") // use execution url to go to execution
    public void i_navigate_to_the_configuration_page() {
        executionPage.verifyOnNavigationPage();
    }

    @When("I run an execution")
    public void i_run_an_execution() {
        executionPage.startAndVerifyRunningExecution();
    }

    @Then("It should run successfully")
    public void execution_should_run_successfully() throws InterruptedException {
        executionPage.verifyExecutionCompletesSuccessfully();
    }

    @And("It should extract results")
    public void execution_should_extract_results() {
        executionPage.assertResultsAreExtracted();
    }
}
