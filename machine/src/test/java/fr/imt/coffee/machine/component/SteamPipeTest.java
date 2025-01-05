package fr.imt.coffee.machine.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SteamPipeTest {

    private SteamPipe steamPipe;

    @BeforeEach
    void setUp() {
        steamPipe = new SteamPipe();
    }

    // Test pour vérifier l'état initial de la buse de vapeur
    @Test
    void testInitialState() {
        assertFalse(steamPipe.isOn(), "La buse de vapeur devrait être éteinte au démarrage.");
    }

    // Test pour vérifier que la buse peut être allumée
    @Test
    void testSetOn() {
        steamPipe.setOn();
        assertTrue(steamPipe.isOn(), "La buse de vapeur devrait être allumée après appel à setOn.");
    }

    // Test pour vérifier que la buse peut être éteinte
    @Test
    void testSetOff() {
        steamPipe.setOn();
        steamPipe.setOff();
        assertFalse(steamPipe.isOn(), "La buse de vapeur devrait être éteinte après appel à setOff.");
    }

    // Test pour vérifier que l'état ne change pas si on appelle deux fois setOn
    @Test
    void testSetOnTwice() {
        steamPipe.setOn();
        steamPipe.setOn();
        assertTrue(steamPipe.isOn(), "La buse de vapeur devrait rester allumée après un double appel à setOn.");
    }

    // Test pour vérifier que l'état ne change pas si on appelle deux fois setOff
    @Test
    void testSetOffTwice() {
        steamPipe.setOff();
        steamPipe.setOff();
        assertFalse(steamPipe.isOn(), "La buse de vapeur devrait rester éteinte après un double appel à setOff.");
    }
}
