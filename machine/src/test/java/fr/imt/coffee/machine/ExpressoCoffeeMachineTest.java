package fr.imt.coffee.machine;

import fr.imt.coffee.machine.exception.*;
import fr.imt.coffee.storage.type.CoffeeType;
import fr.imt.coffee.storage.container.*;
import fr.imt.coffee.storage.exception.CupNotEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.util.Random;


public class ExpressoCoffeeMachineTest {
    public EspressoCoffeeMachine espressoMachineUnderTest;

    @BeforeEach
    public void beforeTest() {
        espressoMachineUnderTest = new EspressoCoffeeMachine(
                0, 10,  // Min et max du réservoir de grains
                0, 10,  // Min et max du réservoir d'eau
                700     // Capacité de pompage
        );
    }

    @Test
    public void testMakeCoffeeWithCorrectCoffeeType() throws Exception {
        // Mock d'un contenant (Cup)
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        //On créé un mock de l'objet random
        Random randomMock = Mockito.mock(Random.class, Mockito.withSettings().withoutAnnotations());
        //On vient ensuite stubber la méthode nextGaussian pour pouvoir controler la valeur retournée
        //ici on veut qu'elle retourne 0.6
        Mockito.when(randomMock.nextGaussian()).thenReturn(0.6);
        //On injecte ensuite le mock créé dans la machine à café
        espressoMachineUnderTest.setRandomGenerator(randomMock);

        // Préparation de la machine
        espressoMachineUnderTest.addWaterInTank(1); // Ajouter de l'eau
        espressoMachineUnderTest.getBeanTank().increaseCoffeeVolumeInTank(5, CoffeeType.ARABICA); // Ajouter des grains de type ARABICA
        espressoMachineUnderTest.plugToElectricalPlug(); // Brancher la machine

        // Faire un café
        CoffeeContainer preparedCoffee = espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);

        // Vérifications
        Assertions.assertFalse(preparedCoffee.isEmpty());
        Assertions.assertEquals(mockCup.getCapacity(), preparedCoffee.getCapacity());
        Assertions.assertEquals(CoffeeType.ARABICA, preparedCoffee.getCoffeeType());
    }

    @Test
    public void testMakeCoffeeWithMismatchedCoffeeTypeThrowsException() {
        // Mock d'un contenant (Cup)
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        // Préparation de la machine
        espressoMachineUnderTest.addWaterInTank(1); // Ajouter de l'eau
        espressoMachineUnderTest.getBeanTank().increaseCoffeeVolumeInTank(5, CoffeeType.ARABICA); // Ajouter des grains de type ARABICA
        espressoMachineUnderTest.plugToElectricalPlug(); // Brancher la machine

        // Tenter de préparer un café de type MOKA
        Assertions.assertThrows(CoffeeTypeCupDifferentOfCoffeeTypeTankException.class, () -> {
            espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.MOKA);
        });
    }

    @Test
    public void testMakeCoffeeWithEmptyCupThrowsException() {
        // Mock d'un contenant (Cup)
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(0.25);
        Mockito.when(mockCup.isEmpty()).thenReturn(false); // La tasse n'est pas vide

        // Préparation de la machine
        espressoMachineUnderTest.addWaterInTank(1); // Ajouter de l'eau
        espressoMachineUnderTest.getBeanTank().increaseCoffeeVolumeInTank(5, CoffeeType.ARABICA); // Ajouter des grains de type ARABICA
        espressoMachineUnderTest.plugToElectricalPlug(); // Brancher la machine

        // Tenter de préparer un café avec une tasse non vide
        Assertions.assertThrows(CupNotEmptyException.class, () -> {
            espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }

    @Test
    public void testMakeCoffeeWithInsufficientWaterThrowsException() {
        // Mock d'un contenant (Cup)
        Cup mockCup = Mockito.mock(Cup.class);
        Mockito.when(mockCup.getCapacity()).thenReturn(1.5); // Capacité supérieure à l'eau disponible
        Mockito.when(mockCup.isEmpty()).thenReturn(true);

        // Préparation de la machine
        espressoMachineUnderTest.addWaterInTank(1); // Ajouter 1L d'eau seulement
        espressoMachineUnderTest.getBeanTank().increaseCoffeeVolumeInTank(5, CoffeeType.ARABICA); // Ajouter des grains de type ARABICA
        espressoMachineUnderTest.plugToElectricalPlug(); // Brancher la machine

        // Tenter de préparer un café sans suffisamment d'eau
        Assertions.assertThrows(LackOfWaterInTankException.class, () -> {
            espressoMachineUnderTest.makeACoffee(mockCup, CoffeeType.ARABICA);
        });
    }

    @AfterEach
    public void afterTest(){
        // Code de nettoyage après chaque test, si nécessaire.
    }

}
