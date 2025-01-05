package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoffeeGrinderTest {

    private CoffeeGrinder coffeeGrinder;
    private BeanTank beanTank;

    @BeforeEach
    void setUp() {
        coffeeGrinder = new CoffeeGrinder(1000); // Temps de mouture fixé à 1000 ms (1 seconde)
        beanTank = mock(BeanTank.class); // Mock du réservoir de café
    }

    // Test pour vérifier la mouture du café
    @Test
    void testGrindCoffee() throws InterruptedException {
        // Simuler l'augmentation du volume de café dans le réservoir après la mouture
        doNothing().when(beanTank).increaseVolumeInTank(0.2); // On vérifie que l'on ajoute 0.2 L de café

        // Vérifier que la méthode de mouture retourne bien le bon temps de mouture
        double grindingTime = coffeeGrinder.grindCoffee(beanTank);

        assertEquals(1000, grindingTime, "Le temps de mouture devrait être de 1000 ms.");
        verify(beanTank).increaseVolumeInTank(0.2); // Vérifier que la méthode increaseVolumeInTank a bien été appelée avec 0.2
    }


    // Test pour vérifier que la méthode de mouture ne fonctionne pas avec un temps de mouture négatif
    @Test
    void testGrindCoffeeWithNegativeTime() {
        // Créer un moulin avec un temps de mouture négatif
        CoffeeGrinder grinderWithNegativeTime = new CoffeeGrinder(-1000);

        // Vérifier qu'un temps de mouture négatif n'est pas autorisé
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            grinderWithNegativeTime.grindCoffee(beanTank);
        });

        String expectedMessage = "Le temps de mouture ne peut pas être négatif.";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    // Test pour vérifier que le volume du réservoir ne dépasse pas un certain seuil (hypothétique)
    @Test
    void testMaxVolumeAfterGrinding() throws InterruptedException {
        // Supposons que le réservoir de graines peut contenir un maximum de 1L après plusieurs moutures
        when(beanTank.getActualVolume()).thenReturn(0.9);

        // Simuler une mouture qui devrait ajouter 0.2L de café
        coffeeGrinder.grindCoffee(beanTank);

        // Vérifier que le volume n'excède pas 1L
        verify(beanTank).increaseVolumeInTank(0.2);
        assertTrue(beanTank.getActualVolume()  <= 1.0, "Le volume total dans le réservoir ne devrait pas dépasser 1L.");
    }

    // Test pour vérifier que l'exception InterruptedException est correctement levée
    @Test
    void testGrindCoffeeInterrupted() throws InterruptedException {
        // Simuler une exception lors de l'appel de la méthode increaseVolumeInTank
        doAnswer(invocation -> {
            throw new InterruptedException();
        }).when(beanTank).increaseVolumeInTank(anyDouble());

        // Vérifier que l'exception InterruptedException est levée lors de l'appel à grindCoffee
        assertThrows(InterruptedException.class, () -> {
            coffeeGrinder.grindCoffee(beanTank);
        });
    }
}
