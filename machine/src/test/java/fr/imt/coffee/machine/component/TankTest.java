package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TankTest {

    private Tank tank;

    @BeforeEach
    void setUp() {
        tank = new Tank(50,10, 100);
    }

    // Test pour vérifier l'initialisation correcte du réservoir
    @Test
    void testInitialVolume() {
        assertEquals(50, tank.getActualVolume(), "Le volume actuel devrait être 50.");
        assertEquals(100, tank.getMaxVolume(), "Le volume maximal devrait être 100.");
        assertEquals(10, tank.getMinVolume(), "Le volume minimal devrait être 10.");
    }

    // Test pour vérifier l'augmentation du volume
    @Test
    void testIncreaseVolume() {
        tank.increaseVolumeInTank(20);
        assertEquals(70, tank.getActualVolume(), "Le volume actuel devrait être augmenté à 70.");

        // Test avec l'ajout de volume à 0
        tank.increaseVolumeInTank(0);
        assertEquals(70, tank.getActualVolume(), "Le volume actuel ne devrait pas changer après l'ajout de 0.");

        //test avec l'ajout de volume négatif
        tank.increaseVolumeInTank(-10);
        assertEquals(70, tank.getActualVolume(), "Le volume actuel ne devrait pas changer après l'ajout de -10.");
    }

    // Test pour vérifier la réduction du volume
    @Test
    void testDecreaseVolume() {
        tank.decreaseVolumeInTank(20);
        assertEquals(30, tank.getActualVolume(), "Le volume actuel devrait être réduit à 30.");

        // Test avec la réduction de volume à 0
        tank.decreaseVolumeInTank(0);
        assertEquals(30, tank.getActualVolume(), "Le volume actuel ne devrait pas changer après la réduction de 0.");

        // Test avec la réduction de volume négatif
        tank.decreaseVolumeInTank(-10);
        assertEquals(30, tank.getActualVolume(), "Le volume actuel ne devrait pas changer après la réduction de -10.");
    }

    // Test pour vérifier que le volume ne descend pas sous le volume minimal
    @Test
    void testDecreaseBelowMinVolume() {
        // Essayer de réduire le volume en dessous du minimum
        tank.decreaseVolumeInTank(45);
        //vérifie que le volume n'est pas égale à 5
        assertNotEquals(5, tank.getActualVolume(), "Le volume actuel ne devrait pas descendre sous 10.");
        assertEquals(10, tank.getActualVolume(), "Le volume actuel devrait être bloqué 10.");
    }

    // Test pour vérifier que le volume ne dépasse pas le volume maximal
    @Test
    void testIncreaseAboveMaxVolume() {
        // Essayer d'augmenter le volume au-dessus du maximum
        tank.increaseVolumeInTank(60);
        //vérifie que le volume n'est pas égale à 110
        assertNotEquals(110, tank.getActualVolume(), "Le volume actuel ne devrait pas dépasser 100.");
        assertEquals(100, tank.getActualVolume(), "Le volume actuel devrait être bloqué 100.");
    }
}