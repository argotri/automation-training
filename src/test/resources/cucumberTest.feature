Feature: Test for wonderful Quote

  Scenario: Add quote and delete quote
    Given user open wonderful quote
    When user add "Hello world" quote with author "Argo Triwidodo"
    Then user should able to see "Hello world" quote and the number should be "6"
    When user click on quote "Hello world" panel
    Then user should not see "Hello world" quote