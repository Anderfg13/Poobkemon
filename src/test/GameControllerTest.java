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
    void shouldInitializeGameWithValidAndInvalidConfigurations() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu", "Charmander");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle", "Bulbasaur");

        // Configuración válida
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null");
        assertNotNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null");

        // Configuración inválida (tipo de máquina inválido)
        gameController.initializeGame("Player1", "invalid", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null");
        assertNull(gameController.getMachineTrainer(), "El entrenador máquina debería ser null para tipo inválido");

        // Configuración con listas vacías
        gameController.initializeGame("Player1", "attacking", new ArrayList<>(), new ArrayList<>());
        assertNull(gameController.getHumanTrainer(), "El entrenador humano debería ser null con listas vacías");
        assertNull(gameController.getMachineTrainer(), "El entrenador máquina debería ser null con listas vacías");
    }

    @Test
    void shouldProcessPlayerTurnWithValidAndInvalidActions() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // Acciones válidas
        String attackResult = gameController.processPlayerTurn("attack", "Placaje");
        assertNotNull(attackResult, "El resultado del ataque no debería ser null");

        String switchResult = gameController.processPlayerTurn("switch", "1");
        assertNotNull(switchResult, "El resultado del cambio no debería ser null");

        String itemResult = gameController.processPlayerTurn("item", "Poción");
        assertNotNull(itemResult, "El resultado del uso de ítem no debería ser null");

        // Acción inválida
        String invalidResult = gameController.processPlayerTurn("invalid", "value");
        assertNotNull(invalidResult, "El resultado para una acción inválida no debería ser null");
        assertTrue(invalidResult.contains("Acción no válida"), "Debería indicar que la acción no es válida");
    }

    @Test
    void shouldProcessMachineTurnCorrectly() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        String result = gameController.processMachineTurn();
        assertNotNull(result, "El resultado del turno de la máquina no debería ser null");
    }

    @Test
    void shouldVerifyGameStateCorrectly() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // Verificar que el juego no ha terminado
        assertFalse(gameController.isGameOver(), "El juego no debería estar terminado al inicio");

        // Marcar el juego como terminado
        gameController.setGameOver(true);
        assertTrue(gameController.isGameOver(), "El juego debería estar terminado después de marcarlo como terminado");
    }

    @Test
    void shouldGetGameResultCorrectly() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // Marcar el juego como terminado
        gameController.setGameOver(true);

        String result = gameController.getGameResult();
        assertNotNull(result, "El resultado del juego no debería ser null");
        assertTrue(result.contains("Juego terminado"), "Debería indicar que el juego ha terminado");
    }

    @Test
    void shouldHandleMultipleInitializations() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        // Primera inicialización
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null");
        assertNotNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null");

        // Segunda inicialización
        gameController.initializeGame("Player2", "defensive", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null después de la segunda inicialización");
        assertNotNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null después de la segunda inicialización");
    }
}
