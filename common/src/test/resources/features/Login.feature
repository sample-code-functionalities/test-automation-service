Feature: Login Feature

  Scenario Outline: Valid Login
    Given I am on the login page
    When I try to login with "<username>" and "<password>"
    Then I should be logged into the homepage
    Examples:
      | username        | password    |
      | system@automation.io  | keepsecret  |