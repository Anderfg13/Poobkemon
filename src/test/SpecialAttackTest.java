package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SpecialAttackTest {
    private SpecialAttack attack;
    private Pokemon attacker;
    private Pokemon defender;

    @BeforeEach
    void setUp() {
        attack = new SpecialAttack("Rayo carga", "Electrico", 50, 5, 90, "Special");
        attacker = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        defender = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
    }

    @Test
    void testGetters() {
        assertEquals("Rayo carga", attack.getName());
        assertEquals("Electrico", attack.getType());
        assertEquals(50, attack.getBaseDamage());
        assertEquals(5, attack.getPowerPoint());
        assertEquals(90, attack.getPrecision());
        assertEquals("Special", attack.getAttackType());
        assertNull(attack.getEffect());
    }

    @Test
    void testSetPowerPointAndUsarAtaque() {
        attack.setPowerPoint(2);
        assertEquals(2, attack.getPowerPoint());
        attack.usarAtaque();
        assertEquals(1, attack.getPowerPoint());
        attack.usarAtaque();
        assertEquals(0, attack.getPowerPoint());
        attack.usarAtaque();
        assertEquals(0, attack.getPowerPoint());
    }

    @Test
    void testCloneCreatesEqualButDistinctObject() {
        SpecialAttack clone = (SpecialAttack) attack.clone();
        assertNotSame(attack, clone);
        assertEquals(attack.getName(), clone.getName());
        assertEquals(attack.getType(), clone.getType());
        assertEquals(attack.getBaseDamage(), clone.getBaseDamage());
    }

    @Test
    void testCalcDañoReturnsZeroIfNoPP() {
        attacker.addAttack(attack);
        defender.addAttack(attack);
        attack.setPowerPoint(0);
        int damage = attack.calcDaño(attacker, defender);
        assertEquals(0, damage);
    }

    @Test
    void testCalcDañoReturnsZeroIfMisses() {
        SpecialAttack lowPrecision = new SpecialAttack("Fallo especial", "Electrico", 50, 5, 0, "Special");
        attacker.addAttack(lowPrecision);
        defender.addAttack(lowPrecision);
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += lowPrecision.calcDaño(attacker, defender);
        }
        assertEquals(0, total);
    }
}