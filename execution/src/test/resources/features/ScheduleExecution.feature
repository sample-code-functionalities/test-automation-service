Feature: Schedule an action Feature

  Scenario: Successfully schedule an action
    Given I am logged into the UI
    And I navigate to the robot configuration page
    When I schedule an execution
    Then It should appear scheduled on the dashboard
    And It should run at the scheduled time
    Then I can un-schedule the execution