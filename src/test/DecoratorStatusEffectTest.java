package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import domain.*;
import java.util.ArrayList;
import java.util.Arrays;

class DecoratorStatusEffectTest {
    
    private Pokemon attackingPokemon;
    private Pokemon defendingPokemon;
    private Coach attacker;
    private Coach defender;
    private BattleArenaNormal battleArena;
    
    @BeforeEach
    void setUp() {
        // Crear Pokémon
        attackingPokemon = new Pokemon("Pikachu", 25, 100, 90, 70, 65, 60, 50, "Electrico", 35);
        defendingPokemon = new Pokemon("Squirtle", 7, 100, 85, 65, 50, 80, 70, "Agua", 30);
        
        // Agregar ataques
        Attack electricAttack = AttackFactory.createAttack("Rayo");
        Attack paralyzeAttack = new StatusAttack("Onda trueno", "Electrico", 0, 15, 90, "Status", "Paralizado", 1, 100);
        Attack sleepAttack = new StatusAttack("Somnífero", "Planta", 0, 15, 75, "Status", "Dormido", 2, 100);
        Attack burnAttack = new StatusAttack("Ascuas", "Fuego", 40, 15, 100, "Special", "Quemado", 3, 30);
        
        attackingPokemon.addAttack(electricAttack);
        attackingPokemon.addAttack(paralyzeAttack);
        attackingPokemon.addAttack(sleepAttack);
        attackingPokemon.addAttack(burnAttack);
        
        defendingPokemon.addAttack(AttackFactory.createAttack("Pistola agua"));
        defendingPokemon.addAttack(AttackFactory.createAttack("Placaje"));
        
        // Crear entrenadores
        ArrayList<Pokemon> attackerPokemons = new ArrayList<>(Arrays.asList(attackingPokemon));
        ArrayList<Pokemon> defenderPokemons = new ArrayList<>(Arrays.asList(defendingPokemon));
        
        attacker = new HumanCoach("Player", attackerPokemons, new ArrayList<>());
        defender = new HumanCoach("Rival", defenderPokemons, new ArrayList<>());
        
        // Configurar arena de batalla
        battleArena = new BattleArenaNormal();
        Coach[] coaches = battleArena.getCoaches();
        coaches[0] = attacker;
        coaches[1] = defender;
        
        // Establecer Pokémon activos
        attacker.setActivePokemon(attackingPokemon);
        defender.setActivePokemon(defendingPokemon);
    }
    
    @Test
    @DisplayName("Aplicar estado de parálisis en batalla")
    void applyParalysisInBattle() {
        // Aplicar parálisis al defensor
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 1);
        
        // Verificar estado antes del turno
        assertEquals(1, decoratedDefender.getStatus(), "El estado debe ser paralizado (1)");
        int initialSpeed = defendingPokemon.getSpeed();
        assertEquals(initialSpeed / 2, decoratedDefender.getSpeed(), "La velocidad debe reducirse a la mitad");
        
        // Simular applyEffectDamage (como ocurriría en un turno)
        decoratedDefender.applyEffectDamage();
        
        // La parálisis no causa daño directo
        assertEquals(defendingPokemon.getPs(), decoratedDefender.getPs(), 
                    "La parálisis no debe causar daño directo");
        
        // Probar que el Pokemon puede fallar al atacar debido a la parálisis
        boolean attackFailed = false;
        
        for (int i = 0; i < 50 && !attackFailed; i++) {
            if (decoratedDefender.attack(attackingPokemon, defendingPokemon.getAtaques().get(0)) == 0) {
                attackFailed = true;
            }
        }
        
