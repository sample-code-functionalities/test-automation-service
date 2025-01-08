package io.automation.common.login.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.automation.common.login.pages.LoginPage;
import org.springframework.beans.factory.annotation.Autowired;


public class LoginSteps {

	@Autowired
	private LoginPage page;

	@Given("^I am on the login page$")
	public void i_am_on_the_login_page() {
		page.goToLoginPage();
	}

	@When("I try to login with {string} and {string}")
	public void i_try_to_login_with(String username, String password) {
		page.loginInToTheUI(username, password);
	}

	@Then("I verify invalid login message")
	public void i_verify_that_i_invalid_login() {
		page.verifyInvalidLogin();
	}

	@Then("I verify invalid empty credentials")
	public void i_verify_empty_credentails_login() {
		page.verifyInvalidLoginWithEmptyCredentials();
	}

	@Then("I should be logged into the homepage")
	public void i_verify_that_i_can_login() {
		page.verifySuccessfulLogin();
	}
}
