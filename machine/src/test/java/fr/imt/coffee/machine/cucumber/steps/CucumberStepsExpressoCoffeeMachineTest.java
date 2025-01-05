package fr.imt.coffee.machine.cucumber.steps;

import fr.imt.coffee.machine.EspressoCoffeeMachine;
import fr.imt.coffee.machine.exception.CannotMakeCremaWithSimpleCoffeeMachine;
import fr.imt.coffee.machine.exception.CoffeeTypeCupDifferentOfCoffeeTypeTankException;
import fr.imt.coffee.machine.exception.LackOfWaterInTankException;
import fr.imt.coffee.machine.exception.MachineNotPluggedException;
import fr.imt.coffee.storage.type.CoffeeType;
import fr.imt.coffee.storage.container.*;
import fr.imt.coffee.storage.exception.CupNotEmptyException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;


public class CucumberStepsExpressoCoffeeMachineTest {
    private EspressoCoffeeMachine espressoCoffeeMachine;
    private Mug mug;
    private Cup cup;
    private CoffeeContainer containerWithCoffee;

    @Given("an espresso coffee machine with {double} l of min capacity, {double} l of max capacity, {double} l per h of water flow for the pump")
    public void givenAnEspressoCoffeeMachine(double minimalWaterCapacity, double maximalWaterCapacity, double pumpWaterFlow) {
        espressoCoffeeMachine = new EspressoCoffeeMachine(minimalWaterCapacity, maximalWaterCapacity, minimalWaterCapacity, maximalWaterCapacity, pumpWaterFlow);
    }

    @And("a espresso {string} with a capacity of {double}")
    public void aWithACapacityOf(String containerType, double containerCapacity) {
        if ("mug".equals(containerType))
            mug = new Mug(containerCapacity);
        if ("cup".equals(containerType))
            cup = new Cup(containerCapacity);
    }

    @When("I plug the espresso machine to electricity")
    public void iPlugTheEspressoMachineToElectricity() {
        espressoCoffeeMachine.plugToElectricalPlug();
    }

    @And("I add {double} l of water in the water tank of the espresso machine")
    public void iAddLitersOfWater(double waterVolume) {
        espressoCoffeeMachine.addWaterInTank(waterVolume);
    }

    @And("I add {double} l of {string} in the bean tank of the espresso machine")
    public void iAddLitersOfCoffeeBeans(double beanVolume, String coffeeType) {
        espressoCoffeeMachine.addCoffeeInBeanTank(beanVolume, CoffeeType.valueOf(coffeeType));
    }

    @And("I make an espresso coffee {string}")
    public void iMakeAnEspressoCoffee(String coffeeType) throws InterruptedException, CupNotEmptyException, LackOfWaterInTankException, MachineNotPluggedException, CoffeeTypeCupDifferentOfCoffeeTypeTankException, CannotMakeCremaWithSimpleCoffeeMachine {
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir controler la valeur retournée
        //ici on veut qu'elle retourne 0.6
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        //On injecte ensuite le mock créé dans la machine à café
        espressoCoffeeMachine.setRandomGenerator(randomMock);

        if (mug != null)
            containerWithCoffee = espressoCoffeeMachine.makeACoffee(mug, CoffeeType.valueOf(coffeeType));
        if (cup != null)
            containerWithCoffee = espressoCoffeeMachine.makeACoffee(cup, CoffeeType.valueOf(coffeeType));

    }

    @Then("the espresso coffee machine returns a coffee cup not empty")
    public void theEspressoCoffeeMachineReturnsACoffeeMugNotEmpty() {
        Assertions.assertFalse(containerWithCoffee.isEmpty());
    }

    @And("a espresso volume equals to {double}")
    public void aEspressoVolumeEqualsTo(double coffeeVolume) {
        assertThat(coffeeVolume, is(containerWithCoffee.getCapacity()));
    }

    @And("a espresso {string} containing a coffee type {string}")
    public void aEspressoContainingACoffeeType(String containerType, String coffeeType) {
        if ("mug".equals(containerType))
            assertThat(containerWithCoffee, instanceOf(CoffeeMug.class));
        if ("cup".equals(containerType))
            assertThat(containerWithCoffee, instanceOf(CoffeeCup.class));

        assertThat(containerWithCoffee.getCoffeeType(), is(CoffeeType.valueOf(coffeeType)));
    }
}
