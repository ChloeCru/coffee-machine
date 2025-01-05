package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WaterTankTest {

    private WaterTank waterTank;

    @BeforeEach
    void setUp() {
        waterTank = new WaterTank(50, 10, 100);
    }

    // Test pour vérifier l'initialisation correcte du réservoir d'eau
    @Test
    void testInitialVolume() {
        assertEquals(50, waterTank.getActualVolume(), "Le volume actuel devrait être 50.");
        assertEquals(100, waterTank.getMaxVolume(), "Le volume maximal devrait être 100.");
        assertEquals(10, waterTank.getMinVolume(), "Le volume minimal devrait être 10.");
    }

    // Test pour vérifier l'augmentation du volume
    @Test
    void testIncreaseVolume() {
        waterTank.increaseVolumeInTank(20);
        assertEquals(70, waterTank.getActualVolume(), "Le volume actuel devrait être augmenté à 70.");

        // Test avec l'ajout de volume à 0
        waterTank.increaseVolumeInTank(0);
        assertEquals(70, waterTank.getActualVolume(), "Le volume actuel ne devrait pas changer après l'ajout de 0.");

        // Test avec l'ajout de volume négatif
        waterTank.increaseVolumeInTank(-10);
        assertEquals(70, waterTank.getActualVolume(), "Le volume actuel ne devrait pas changer après l'ajout de -10.");
    }

    // Test pour vérifier la réduction du volume
    @Test
    void testDecreaseVolume() {
        waterTank.decreaseVolumeInTank(20);
        assertEquals(30, waterTank.getActualVolume(), "Le volume actuel devrait être réduit à 30.");

        // Test avec la réduction de volume à 0
        waterTank.decreaseVolumeInTank(0);
        assertEquals(30, waterTank.getActualVolume(), "Le volume actuel ne devrait pas changer après la réduction de 0.");

        // Test avec la réduction de volume négatif
        waterTank.decreaseVolumeInTank(-10);
        assertEquals(30, waterTank.getActualVolume(), "Le volume actuel ne devrait pas changer après la réduction de -10.");
    }

    // Test pour vérifier que le volume ne descend pas sous le volume minimal
    @Test
    void testDecreaseBelowMinVolume() {
        waterTank.decreaseVolumeInTank(45);

        // Vérifier que le volume ne descend pas sous le minimum
        assertNotEquals(5, waterTank.getActualVolume(), "Le volume actuel ne devrait pas descendre sous 10.");
        assertEquals(10, waterTank.getActualVolume(), "Le volume actuel devrait être bloqué à 10.");
    }

    // Test pour vérifier que le volume ne dépasse pas le volume maximal
    @Test
    void testIncreaseAboveMaxVolume() {
        waterTank.increaseVolumeInTank(60);

        // Vérifier que le volume ne dépasse pas le maximum
        assertNotEquals(110, waterTank.getActualVolume(), "Le volume actuel ne devrait pas dépasser 100.");
        assertEquals(100, waterTank.getActualVolume(), "Le volume actuel devrait être bloqué à 100.");
    }
}
