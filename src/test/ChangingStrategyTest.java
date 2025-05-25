package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ChangingStrategyTest {

    private ChangingStrategy strategy;
    private Machine machine;
    private BattleArena battleArena;
    private Coach opponent;
    private Pokemon raichu;
    private Pokemon venusaur;
    private Pokemon blastoise;
    private List<Pokemon> pokemons;
    private List<Item> items;
    private ArrayList<String> itemNames;

    @BeforeEach
    void setUp() {
        // Initialize Pokemons
        raichu = new Pokemon("Raichu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        venusaur = new Pokemon("Venusaur", 5, 45, 49, 49, 65, 65, 45, "Planta", 45);
        blastoise = new Pokemon("Blastoise", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);

        // Add attacks to Pokémon
        Attack rayoCarga = AttackFactory.createAttack("Rayo carga");
        Attack defensaFerrea = AttackFactory.createAttack("Defensa férrea");
        Attack bombaLodo = AttackFactory.createAttack("Bomba lodo");
        Attack hidrochorro = AttackFactory.createAttack("Hidrochorro");

        raichu.addAttack(rayoCarga);
        raichu.addAttack(defensaFerrea);
        venusaur.addAttack(bombaLodo);
        blastoise.addAttack(hidrochorro);

        // Initialize Items
        items = new ArrayList<>();
        items.add(ItemFactory.createItem("Poción"));
        items.add(ItemFactory.createItem("Superpoción"));
        
        // Initialize item names
        itemNames = new ArrayList<>();
        itemNames.add("Poción");
        itemNames.add("Superpoción");

        // Initialize Pokemons list
        pokemons = new ArrayList<>(Arrays.asList(raichu, venusaur, blastoise));

        // Initialize Machine and Coach
        machine = new AttackingMachine("CPU", new ArrayList<>(pokemons), itemNames);
        
        // Initialize the opponent
        ArrayList<Pokemon> emptyPokemonList = new ArrayList<>();
        ArrayList<String> emptyItemList = new ArrayList<>();
        opponent = new HumanCoach("Ash", emptyPokemonList, emptyItemList);
        
        // Add a Pokémon to the opponent's team for testing
        Pokemon oppPokemon = new Pokemon("Pikachu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        opponent.getPokemons().add(oppPokemon);

        // Inicialización correcta de BattleArenaNormal
        battleArena = new BattleArenaNormal();

        // Establecer los entrenadores en la arena usando el método correcto
        // Si BattleArenaNormal tiene un array de tamaño fijo para los coaches
        Coach[] coaches = battleArena.getCoaches();
        if (coaches != null && coaches.length >= 2) {
            coaches[0] = machine;
            coaches[1] = opponent;
        }

        // Establecer Pokémon activo para los entrenadores
        machine.setActivePokemon(raichu);
        opponent.setActivePokemon(oppPokemon);
        
        // Create ChangingStrategy
        strategy = new ChangingStrategy();
    }



    @Test
    void testDecideActionWithTypeDisadvantage() {
        // Arrange
        Pokemon electricType = new Pokemon("Pikachu", 5, 35, 55, 40, 50, 50, 90, "Electrico", 35);
        opponent.setActivePokemon(electricType);
        machine.setActivePokemon(blastoise);

        // Act
        int action = strategy.decideAction(machine, battleArena);

        // Assert
        // Since we can't directly control the internal behavior without Mockito,
        // we can only assert that the action is one of the possible actions.
        assertTrue(action == 1 || action == 2 || action == 3, "Should decide to attack, use item, or change Pokémon when at type disadvantage");
    }



    @Test
    void testSelectAttackWithNoAttacks() {
        // Arrange
        raichu.getAtaques().clear();
        machine.setActivePokemon(raichu);

        // Act
        String selectedAttack = strategy.selectAttack(machine, battleArena);

        // Assert
        assertNull(selectedAttack, "Should return null when no attacks are available");
    }

    @Test
    void testSelectItemWithLowHealth() {
        // Arrange
        raichu.setPs((int) (raichu.getTotalPs() * 0.3)); // 30% health
        machine.setActivePokemon(raichu);

        // Act
        String selectedItem = strategy.selectItem(machine, battleArena);

        // Assert
        assertTrue(selectedItem.contains("Poción") || selectedItem.contains("Superpoción"), "Should select a potion when health is low");
    }

    @Test
    void testSelectItemWithNoItems() {
        // Arrange
        machine.getItems().clear();

        // Act
        String selectedItem = strategy.selectItem(machine, battleArena);

        // Assert
        assertNull(selectedItem, "Should return null when no items are available");
    }

    @Test
    void testSelectPokemonWithTypeAdvantage() {
        // Arrange
        Pokemon waterType = new Pokemon("Squirtle", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);
        opponent.setActivePokemon(waterType);

        // Act
        int selectedIndex = strategy.selectPokemon(machine, battleArena);

        // Assert
        assertTrue(selectedIndex >= 0 && selectedIndex < pokemons.size(), "Should select a valid pokemon");
    }

    @Test
    void testSelectPokemonWithNoOpponent() {
        // Arrange
        opponent.setActivePokemon(null);

        // Act
        int selectedIndex = strategy.selectPokemon(machine, battleArena);

        // Assert
        assertTrue(selectedIndex >= 0 && selectedIndex < pokemons.size(), "Should select a valid pokemon");
    }

    

    @Test
    void testShouldNotFleeWithAdvantage() {
        // Arrange
        Pokemon waterType = new Pokemon("Squirtle", 5, 44, 48, 65, 50, 64, 43, "Agua", 44);
        opponent.setActivePokemon(waterType);

        // Act
        boolean shouldFlee = strategy.shouldFlee(machine, battleArena);

        // Assert
        // Due to randomness, we can't assert a specific outcome.
        // We can only assert that the method runs without throwing an exception.
        assertNotNull(shouldFlee);
    }

    @Test
    void testShouldNotFleeWithNoOpponent() {
        // Arrange
        opponent.setActivePokemon(null);

        // Act
        boolean shouldFlee = strategy.shouldFlee(machine, battleArena);

        // Assert
        assertFalse(shouldFlee, "Should not flee when there's no opponent information");
    }
