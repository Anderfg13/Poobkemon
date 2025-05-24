package test;

import domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AttackFactoryTest {

    @Test
    void testCreateAttackReturnsClone() {
        Attack attack1 = AttackFactory.createAttack("Puño meteoro");
        Attack attack2 = AttackFactory.createAttack("Puño meteoro");
        assertNotSame(attack1, attack2);
        assertEquals(attack1.getName(), attack2.getName());
        assertEquals(attack1.getType(), attack2.getType());
    }

    @Test
    void testCreateAttackThrowsForUnknownAttack() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            AttackFactory.createAttack("NoExiste");
        });
        assertTrue(exception.getMessage().contains("no está registrado"));
    }

    @Test
    void testGetAttackNamesContainsKnownAttack() {
        List<String> names = AttackFactory.getAttackNames();
        assertTrue(names.contains("Puño meteoro"));
        assertTrue(names.contains("Hidrobomba"));
        assertTrue(names.contains("Defensa férrea"));
    }

    @Test
    void testGetAllAttacksReturnsMapWithKnownAttack() {
        Map<String, Attack> all = AttackFactory.getAllAttacks();
        assertTrue(all.containsKey("Puño meteoro"));
        assertTrue(all.containsKey("Hidrobomba"));
        assertTrue(all.containsKey("Defensa férrea"));
        assertTrue(all.get("Puño meteoro") instanceof PhysicalAttack);
    }

    @Test
    void testGetAttackReturnsRegisteredInstance() {
        Attack attack = AttackFactory.getAttack("Puño meteoro");
        assertNotNull(attack);
        assertEquals("Puño meteoro", attack.getName());
    }

    @Test
    void testGetAttackReturnsNullForUnknown() {
        Attack attack = AttackFactory.getAttack("NoExiste");
        assertNull(attack);
    }
}