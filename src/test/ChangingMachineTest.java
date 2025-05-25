package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ChangingMachineTest {
    private ChangingMachine machine;
    private Pokemon raichu, venusaur, blastoise;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<String> items;

    @BeforeEach
    void setUp() {
        raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        raichu.addAttack(AttackFactory.createAttack("Rayo carga"));
        raichu.addAttack(AttackFactory.createAttack("Defensa férrea"));
        venusaur.addAttack(AttackFactory.createAttack("Bomba lodo"));
        blastoise.addAttack(AttackFactory.createAttack("Hidrochorro"));

        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));
        items = new ArrayList<>(Arrays.asList("Poción", "Revive", "Superpoción"));

        machine = new ChangingMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Changing", machine.getMachineType());
    }

    @Test
    void testSelectMoveReturnsValidIndex() {
        // Simula un oponente
        DefensiveMachine opponent = new DefensiveMachine("CPU2", pokemons, items);
        machine.setOpponent(opponent);
        int moveIndex = machine.selectMove();
        assertTrue(moveIndex >= 0 && moveIndex < machine.getActivePokemon().getAtaques().size());
    }

    @Test
    void testSelectBestPokemonReturnsValidIndex() {
        DefensiveMachine opponent = new DefensiveMachine("CPU2", pokemons, items);
        machine.setOpponent(opponent);
        int idx = machine.selectBestPokemon();
        assertTrue(idx >= 0 && idx < pokemons.size());
    }

    @Test
    void testShouldUseItemWhenLowHealth() {
        machine.getActivePokemon().setPs(5); // Menos del 35%
        assertTrue(machine.shouldUseItem());
    }

    @Test
    void testSelectItemPrefersPotionIfLowHealth() {
        machine.getActivePokemon().setPs(5);
        int idx = machine.selectItem();
        assertTrue(idx >= 0 && idx < items.size());
        assertEquals("Poción", items.get(idx));
    }

  

@Test
void testChangesStrategyBasedOnOpponent() {
    // Create two different types of opponents
    Pokemon attackerPokemon = new Pokemon("Attacker", 1, 50, 100, 100, 20, 20, 100, "Lucha", 100);
    ArrayList<Pokemon> attackingTeam = new ArrayList<>(Arrays.asList(attackerPokemon));
    AttackingMachine attackingOpponent = new AttackingMachine("Attacker", attackingTeam, items);
    
    Pokemon defenderPokemon = new Pokemon("Defender", 1, 200, 20, 20, 100, 100, 20, "Acero", 50);
    ArrayList<Pokemon> defendingTeam = new ArrayList<>(Arrays.asList(defenderPokemon));
    DefensiveMachine defensiveOpponent = new DefensiveMachine("Defender", defendingTeam, items);
    
    // Test behavior with attacking opponent
    machine.setOpponent(attackingOpponent);
    raichu.setPs(100); // Make sure health is high
    int moveIndex1 = machine.selectMove();
    
    // Test behavior with defensive opponent
    machine.setOpponent(defensiveOpponent);
    int moveIndex2 = machine.selectMove();
    
    assertNotEquals(moveIndex1, moveIndex2);
}

@Test
void testSelectsPokemonBasedOnOpponentType() {
    // Create opponent with a specific type
    Pokemon firePokemon = new Pokemon("Charizard", 1, 100, 90, 90, 70, 70, 100, "Fuego", 100);
    ArrayList<Pokemon> fireTeam = new ArrayList<>(Arrays.asList(firePokemon));
    AttackingMachine fireOpponent = new AttackingMachine("FireTrainer", fireTeam, items);
    machine.setOpponent(fireOpponent);
    
    int waterIndex = -1;
    for (int i = 0; i < pokemons.size(); i++) {
        if ("Agua".equals(pokemons.get(i).getType())) {
            waterIndex = i;
            break;
        }
    }
    
    if (waterIndex != -1) {
        int selectedIndex = machine.selectBestPokemon();
        assertEquals(waterIndex, selectedIndex);
    }
}



}