        assertTrue(attackFailed, "El Pokémon paralizado debe fallar al menos un ataque en varios intentos");
    }
    
    @Test
    @DisplayName("Aplicar estado de sueño en batalla")
    void applySleepInBattle() {
        // Aplicar sueño al defensor
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 2);
        
        // Verificar estado antes del turno
        assertEquals(2, decoratedDefender.getStatus(), "El estado debe ser dormido (2)");
        assertTrue(decoratedDefender.getTurnStatus() > 0, "Debe tener turnos de sueño asignados");
        
        // Intentar atacar mientras duerme
        int damage = decoratedDefender.attack(attackingPokemon, defendingPokemon.getAtaques().get(0));
        assertEquals(0, damage, "El Pokémon dormido no debe poder atacar");
        
        // Simular suficientes turnos para despertar
        int initialTurns = decoratedDefender.getTurnStatus();
        for (int i = 0; i < initialTurns; i++) {
            decoratedDefender.attack(attackingPokemon, defendingPokemon.getAtaques().get(0));
        }
        
        // Debe haber despertado
        assertEquals(0, decoratedDefender.getTurnStatus(), "Los turnos de sueño deben agotarse");
        assertEquals(0, decoratedDefender.getStatus(), "El estado debe volver a normal (0)");
    }
    
    @Test
    @DisplayName("Aplicar estado de quemadura en batalla")
    void applyBurnInBattle() {
        // Aplicar quemadura al defensor
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 3);
        
        // Verificar estado y reducción de ataque
        assertEquals(3, decoratedDefender.getStatus(), "El estado debe ser quemado (3)");
        assertEquals(defendingPokemon.getPhysicalAttack() / 2, decoratedDefender.getPhysicalAttack(),
                   "El ataque físico debe reducirse a la mitad");
        
        // Comprobar daño por quemadura
        int initialHP = decoratedDefender.getPs();
        decoratedDefender.applyEffectDamage();
        
        int expectedDamage = Math.max(1, defendingPokemon.getTotalPs() / 16);
        assertEquals(expectedDamage, initialHP - decoratedDefender.getPs(),
                   "El daño por quemadura debe ser 1/16 de la vida total");
    }
    
    @Test
    @DisplayName("Aplicar estado de congelación en batalla")
    void applyFrozenInBattle() {
        // Aplicar congelación al defensor
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 4);
        
        // Verificar estado
        assertEquals(4, decoratedDefender.getStatus(), "El estado debe ser congelado (4)");
        
        // Determinar si el Pokémon puede atacar mientras está congelado
        boolean attackFailed = false;
        for (int i = 0; i < 10 && !attackFailed; i++) {
            if (decoratedDefender.attack(attackingPokemon, defendingPokemon.getAtaques().get(0)) == 0) {
                attackFailed = true;
            }
        }
        
        assertTrue(attackFailed, "El Pokémon congelado debe fallar al menos un ataque");
    }
    
    @Test
    @DisplayName("Aplicar estado de veneno en batalla")
    void applyPoisonInBattle() {
        // Aplicar veneno al defensor
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 5);
        
        // Verificar estado
        assertEquals(5, decoratedDefender.getStatus(), "El estado debe ser envenenado (5)");
        
        // Comprobar daño por veneno en varios turnos
        int initialHP = decoratedDefender.getPs();
        
        // Primer turno
        decoratedDefender.applyEffectDamage();
        int firstDamage = initialHP - decoratedDefender.getPs();
        
        // Segundo turno
        int secondTurnHP = decoratedDefender.getPs();
        decoratedDefender.applyEffectDamage();
        int secondDamage = secondTurnHP - decoratedDefender.getPs();
        
        // El daño del veneno debe aumentar con el tiempo
        assertTrue(secondDamage >= firstDamage, 
                 "El daño por veneno debe incrementar o mantenerse en turnos sucesivos");
    }
    
    @Test
    @DisplayName("Curar todos los estados")
    void cureAllStatusEffects() {
        // Aplicar estado
        PokemonBase decoratedDefender = PokemonStatusFactory.applyStatus(defendingPokemon, 3); // Quemado
        
        // Verificar estado
        assertEquals(3, decoratedDefender.getStatus(), "El estado debe ser quemado (3)");
        
        // Curar todos los estados
        Pokemon cured = PokemonStatusFactory.cureAllStatus(decoratedDefender);
        
        // Verificar que se curó correctamente
        assertEquals(0, cured.getStatus(), "El estado debe ser normal (0) después de curar");
        assertEquals(defendingPokemon.getPhysicalAttack(), cured.getPhysicalAttack(),
                   "El ataque físico debe restaurarse al curar la quemadura");
    }
}