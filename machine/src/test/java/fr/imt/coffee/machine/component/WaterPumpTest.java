package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WaterPumpTest {

    private WaterPump waterPump;
    private WaterTank waterTank;

    @BeforeEach
    void setUp() {
        waterPump = new WaterPump(2.0); // Capacité de pompage de 2 L/s
        waterTank = mock(WaterTank.class); // Mock pour le réservoir d'eau
    }

    // Test pour vérifier le pompage avec un volume valide
    @Test
    void testPumpWaterSuccess() throws InterruptedException {
        when(waterTank.getActualVolume()).thenReturn(10.0);

        double waterVolume = 4.0; // Volume à pomper
        double expectedPumpingTime = (waterVolume / waterPump.getPumpingCapacity()) * 1000 * 2;

        // Simule un réservoir avec suffisamment d'eau
        doNothing().when(waterTank).decreaseVolumeInTank(waterVolume);

        double actualPumpingTime = waterPump.pumpWater(waterVolume, waterTank);

        assertEquals(expectedPumpingTime, actualPumpingTime, "Le temps de pompage devrait correspondre à la formule.");
        verify(waterTank).decreaseVolumeInTank(waterVolume);
    }

    // Test pour vérifier le pompage avec un volume supérieur à ce que contient le réservoir
    @Test
    void testPumpWaterNotEnoughWater() {
        when(waterTank.getActualVolume()).thenReturn(3.0); // Le réservoir contient 3 L

        double waterVolume = 5.0; // Volume à pomper

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            waterPump.pumpWater(waterVolume, waterTank);
        });

        String expectedMessage = "Pas assez d'eau dans le réservoir pour pomper.";
        assertTrue(exception.getMessage().contains(expectedMessage));
        verify(waterTank, never()).decreaseVolumeInTank(anyDouble());
    }

    // Test pour vérifier le pompage avec un volume négatif
    @Test
    void testPumpWaterNegativeVolume() {
        double waterVolume = -1.0;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            waterPump.pumpWater(waterVolume, waterTank);
        });

        String expectedMessage = "Le volume d'eau à pomper doit être positif.";
        assertTrue(exception.getMessage().contains(expectedMessage));
        verify(waterTank, never()).decreaseVolumeInTank(anyDouble());
    }

    // Test pour vérifier le débit de la pompe
    @Test
    void testGetPumpingCapacity() {
        assertEquals(2.0, waterPump.getPumpingCapacity(), "La capacité de pompage devrait être 2.0 L/s.");
    }
}
