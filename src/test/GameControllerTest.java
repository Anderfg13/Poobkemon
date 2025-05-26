package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import domain.*;
import java.util.ArrayList;

public class GameControllerTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
    }

    // Método helper para crear listas mutables de Pokémon
    private ArrayList<String> createMutablePokemonList(String... pokemonNames) {
        ArrayList<String> list = new ArrayList<>();
        for (String name : pokemonNames) {
            list.add(name);
        }
        return list;
    }



 

    @Test
    void shouldProcessMachineTurnWithNoActivePokemon() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // Debilitar todos los Pokémon de la máquina
        for (Pokemon p : gameController.getMachineTrainer().getPokemons()) {
            p.setPs(0);
        }

        String result = gameController.processMachineTurn();
        assertTrue(result.contains("No hay Pokémon activos"), "Debería manejar el caso donde no hay Pokémon activos");
    }

  




    @Test
    void shouldInitializeGameWithEmptyLists() {
        ArrayList<String> emptyPlayerPokemons = new ArrayList<>();
        ArrayList<String> emptyMachinePokemons = new ArrayList<>();

        gameController.initializeGame("Player1", "attacking", emptyPlayerPokemons, emptyMachinePokemons);
        assertNull(gameController.getHumanTrainer(), "El entrenador humano debería ser null con listas vacías");
        assertNull(gameController.getMachineTrainer(), "El entrenador máquina debería ser null con listas vacías");
    }

    @Test
    void shouldProcessTurnsWithoutInitialization() {
        String playerResult = gameController.processPlayerTurn("attack", "Placaje");
        assertNotNull(playerResult, "El resultado del turno del jugador no debería ser null");

        String machineResult = gameController.processMachineTurn();
        assertNotNull(machineResult, "El resultado del turno de la máquina no debería ser null");
    }


}
