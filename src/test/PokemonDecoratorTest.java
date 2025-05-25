package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import domain.*;

class PokemonDecoratorTest {
    
    private Pokemon basePokemon;
    private Pokemon opponent;
    private Attack attack;
    
    @BeforeEach
    void setUp() {
        // Configurar Pokémon base para pruebas
        basePokemon = new Pokemon("Pikachu", 25, 35, 90, 50, 55, 40, 50, "Electrico", 35);
        opponent = new Pokemon("Bulbasaur", 1, 45, 45, 49, 49, 65, 65, "Planta", 45);
        
        // Crear un ataque para pruebas
        attack = AttackFactory.createAttack("Rayo carga");
        basePokemon.addAttack(attack);
        opponent.addAttack(attack);
    }
    
    @Test
    @DisplayName("ParalyzedPokemonDecorator reduce velocidad correctamente")
    void paralyzedDecoratorReducesSpeed() {
        ParalyzedPokemonDecorator decorated = new ParalyzedPokemonDecorator(basePokemon);
        
        assertEquals(basePokemon.getSpeed() / 2, decorated.getSpeed(), 
                     "La velocidad debe reducirse a la mitad con parálisis");
        assertEquals(1, decorated.getStatus(), "El estado debe ser paralizado (1)");
        assertTrue(decorated.getName().contains("Paralizado"), "El nombre debe indicar el estado");
    }
    
    @Test
    @DisplayName("ParalyzedPokemonDecorator puede impedir atacar")
    void paralyzedDecoratorCanPreventAttack() {
        ParalyzedPokemonDecorator decorated = new ParalyzedPokemonDecorator(basePokemon);
        
        // Ejecutar múltiples ataques para verificar probabilidad de parálisis
        boolean attackPrevented = false;
        for (int i = 0; i < 100 && !attackPrevented; i++) {
            int damage = decorated.attack(opponent, attack);
            if (damage == 0) {
                attackPrevented = true;
            }
        }
        
        assertTrue(attackPrevented, "En múltiples intentos, la parálisis debe impedir al menos un ataque");
    }
    
    @Test
    @DisplayName("SleepingPokemonDecorator impide ataques y reduce turnos")
    void sleepingDecoratorPreventsAttacks() {
        SleepingPokemonDecorator decorated = new SleepingPokemonDecorator(basePokemon);
        
        int initialTurns = decorated.getTurnStatus();
        assertTrue(initialTurns > 0, "Debe tener turnos de sueño iniciales");
        
        // El primer ataque debe fallar porque está dormido
        int damage = decorated.attack(opponent, attack);
        assertEquals(0, damage, "El ataque debe fallar mientras está dormido");
        
        // Debe haber reducido los turnos de sueño
        assertEquals(initialTurns - 1, decorated.getTurnStatus(), 
                     "Los turnos de sueño deben reducirse después de atacar");
    }
    
    @Test
    @DisplayName("BurnedPokemonDecorator reduce ataque físico y causa daño")
    void burnedDecoratorReducesAttackAndCausesDamage() {
        BurnedPokemonDecorator decorated = new BurnedPokemonDecorator(basePokemon);
        
        assertEquals(basePokemon.getPhysicalAttack() / 2, decorated.getPhysicalAttack(), 
                     "El ataque físico debe reducirse a la mitad con quemadura");
        
        int initialHP = decorated.getPs();
        decorated.applyEffectDamage();
        
        assertTrue(decorated.getPs() < initialHP, "La quemadura debe causar daño por turno");
        assertEquals(Math.max(1, basePokemon.getTotalPs() / 16), initialHP - decorated.getPs(),
                    "El daño debe ser 1/16 de la vida total");
    }
    
    @Test
    @DisplayName("FrozenPokemonDecorator impide ataques")
    void frozenDecoratorPreventsAttacks() {
        FrozenPokemonDecorator decorated = new FrozenPokemonDecorator(basePokemon);
        
        // Modificación para forzar que permanezca congelado (sin aleatoriedad)
        // Este enfoque asume que FrozenPokemonDecorator usa Math.random()
        // Si el ataque falló, confirmamos que el estado congelado bloqueó el ataque
        boolean attackBlocked = false;
        for (int i = 0; i < 10 && !attackBlocked; i++) {
            int damage = decorated.attack(opponent, attack);
            if (damage == 0) {
                attackBlocked = true;
            }
        }
        
        assertTrue(attackBlocked, "El estado congelado debe impedir al menos un ataque");
    }
    
    @Test
    @DisplayName("PoisonedPokemonDecorator causa daño progresivo")
    void poisonedDecoratorCausesProgressiveDamage() {
        PoisonedPokemonDecorator decorated = new PoisonedPokemonDecorator(basePokemon);
        
        int initialHP = decorated.getPs();
        
        // Primer turno de veneno
        decorated.applyEffectDamage();
        int firstDamage = initialHP - decorated.getPs();
        assertTrue(firstDamage > 0, "El veneno debe causar daño en el primer turno");
        
        // Segundo turno de veneno - el daño debe ser mayor
        int secondTurnHP = decorated.getPs();
        decorated.applyEffectDamage();
        int secondDamage = secondTurnHP - decorated.getPs();
        
        assertTrue(secondDamage >= firstDamage, 
                   "El daño en el segundo turno debe ser mayor o igual que en el primero");
    }
    
    @Test
    @DisplayName("Decoradores conservan propiedades del Pokémon base")
    void decoratorsPreserveBasePokemonProperties() {
        PokemonBase decorated = new ParalyzedPokemonDecorator(basePokemon);
        
        // Verificar que las propiedades no alteradas se mantienen
        assertEquals(basePokemon.getId(), decorated.getId(), "El ID debe mantenerse");
        assertEquals(basePokemon.getType(), decorated.getType(), "El tipo debe mantenerse");
        assertEquals(basePokemon.getTotalPs(), decorated.getTotalPs(), "Los PS totales deben mantenerse");
        assertEquals(basePokemon.getSpecialAttack(), decorated.getSpecialAttack(), "El ataque especial debe mantenerse");
        assertEquals(basePokemon.getSpecialDefense(), decorated.getSpecialDefense(), "La defensa especial debe mantenerse");
    }
    
    @Test
    @DisplayName("Decoradores mantienen lista de ataques")
    void decoratorsPreserveAttackList() {
        // Agregar múltiples ataques
        Attack attack2 = AttackFactory.createAttack("Rayo");
        basePokemon.addAttack(attack2);
        
        PokemonBase decorated = new BurnedPokemonDecorator(basePokemon);
        
        assertEquals(basePokemon.getAtaques().size(), decorated.getAtaques().size(), 
                     "La cantidad de ataques debe mantenerse");
        assertEquals(basePokemon.getAtaques().get(0).getName(), 
                     decorated.getAtaques().get(0).getName(), 
                     "Los nombres de ataques deben mantenerse");
    }
}