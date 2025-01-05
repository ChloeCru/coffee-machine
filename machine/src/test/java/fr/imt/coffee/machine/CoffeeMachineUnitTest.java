package fr.imt.coffee.machine;

import fr.imt.coffee.machine.exception.CannotMakeCremaWithSimpleCoffeeMachine;
import fr.imt.coffee.machine.exception.CoffeeTypeCupDifferentOfCoffeeTypeTankException;
import fr.imt.coffee.machine.exception.LackOfWaterInTankException;
import fr.imt.coffee.machine.exception.MachineNotPluggedException;
import fr.imt.coffee.storage.type.CoffeeType;
import fr.imt.coffee.storage.container.CoffeeContainer;
import fr.imt.coffee.storage.container.Cup;
import fr.imt.coffee.storage.exception.CupNotEmptyException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class CoffeeMachineUnitTest {
    public CoffeeMachine coffeeMachineUnderTest;

    /**
     * @BeforeEach est une annotation permettant d'exécuter la méthode annotée avant chaque test unitaire
     * Ici avant chaque test on initialise la machine à café
     */
    @BeforeEach
    public void beforeTest(){
        coffeeMachineUnderTest = new CoffeeMachine(
                0,10,
                0,10,  700);
    }

    /**
     * On vient tester si la machine ne se met pas en défaut
     */
    @Test
    public void testMachineFailureTrue(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 1.0
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(1.0);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocké l'objet random donc la valeur retournée par nextGaussian() sera 1
        //La machine doit donc se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertTrue(coffeeMachineUnderTest.isOutOfOrder());
        assertThat(true, is(coffeeMachineUnderTest.isOutOfOrder()));
    }

    /**
     * On vient tester si la machine se met en défaut
     */
    @Test
    public void testMachineFailureFalse(){
        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir contrôler la valeur retournée
        //ici on veut qu'elle retourne 0.6
        //when : permet de définir quand sur quelle méthode établir le stub
        //thenReturn : va permettre de contrôler la valeur retournée par le stub
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        //On injecte ensuite le mock créé dans la machine à café
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        //On vérifie que le booleen outOfOrder est bien à faux avant d'appeler la méthode
        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));

        //on appelle la méthode qui met la machine en défaut
        //On a mocker l'objet random donc la valeur retournée par nextGaussian() sera 0.6
        //La machine doit donc NE PAS se mettre en défaut
        coffeeMachineUnderTest.coffeeMachineFailure();

        Assertions.assertFalse(coffeeMachineUnderTest.isOutOfOrder());
        //Ou avec Hamcrest
        assertThat(false, is(coffeeMachineUnderTest.isOutOfOrder()));
    }

    /**
     * On test que la machine se branche correctement au réseau électrique
     */
    @Test
    public void testPlugMachine(){
        Assertions.assertFalse(coffeeMachineUnderTest.isPlugged());

        coffeeMachineUnderTest.plugToElectricalPlug();

        Assertions.assertTrue(coffeeMachineUnderTest.isPlugged());
    }

    /**
     * On test qu'une exception est bien levée lorsque que le cup passé en paramètre retourne qu'il n'est pas vide
     * Tout comme le test sur la mise en défaut afin d'avoir un comportement isolé et indépendant de la machine
     * on vient ici mocker un objet Cup afin d'en maitriser complétement son comportement
     * On ne compte pas sur "le bon fonctionnement de la méthode"
     */
    @Test
    public void testMakeACoffeeCupNotEmptyException(){
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.isEmpty()).thenReturn(false);

        coffeeMachineUnderTest.plugToElectricalPlug();

        //assertThrows( [Exception class expected], [lambda expression with the method that throws an exception], [exception message expected])
        //AssertThrows va permettre de venir tester la levée d'une exception, ici lorsque que le contenant passé en
        //paramètre n'est pas vide
        //On teste à la fois le type d'exception levée mais aussi le message de l'exception
        Assertions.assertThrows(CupNotEmptyException.class, ()->{
                coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
            });
    }



    @Test
    public void testCoffeeTypeMismatchException() {
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);  // La tasse a une capacité de 0,25L
        Mockito.when(mockCup.isEmpty()).thenReturn(true);     // La tasse est vide

        coffeeMachineUnderTest.plugToElectricalPlug(); // La machine est branchée


        // Assume the bean tank is set for ARABICA coffee
        coffeeMachineUnderTest.addCoffeeInBeanTank(5, CoffeeType.ARABICA); // Le réservoir de grains est rempli de café ARABICA
        coffeeMachineUnderTest.addWaterInTank(1); // Ajout d'eau suffisante pour faire le café (1L)


        // On vérifie que l'exception CoffeeTypeCupDifferentOfCoffeeTypeTankException est levée si on tente de faire un café de type MOKA
        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA); // Demande un café de type MOKA alors que le réservoir contient du café ARABICA
        });
    }

    @Test
    public void testLackOfWaterInTankException() {
        // Mock a cup with a capacity that exceeds the current water in the tank
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.5);  // La tasse a une capacité de 1,5L
        Mockito.when(mockCup.isEmpty()).thenReturn(true);     // La tasse est vide


        coffeeMachineUnderTest.plugToElectricalPlug(); // La machine est branchée

        coffeeMachineUnderTest.addWaterInTank(1); // Ajout de seulement 1L d'eau dans le réservoir

        // On vérifie que l'exception LackOfWaterInTankException est levée si on essaie de faire du café
        Assertions.assertThrows(LackOfWaterInTankException.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);  // Tente de faire du café MOKA
        });
    }

    @Test
    public void testNbCoffeeMadeIncrement() throws LackOfWaterInTankException, InterruptedException, MachineNotPluggedException, CupNotEmptyException, CoffeeTypeCupDifferentOfCoffeeTypeTankException, CannotMakeCremaWithSimpleCoffeeMachine {
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        coffeeMachineUnderTest.addWaterInTank(1);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5, CoffeeType.ARABICA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        int initialCoffeeCount = coffeeMachineUnderTest.getNbCoffeeMade();

        coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);

        Assertions.assertEquals(initialCoffeeCount + 1, coffeeMachineUnderTest.getNbCoffeeMade());
    }

    @Test
    public void testCannotMakeCremaWithSimpleCoffeeMachine() {
        // Mock a Cup instance
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        // Add water and coffee beans of type ARABICA_CREMA
        coffeeMachineUnderTest.addWaterInTank(1);
        coffeeMachineUnderTest.addCoffeeInBeanTank(5, CoffeeType.ARABICA_CREMA);
        coffeeMachineUnderTest.plugToElectricalPlug();

        // Assert that CannotMakeCremaWithSimpleCoffeeMachine is thrown
        Assertions.assertThrows(CannotMakeCremaWithSimpleCoffeeMachine.class, () -> {
            coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA_CREMA);
        });
    }

    @Test
    public void testCoffeeContainerCapacityMatchesInputContainer() throws InterruptedException {
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        coffeeMachineUnderTest.setRandomGenerator(randomMock);

        // Mock d'un contenant (Cup) avec une capacité définie
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        // Préparation de la machine
        coffeeMachineUnderTest.addWaterInTank(1); // Ajouter de l'eau
        coffeeMachineUnderTest.addCoffeeInBeanTank(5, CoffeeType.ARABICA); // Ajouter des grains de café
        coffeeMachineUnderTest.plugToElectricalPlug(); // Brancher la machine

        try {
            // Faire un café
            CoffeeContainer preparedCoffee = coffeeMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);

            // Vérification : La capacité du contenant préparé doit correspondre à celle du contenant d'entrée
            Assertions.assertEquals(mockCup.getCapacity(), preparedCoffee.getCapacity(),
                    "The capacity of the coffee container does not match the input container's capacity.");
        } catch (Exception e) {
            Assertions.fail("An exception was thrown while making coffee: " + e.getMessage());
        }
    }

    @AfterEach
    public void afterTest(){

    }
}
