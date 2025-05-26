package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GeminiMachineTest {
    private GeminiMachine machine;
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

        machine = new GeminiMachine("CPU", pokemons, items);
    }

    @Test
    void testMachineTypeAndName() {
        assertEquals("CPU", machine.getMachineName());
        assertEquals("Gemini", machine.getMachineType());
    }

    @Test
    void testShouldNotUseItemWhenSufficientHealth() {
        machine.getActivePokemon().setPs(20); // Más del 40% de salud
        assertFalse(machine.shouldUseItem(), "No debería usar un ítem cuando la salud es suficiente");
    }

    @Test
    void testSelectItemReturnsValidIndexWhenItemsAvailable() {
        machine.getActivePokemon().setPs(5); // Salud baja para asegurar el uso de ítems
        int itemIndex = machine.selectItem();
        assertTrue(itemIndex >= 0 && itemIndex < items.size(), "Debe devolver un índice válido de ítem");
    }

    @Test
    void testSelectItemReturnsMinusOneWhenNoItemsAvailable() {
        machine.getItems().clear(); // Sin ítems disponibles
        int itemIndex = machine.selectItem();
        assertEquals(-1, itemIndex, "Debe devolver -1 cuando no hay ítems disponibles");
    }

    @Test
    void testSelectBestPokemonReturnsValidIndex() {
        int bestPokemonIndex = machine.selectBestPokemon();
        assertTrue(bestPokemonIndex >= 0 && bestPokemonIndex < pokemons.size(), "Debe devolver un índice válido de Pokémon");
    }

    @Test
    void testSelectMoveReturnsValidIndex() {
        int moveIndex = machine.selectMove();
        assertTrue(moveIndex >= 0 && moveIndex < machine.getActivePokemon().getAtaques().size(), "Debe devolver un índice válido de ataque");
    }
}