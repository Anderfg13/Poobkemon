package test;

import domain.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class AttackTest {

    private PhysicalAttack physicalAttack;
    private SpecialAttack specialAttack;
    private StatusAttack statusAttack;
    private Pokemon attacker;
    private Pokemon defender;

    @BeforeEach
    void setUp() {
        physicalAttack = new PhysicalAttack("Puño meteoro", "Acero", 100, 5, 90, "Physical");
        specialAttack = new SpecialAttack("Rayo", "Electrico", 80, 5, 100, "Special");
        statusAttack = new StatusAttack("Defensa férrea", "Acero", 0, 5, 100, AttributeType.PHYSICAL_DEFENSE, 3, "Status");
        attacker = new Pokemon("Pikachu", 1, 100, 90, 55, 40, 50, 50, "Electrico", 100);
        defender = new Pokemon("Bulbasaur", 1, 120, 80, 60, 45, 55, 60, "Planta", 120);
    }

    @Test
    void testGetters() {
        assertEquals("Puño meteoro", physicalAttack.getName());
        assertEquals("Acero", physicalAttack.getType());
        assertEquals(100, physicalAttack.getBaseDamage());
        assertEquals(5, physicalAttack.getPowerPoint());
        assertEquals(90, physicalAttack.getPrecision());
        assertEquals("Physical", physicalAttack.getAttackType());
        assertNull(physicalAttack.getEffect());
    }

    @Test
    void testSetPowerPointAndUsarAtaque() {
        physicalAttack.setPowerPoint(3);
        assertEquals(3, physicalAttack.getPowerPoint());
        physicalAttack.usarAtaque();
        assertEquals(2, physicalAttack.getPowerPoint());
        physicalAttack.setPowerPoint(0);
        physicalAttack.usarAtaque();
        assertEquals(0, physicalAttack.getPowerPoint());
    }

    @Test
    void testGetMaxPowerPoint() {
        assertEquals(15, physicalAttack.getMaxPowerPoint());
        assertEquals(15, specialAttack.getMaxPowerPoint());
        assertEquals(20, statusAttack.getMaxPowerPoint());
    }

    @Test
    void testGetMaxPowerPointReturns10ForUnknownType() {
        // Creamos un ataque con un tipo desconocido
        PhysicalAttack unknownTypeAttack = new PhysicalAttack("Raro", "Desconocido", 50, 5, 90, "OtroTipo");
        assertEquals(10, unknownTypeAttack.getMaxPowerPoint());
    }

    @Test
    void testClone() {
        Attack clone = physicalAttack.clone();
        assertNotSame(physicalAttack, clone);
        assertEquals(physicalAttack.getName(), clone.getName());
        assertEquals(physicalAttack.getType(), clone.getType());
    }

    @Test
    void testCalcDañoSpecial() {
        attacker.addAttack(specialAttack);
        defender.addAttack(specialAttack);
        int damage = specialAttack.calcDaño(attacker, defender);
        assertTrue(damage >= 1);
        assertEquals(4, specialAttack.getPowerPoint());
    }

    @Test
    void testCalcDañoStatus() {
        attacker.addAttack(statusAttack);
        defender.addAttack(statusAttack);
        int damage = statusAttack.calcDaño(attacker, defender);
        assertEquals(0, damage);
        assertEquals(4, statusAttack.getPowerPoint());
    }

    @Test
    void testCalcDañoSinPP() {
        attacker.addAttack(physicalAttack);
        defender.addAttack(physicalAttack);
        physicalAttack.setPowerPoint(0);
        int damage = physicalAttack.calcDaño(attacker, defender);
        assertEquals(0, damage);
    }

    @Test
    void testCalcDañoPrecisionFalla() {
        attacker.addAttack(physicalAttack);
        defender.addAttack(physicalAttack);
        physicalAttack = new PhysicalAttack("Golpe fallido", "Acero", 100, 5, 0, "Physical");
        attacker.addAttack(physicalAttack);
        int total = 0;
        for (int i = 0; i < 10; i++) {
            total += physicalAttack.calcDaño(attacker, defender);
        }
        assertEquals(0, total);
    }

    @Test
    void testCalcDañoThrowsForUnknownAttackType() {
        // Creamos un ataque con tipo no registrado en efectivity.numberType
        PhysicalAttack unknownTypeAttack = new PhysicalAttack("Raro", "TipoInexistente", 50, 5, 90, "Physical");
        attacker.addAttack(unknownTypeAttack);
        defender.addAttack(unknownTypeAttack);
        Exception ex = assertThrows(RuntimeException.class, () -> {
            unknownTypeAttack.calcDaño(attacker, defender);
        });
        assertTrue(ex.getMessage().contains("Tipo de ataque no reconocido"));
    }

    @Test
    void testCalcDañoThrowsForUnknownDefenderType() {
        // Creamos un Pokémon con tipo no registrado
        Pokemon rareDefender = new Pokemon("Raro", 1, 100, 90, 55, 40, 50, 50, "TipoQueNoExisteJamás", 100);
        PhysicalAttack attack = new PhysicalAttack("Puño meteoro", "Acero", 100, 5, 90, "Physical");
        attacker.addAttack(attack);
        rareDefender.addAttack(attack);
        Exception ex = assertThrows(RuntimeException.class, () -> {
            attack.calcDaño(attacker, rareDefender);
        });
        assertTrue(ex.getMessage().contains("Tipo de defensor no reconocido"));
    }
}