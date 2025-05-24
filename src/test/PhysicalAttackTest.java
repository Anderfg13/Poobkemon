package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PhysicalAttackTest {
    private PhysicalAttack attack;
    private Pokemon attacker;
    private Pokemon defender;

    @BeforeEach
    void setUp() {
        attack = new PhysicalAttack("Puño meteoro", "Acero", 100, 5, 90, "Physical");
        attacker = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        defender = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
    }

    @Test
    void testGetters() {
        assertEquals("Puño meteoro", attack.getName());
        assertEquals("Acero", attack.getType());
        assertEquals(100, attack.getBaseDamage());
        assertEquals(5, attack.getPowerPoint());
        assertEquals(90, attack.getPrecision());
        assertEquals("Physical", attack.getAttackType());
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
        PhysicalAttack clone = (PhysicalAttack) attack.clone();
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
        PhysicalAttack lowPrecision = new PhysicalAttack("Golpe fallido", "Acero", 100, 5, 0, "Physical");
        attacker.addAttack(lowPrecision);
        defender.addAttack(lowPrecision);
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += lowPrecision.calcDaño(attacker, defender);
        }
        assertEquals(0, total);
    }
}