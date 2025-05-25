package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import domain.*;

class PokemonStatusFactoryTest {
    
    private Pokemon basePokemon;
    
    @BeforeEach
    void setUp() {
        basePokemon = new Pokemon("Pikachu", 25, 35, 90, 50, 55, 40, 50, "Electrico", 35);
        // Agregar ataques para pruebas completas
        Attack attack = AttackFactory.createAttack("Rayo carga");
        basePokemon.addAttack(attack);
    }
    
    @Test
    @DisplayName("Aplicar estado normal no cambia nada")
    void applyNormalStatusReturnsSamePokemon() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 0);
        
        assertSame(basePokemon, resultPokemon, "Aplicar estado normal debe devolver el mismo Pokémon");
        assertEquals(0, resultPokemon.getStatus(), "El estado debe ser normal (0)");
    }
    
    @Test
    @DisplayName("Aplicar estado de parálisis crea el decorador correcto")
    void applyParalyzedStatusCreatesCorrectDecorator() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 1);
        
        assertTrue(resultPokemon instanceof ParalyzedPokemonDecorator, "Debe ser instancia de ParalyzedPokemonDecorator");
        assertEquals(1, resultPokemon.getStatus(), "El estado debe ser paralizado (1)");
        assertTrue(resultPokemon.getName().contains("Paralizado"), "El nombre debe indicar el estado");
        
        // Probar que la velocidad se reduce a la mitad
        assertEquals(basePokemon.getSpeed() / 2, resultPokemon.getSpeed(), 
                     "La velocidad debe reducirse a la mitad con parálisis");
    }
    
    @Test
    @DisplayName("Aplicar estado de sueño crea el decorador correcto")
    void applySleepStatusCreatesCorrectDecorator() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 2);
        
        assertTrue(resultPokemon instanceof SleepingPokemonDecorator, "Debe ser instancia de SleepingPokemonDecorator");
        assertEquals(2, resultPokemon.getStatus(), "El estado debe ser dormido (2)");
        assertTrue(resultPokemon.getName().contains("Dormido"), "El nombre debe indicar el estado");
        
        // Probar que el Pokemon tiene turnos de sueño
        assertTrue(resultPokemon.getTurnStatus() > 0, "Debe tener turnos de sueño asignados");
    }
    
    @Test
    @DisplayName("Aplicar estado de quemadura crea el decorador correcto")
    void applyBurnStatusCreatesCorrectDecorator() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 3);
        
        assertTrue(resultPokemon instanceof BurnedPokemonDecorator, "Debe ser instancia de BurnedPokemonDecorator");
        assertEquals(3, resultPokemon.getStatus(), "El estado debe ser quemado (3)");
        assertTrue(resultPokemon.getName().contains("Quemado"), "El nombre debe indicar el estado");
        
        // Probar que el ataque físico se reduce a la mitad
        assertEquals(basePokemon.getPhysicalAttack() / 2, resultPokemon.getPhysicalAttack(), 
                     "El ataque físico debe reducirse a la mitad con quemadura");
    }
    
    @Test
    @DisplayName("Aplicar estado de congelación crea el decorador correcto")
    void applyFrozenStatusCreatesCorrectDecorator() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 4);
        
        assertTrue(resultPokemon instanceof FrozenPokemonDecorator, "Debe ser instancia de FrozenPokemonDecorator");
        assertEquals(4, resultPokemon.getStatus(), "El estado debe ser congelado (4)");
        assertTrue(resultPokemon.getName().contains("Congelado"), "El nombre debe indicar el estado");
    }
    
    @Test
    @DisplayName("Aplicar estado de veneno crea el decorador correcto")
    void applyPoisonStatusCreatesCorrectDecorator() {
        PokemonBase resultPokemon = PokemonStatusFactory.applyStatus(basePokemon, 5);
        
        assertTrue(resultPokemon instanceof PoisonedPokemonDecorator, "Debe ser instancia de PoisonedPokemonDecorator");
        assertEquals(5, resultPokemon.getStatus(), "El estado debe ser envenenado (5)");
        assertTrue(resultPokemon.getName().contains("Envenenado"), "El nombre debe indicar el estado");
    }
    
    @Test
    @DisplayName("Aplicar estado inválido lanza excepción")
    void applyInvalidStatusThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PokemonStatusFactory.applyStatus(basePokemon, 99);
        }, "Debe lanzar excepción para un código de estado inválido");
    }
    
    @Test
    @DisplayName("Curar todos los estados devuelve el Pokémon base")
    void cureAllStatusReturnBasePokemon() {
        // Aplicar un estado
        PokemonBase decorated = PokemonStatusFactory.applyStatus(basePokemon, 1);
        
        // Curar el estado
        Pokemon cured = PokemonStatusFactory.cureAllStatus(decorated);
        
        assertSame(basePokemon, cured, "Debe devolver el Pokémon base original");
        assertEquals(0, cured.getStatus(), "El estado debe ser reseteado a normal (0)");
        assertEquals(0, cured.getTurnStatus(), "Los turnos de estado deben ser reseteados a 0");
    }
    
    @Test
    @DisplayName("Obtener Pokémon base desde un decorador anidado")
    void getBasePokemonFromNestedDecorators() {
        // Aplicar varios estados (anidamiento de decoradores)
        PokemonBase decorated = PokemonStatusFactory.applyStatus(basePokemon, 1); // Paralizado
        decorated = PokemonStatusFactory.applyStatus(decorated, 3); // Quemado
        
        // Obtener el Pokémon base
        Pokemon base = PokemonStatusFactory.getBasePokemon(decorated);
        
        assertSame(basePokemon, base, "Debe devolver el Pokémon base original");
    }
    
    @Test
    @DisplayName("Verificar estado correcto con hasStatus")
    void hasStatusChecksCorrectly() {
        PokemonBase paralyzed = PokemonStatusFactory.applyStatus(basePokemon, 1);
        
        assertTrue(PokemonStatusFactory.hasStatus(paralyzed, 1), "Debe detectar que el Pokémon está paralizado");
        assertFalse(PokemonStatusFactory.hasStatus(paralyzed, 2), "No debe detectar estado incorrecto");
        assertFalse(PokemonStatusFactory.hasStatus(basePokemon, 1), "Pokémon base no debe tener estado");
    }
    
    @Test
    @DisplayName("Aplicar múltiples estados funciona correctamente")
    void applyMultipleStatusWorksCorrectly() {
        PokemonBase decorated = PokemonStatusFactory.applyMultipleStatus(basePokemon, 1, 3);
        
        // Verificar que tiene alguno de los estados (dependerá del orden de aplicación)
        assertTrue(PokemonStatusFactory.hasStatus(decorated, 1) || 
                  PokemonStatusFactory.hasStatus(decorated, 3),
                  "Debe tener al menos uno de los estados aplicados");
    }
}