Feature: Run an action Feature

  Scenario: Successfully run an action
    Given I am logged into the ui
    And I navigate to the configuration page
    When I run an execution
    Then It should run successfully
    And It should extract results