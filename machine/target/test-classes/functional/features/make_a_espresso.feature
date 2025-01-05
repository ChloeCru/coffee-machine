Feature: Make a coffee with an espresso coffee machine
  A user wants to make a coffee using an espresso machine

  Scenario: A user plugs the espresso coffee machine and makes an Moka coffee
    Given an espresso coffee machine with 0.10 l of min capacity, 3.0 l of max capacity, 600.0 l per h of water flow for the pump
    And a espresso "cup" with a capacity of 0.25
    When I plug the espresso machine to electricity
    And I add 1 l of water in the water tank of the espresso machine
    And I add 0.5 l of "MOKA" in the bean tank of the espresso machine
    And I make an espresso coffee "MOKA"
    Then the espresso coffee machine returns a coffee cup not empty
    And a espresso volume equals to 0.25
    And a espresso "cup" containing a coffee type "MOKA"
