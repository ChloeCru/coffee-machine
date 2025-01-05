package fr.imt.coffee.machine.component;

import fr.imt.coffee.storage.type.CoffeeType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeanTankTest {

    private BeanTank beanTank;

    @BeforeEach
    void setUp() {
        beanTank = new BeanTank(50, 10, 100, CoffeeType.ARABICA);
    }

    // Test pour vérifier l'initialisation correcte avec un type de café
    @Test
    void testInitialBeanTank() {
        assertEquals(50, beanTank.getActualVolume(), "Le volume actuel devrait être 50.");
        assertEquals(10, beanTank.getMinVolume(), "Le volume minimal devrait être 10.");
        assertEquals(100, beanTank.getMaxVolume(), "Le volume maximal devrait être 100.");
        assertEquals(CoffeeType.ARABICA, beanTank.getBeanCoffeeType(), "Le type de café initial devrait être ARABICA.");
    }

    // Test avec tous les types de café définis dans CoffeeType
    @Test
    void testBeanCoffeeTypeVariations() {
        for (CoffeeType coffeeType : CoffeeType.values()) {
            beanTank.setBeanCoffeeType(coffeeType);
            assertEquals(coffeeType, beanTank.getBeanCoffeeType(),
                    "Le type de café devrait être correctement mis à jour à " + coffeeType);
        }
    }

    // Test pour vérifier la méthode increaseCoffeeVolumeInTank avec différents types de café
    @Test
    void testIncreaseCoffeeVolumeInTank() {
        beanTank.increaseCoffeeVolumeInTank(20, CoffeeType.ROBUSTA);
        assertEquals(70, beanTank.getActualVolume(), "Le volume actuel devrait être 70 après augmentation.");
        assertEquals(CoffeeType.ROBUSTA, beanTank.getBeanCoffeeType(), "Le type de café devrait être mis à jour à ROBUSTA.");
    }

    // Test pour vérifier les limites des volumes avec changement de type de café
    @Test
    void testIncreaseAboveMaxVolumeWithCoffeeTypeChange() {
        beanTank.increaseCoffeeVolumeInTank(60, CoffeeType.BAHIA);
        assertEquals(100, beanTank.getActualVolume(), "Le volume actuel devrait être bloqué à 100.");
        assertEquals(CoffeeType.BAHIA, beanTank.getBeanCoffeeType(), "Le type de café devrait être mis à jour à BAHIA.");
    }

    // Test pour vérifier le comportement avec les volumes négatifs
    @Test
    void testIncreaseCoffeeVolumeNegative() {
        beanTank.increaseCoffeeVolumeInTank(-10, CoffeeType.MOKA);
        assertEquals(50, beanTank.getActualVolume(), "Le volume actuel ne devrait pas changer après une augmentation négative.");
        assertEquals(CoffeeType.MOKA, beanTank.getBeanCoffeeType(), "Le type de café devrait être mis à jour à MOKA.");
    }

    // Test pour les types de café non modifiés après un ajout de 0 volume
    @Test
    void testNoChangeInVolumeWithZeroAddition() {
        beanTank.increaseCoffeeVolumeInTank(0, CoffeeType.ARABICA_CREMA);
        assertEquals(50, beanTank.getActualVolume(), "Le volume actuel ne devrait pas changer après une augmentation de 0.");
        assertEquals(CoffeeType.ARABICA_CREMA, beanTank.getBeanCoffeeType(), "Le type de café devrait être mis à jour à ARABICA_CREMA.");
    }
}
