Feature: Login Feature

  @negative
  Scenario Outline: Invalid credentials Login
    Given I am on the login page
    When I try to login with "<username>" and "<password>"
    Then I verify invalid login message
    Examples:
      | username              | password |
      | invalid_email@automated.io | password |
      | test@login.automated.io    | 12345678 |

  @negative
  Scenario Outline: Empty Credentials Login
    Given I am on the login page
    When I try to login with "<username>" and "<password>"
    Then I verify invalid empty credentials
    Examples:
      | username | password |
      |          |          |


  Scenario Outline: Valid Login
    Given I am on the login page
    When I try to login with "<username>" and "<password>"
    Then I should be logged into the homepage
    Examples:
      | username        | password    |
      | system@automated.io  | systemsecret321  |