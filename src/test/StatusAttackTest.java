package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusAttackTest {
    private StatusAttack attack;
    private Pokemon attacker;
    private Pokemon defender;

    @BeforeEach
    void setUp() {
        attack = new StatusAttack("Defensa férrea", "Acero", 0, 5, 100, AttributeType.PHYSICAL_DEFENSE, 3, "Status");
        attacker = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        defender = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
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
    void testCalcDañoReturnsZeroAndReducesPP() {
        attacker.addAttack(attack);
        defender.addAttack(attack);
        int initialPP = attack.getPowerPoint();
        int damage = attack.calcDaño(attacker, defender);
        assertEquals(0, damage);
        assertEquals(initialPP - 1, attack.getPowerPoint());
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
        StatusAttack lowPrecision = new StatusAttack("Fallo estado", "Acero", 0, 5, 0, AttributeType.PHYSICAL_DEFENSE, 3, "Status");
        attacker.addAttack(lowPrecision);
        defender.addAttack(lowPrecision);
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += lowPrecision.calcDaño(attacker, defender);
        }
        assertEquals(0, total);
    }

    @Test
    void testGetAffectsReturnsCorrectAttributeType() {
        assertEquals(AttributeType.PHYSICAL_DEFENSE, attack.getAffects());
    }

    @Test
    void testGetEffectReturnsAttributeName() {
        assertEquals(AttributeType.PHYSICAL_DEFENSE.name(), attack.getEffect());
    }
}