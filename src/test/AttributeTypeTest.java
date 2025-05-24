package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AttributeTypeTest {
    private Pokemon raichu;

    @BeforeEach
    void setUp() {
        raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
    }

    @Test
    void testHPApplyHealsButNotOverMax() {
        raichu.setPs(10);
        AttributeType.HP.apply(raichu, 20);
        assertEquals(30, raichu.getPs());
        AttributeType.HP.apply(raichu, 20); // No debe pasar el total
        assertEquals(raichu.getTotalPs(), raichu.getPs());
    }

    @Test
    void testHPApplyDoesNotWorkIfFainted() {
        raichu.setPs(0);
        AttributeType.HP.apply(raichu, 10);
        assertEquals(0, raichu.getPs());
    }

    @Test
    void testPhysicalAttackApply() {
        int before = raichu.getPhysicalAttack();
        AttributeType.PHYSICAL_ATTACK.apply(raichu, 5);
        assertEquals(before + 5, raichu.getPhysicalAttack());
    }

    @Test
    void testPhysicalDefenseApply() {
        int before = raichu.getPhysicalDefense();
        AttributeType.PHYSICAL_DEFENSE.apply(raichu, 3);
        assertEquals(before + 3, raichu.getPhysicalDefense());
    }

    @Test
    void testSpecialAttackApply() {
        int before = raichu.getSpecialAttack();
        AttributeType.SPECIAL_ATTACK.apply(raichu, 4);
        assertEquals(before + 4, raichu.getSpecialAttack());
    }

    @Test
    void testSpecialDefenseApply() {
        int before = raichu.getSpecialDefense();
        AttributeType.SPECIAL_DEFENSE.apply(raichu, 2);
        assertEquals(before + 2, raichu.getSpecialDefense());
    }

    @Test
    void testSpeedApply() {
        int before = raichu.getSpeed();
        AttributeType.SPEED.apply(raichu, 7);
        assertEquals(before + 7, raichu.getSpeed());
    }

    @Test
    void testEvasionApply() {
        int before = raichu.getEvasion();
        AttributeType.EVASION.apply(raichu, 1);
        assertEquals(before + 1, raichu.getEvasion());
    }
}