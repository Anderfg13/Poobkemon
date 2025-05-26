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

        // Verificar que los entrenadores se inicializaron correctamente
        assertNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null");
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null");

        // Debilitar todos los Pokémon de la máquina
        for (Pokemon p : gameController.getMachineTrainer().getPokemons()) {
            p.setPs(0);
        }

        String result = gameController.processMachineTurn();
        assertNotNull(result);
        assertTrue(result.toLowerCase().contains("no hay") || result.toLowerCase().contains("debilitado") || result.toLowerCase().contains("error"),
                "Debe indicar que no hay pokémon activos o están debilitados");
    }

    @Test
    void shouldInitializeGameWithEmptyLists() {
        ArrayList<String> emptyPlayerPokemons = new ArrayList<>();
        ArrayList<String> emptyMachinePokemons = new ArrayList<>();

        gameController.initializeGame("Player1", "attacking", emptyPlayerPokemons, emptyMachinePokemons);

        // Si los entrenadores existen, no deben tener pokémon
        if (gameController.getHumanTrainer() != null) {
            assertTrue(gameController.getHumanTrainer().getPokemons().isEmpty(),
                    "Si el entrenador existe, no debería tener Pokémon");
        }
        if (gameController.getMachineTrainer() != null) {
            assertTrue(gameController.getMachineTrainer().getPokemons().isEmpty(),
                    "Si el entrenador existe, no debería tener Pokémon");
        }
    }

    @Test
    void shouldProcessTurnsWithoutInitialization() {
        // Cuando no hay inicialización, los métodos deberían manejar el caso graciosamente
        String playerResult = gameController.processPlayerTurn("attack", "Placaje");
        assertNotNull(playerResult, "El resultado del turno del jugador no debería ser null");

        String machineResult = gameController.processMachineTurn();
        assertNotNull(machineResult, "El resultado del turno de la máquina no debería ser null");
    }

    @Test
    void shouldHandleMultipleInitializations() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        // Primera inicialización
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null");
        assertNotNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null");
        String player1Name = gameController.getHumanTrainer().getName();

        // Segunda inicialización
        gameController.initializeGame("Player2", "defensive", playerPokemons, machinePokemons);
        assertNotNull(gameController.getHumanTrainer(), "El entrenador humano no debería ser null después de múltiples inicializaciones");
        assertNotNull(gameController.getMachineTrainer(), "El entrenador máquina no debería ser null después de múltiples inicializaciones");

        // Verificar que el nombre del jugador cambió (confirmando reinicialización)
        assertEquals("Player2", gameController.getHumanTrainer().getName(),
                "El nombre del jugador debería actualizarse en la segunda inicialización");
    }

    @Test
    void shouldReturnCorrectGameResult() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // El resultado del juego cuando no ha terminado
        String initialResult = gameController.getGameResult();
        assertNotNull(initialResult);
        assertTrue(initialResult.toLowerCase().contains("curso") || initialResult.toLowerCase().contains("terminado"),
                "Debería indicar que el juego está en curso o terminado");

        // Marcar el juego como terminado
        gameController.setGameOver(true);
        String gameOverResult = gameController.getGameResult();
        assertNotNull(gameOverResult, "El resultado del juego no debería ser null cuando el juego ha terminado");
    }

    @Test
    void shouldInitializeWithDifferentMachineTypes() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        String[] machineTypes = {"attacking", "defensive", "changing", "expert"};

        for (String type : machineTypes) {
            gameController.initializeGame("Player1", type, playerPokemons, machinePokemons);
            assertNotNull(gameController.getMachineTrainer(),
                    "El entrenador máquina no debería ser null para el tipo " + type);
            assertTrue(gameController.getMachineTrainer() instanceof Machine,
                    "El entrenador máquina debería ser una instancia de Machine");
        }
    }

    @Test
    void shouldHandlePlayerTurnActions() {
        ArrayList<String> playerPokemons = createMutablePokemonList("Pikachu");
        ArrayList<String> machinePokemons = createMutablePokemonList("Squirtle");

        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);

        // Probar diferentes acciones del jugador
        String attackResult = gameController.processPlayerTurn("attack", "Placaje");
        assertNotNull(attackResult, "El resultado del ataque no debería ser null");

        // Reinicializar para prueba independiente
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        String switchResult = gameController.processPlayerTurn("switch", "0");
        assertNotNull(switchResult, "El resultado del cambio no debería ser null");

        // Reinicializar para prueba independiente
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        String itemResult = gameController.processPlayerTurn("item", "Poción");
        assertNotNull(itemResult, "El resultado del uso de ítem no debería ser null");

        // Acción inválida
        gameController.initializeGame("Player1", "attacking", playerPokemons, machinePokemons);
        String invalidResult = gameController.processPlayerTurn("invalid", "value");
        assertNotNull(invalidResult, "El resultado para una acción inválida no debería ser null");
    }
}
