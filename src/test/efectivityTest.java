package test;

import domain.efectivity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class efectivityTest {

    @Test
    void testSuperEffective() {
        // Fuego es super efectivo contra Planta
        int fire = efectivity.numberType.get("Fuego");
        int grass = efectivity.numberType.get("Planta");
        assertEquals(2.0, efectivity.efectividad(fire, grass));
    }

    @Test
    void testNotVeryEffective() {
        // Agua es poco efectivo contra Agua
        int water = efectivity.numberType.get("Agua");
        assertEquals(0.5, efectivity.efectividad(water, water));
    }

    @Test
    void testNoEffect() {
        // Normal no afecta a Fantasma
        int normal = efectivity.numberType.get("Normal");
        int ghost = efectivity.numberType.get("Fantasma");
        assertEquals(0.0, efectivity.efectividad(normal, ghost));
    }

    @Test
    void testNeutralEffect() {
        // Planta contra Veneno es neutral
        int grass = efectivity.numberType.get("Planta");
        int poison = efectivity.numberType.get("Veneno");
        assertEquals(0.5, efectivity.efectividad(grass, poison));
        // Planta contra Psiquico es neutral (1.0)
        int psychic = efectivity.numberType.get("Psiquico");
        assertEquals(1.0, efectivity.efectividad(grass, psychic));
    }

    @Test
    void testFlyingAgainstFighting() {
        int flying = efectivity.numberType.get("Volador");
        int fighting = efectivity.numberType.get("Lucha");
        assertEquals(2.0, efectivity.efectividad(flying, fighting));
    }

    @Test
    void testSteelAgainstFairy() {
        int steel = efectivity.numberType.get("Acero");
        int fairy = efectivity.numberType.get("Hada");
        assertEquals(2.0, efectivity.efectividad(steel, fairy));
    }
}