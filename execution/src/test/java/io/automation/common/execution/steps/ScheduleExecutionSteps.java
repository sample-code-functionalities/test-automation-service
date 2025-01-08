package io.automation.common.execution.steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.automation.common.execution.pages.ScheduleExecutionPage;
import org.springframework.beans.factory.annotation.Autowired;


public class ScheduleExecutionSteps {

	@Autowired
	private ScheduleExecutionPage scheduleExecutionPage;

	@Given("I am logged into the UI")
	public void i_am_logged_into_the_ui() {
		scheduleExecutionPage.verifySystemIsLoggedIn();
	}


	@And("I navigate to the robot configuration page")
	public void i_navigate_to_the_configuration_page() {
		scheduleExecutionPage.verifyOnConfigurationPage();
	}

	@When("I schedule an execution")
	public void i_schedule_an_execution() {
		scheduleExecutionPage.scheduleAnExecution();
	}

	@Then("It should appear scheduled on the dashboard")
	public void execution_should_appear_scheduled_on_dashboard() throws InterruptedException {
		scheduleExecutionPage.assertExecutionIsScheduled();
	}

	@And("It should run at the scheduled time")
	public void execution_should_run_at_scheduled_time() throws InterruptedException {
		scheduleExecutionPage.verifyExecutionRunsAtScheduledTIme();
	}

	@Then("I can un-schedule the execution")
	public void unschedule_the_execution() {
		scheduleExecutionPage.successfullyUnScheduleExecution();
	}
}
