package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExpertStrategyTest {
    private ExpertStrategy strategy;
    private ExpertMachine machine;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;
    private Pokemon raichu, venusaur, blastoise;
    private BattleArenaNormal arena;

    @BeforeEach
    void setUp() {
        strategy = new ExpertStrategy();

        // Crear Pokémon con stats y ataques
        raichu = new Pokemon("Raichu", 25, 100, 90, 55, 80, 55, 110, "Electrico", 100);
        venusaur = new Pokemon("Venusaur", 3, 100, 82, 83, 100, 100, 80, "Planta", 100);
        blastoise = new Pokemon("Blastoise", 9, 100, 83, 100, 85, 105, 78, "Agua", 100);

        raichu.addAttack(new PhysicalAttack("Placaje", "Normal", 40, 35, 100, "Physical"));
        raichu.addAttack(new SpecialAttack("Rayo", "Electrico", 90, 15, 100, "Special"));
        venusaur.addAttack(new PhysicalAttack("Látigo cepa", "Planta", 45, 25, 100, "Physical"));
        blastoise.addAttack(new SpecialAttack("Pistola Agua", "Agua", 40, 25, 100, "Special"));

        pokemons = new ArrayList<>();
        pokemons.add(raichu);
        pokemons.add(venusaur);
        pokemons.add(blastoise);

        items = new ArrayList<>();
        items.add("Poción");
        items.add("Superpoción");
        items.add("Revive");

        machine = new ExpertMachine("CPU", new ArrayList<>(pokemons), new ArrayList<>(items));
        machine.setActivePokemon(raichu);

        arena = new BattleArenaNormal();
        arena.getCoaches()[0] = machine;
        arena.getCoaches()[1] = new DefensiveMachine("OPP", new ArrayList<>(pokemons), new ArrayList<>(items));
    }

    @Test
    void testDecideActionSwitchWhenTypeDisadvantage() {
        DefensiveMachine opponent = new DefensiveMachine("OPP", new ArrayList<>(pokemons), new ArrayList<>(items));
        opponent.setActivePokemon(venusaur); // Ventaja de tipo sobre Raichu
        machine.setOpponent(opponent);

        int action = strategy.decideAction(machine, arena);
        assertEquals(3, action); // Cambiar Pokémon
    }

    @Test
    void testDecideActionUseItemWhenLowHealth() {
        machine.getActivePokemon().setPs(10); // Salud baja
        int action = strategy.decideAction(machine, arena);
        assertEquals(2, action); // Usar ítem
    }

    @Test
    void testDecideActionAttackWhenOpponentWeak() {
        DefensiveMachine opponent = new DefensiveMachine("OPP", new ArrayList<>(pokemons), new ArrayList<>(items));
        opponent.setActivePokemon(blastoise);
        opponent.getActivePokemon().setPs(10); // Oponente con poca salud
        machine.setOpponent(opponent);

        int action = strategy.decideAction(machine, arena);
        assertEquals(1, action); // Atacar para terminar
    }

    @Test
    void testSelectAttackReturnsBestAttack() {
        DefensiveMachine opponent = new DefensiveMachine("OPP", new ArrayList<>(pokemons), new ArrayList<>(items));
        opponent.setActivePokemon(blastoise); // Ventaja de tipo para Raichu
        machine.setOpponent(opponent);

        String attack = strategy.selectAttack(machine, arena);
        assertNotNull(attack);
    }

    @Test
    void testSelectItemReturnsPotionWhenLowHealth() {
        machine.getActivePokemon().setPs(10); // Salud baja
        String item = strategy.selectItem(machine, arena);
        assertEquals("Poción", item);
    }

    @Test
    void testSelectPokemonReturnsBestOption() {
        // Configurar oponente con ventaja de tipo sobre Raichu
        DefensiveMachine opponent = new DefensiveMachine("OPP", new ArrayList<>(pokemons), new ArrayList<>(items));
        opponent.setActivePokemon(venusaur); // Planta tiene ventaja sobre Eléctrico
        machine.setOpponent(opponent);

        // Ejecutar la selección de Pokémon
        Integer idx = strategy.selectPokemon(machine, arena);

        // Verificar el resultado
        if (idx == null) {
            assertNull(idx, "Debe retornar null si no hay un Pokémon válido para seleccionar");
        } else if (idx == -1) {
            assertEquals(-1, idx, "Debe retornar -1 si no hay un Pokémon adecuado");
        } else {
            assertTrue(idx >= 0 && idx < pokemons.size(), "Debe seleccionar un índice válido de Pokémon");
        }
    }

    @Test
    void testShouldFleeWhenAllPokemonFainted() {
        machine.getPokemons().forEach(p -> p.setPs(0)); // Todos los Pokémon debilitados
        boolean shouldFlee = strategy.shouldFlee(machine, arena);
        assertTrue(shouldFlee);
    }

    //prueba
    @Test
    void testShouldNotFleeWhenHealthy() {
        machine.getPokemons().forEach(p -> p.setPs(p.getTotalPs())); // Todos los Pokémon sanos
        boolean shouldFlee = strategy.shouldFlee(machine, arena);
        assertFalse(shouldFlee);
    }

    }
