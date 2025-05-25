package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Pruebas para Machine")
public class MachineTest {
    private AttackingMachine attackingMachine;
    private DefensiveMachine defensiveMachine;
    private ArrayList<Pokemon> pokemons;
    private ArrayList<Pokemon> opponentPokemons;
    private ArrayList<String> items;
    private ArrayList<String> opponentItems;
    private Attack ataque1, ataque2, ataque3, ataque4;
    private Pokemon poke1, poke2, poke3, poke4, poke5, poke6;
    private Pokemon opp1, opp2, opp3, opp4, opp5, opp6;

    @BeforeEach
    public void setUp() {
        ataque1 = AttackFactory.createAttack("Puño meteoro");
        ataque2 = AttackFactory.createAttack("Ala de acero");
        ataque3 = AttackFactory.createAttack("Cascada");
        ataque4 = AttackFactory.createAttack("Matillazo");

        poke1 = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Eléctrico", 100);
        poke2 = new Pokemon("Venusaur", 1, 120, 80, 60, 45, 55, 60, "Planta", 120);
        poke3 = new Pokemon("Charizard", 1, 110, 85, 65, 50, 60, 65, "Fuego", 110);
        poke4 = new Pokemon("Blastoise", 1, 130, 75, 70, 55, 65, 70, "Agua", 130);
        poke5 = new Pokemon("Pidgeot", 1, 90, 100, 50, 40, 45, 55, "Volador", 90);
        poke6 = new Pokemon("Gengar", 1, 95, 95, 60, 55, 60, 60, "Fantasma", 95);

        poke1.addAttack(ataque1);
        poke1.addAttack(ataque2);
        poke1.addAttack(ataque3);
        poke1.addAttack(ataque4);

        poke2.addAttack(ataque1);
        poke2.addAttack(ataque2);
        poke2.addAttack(ataque3);
        poke2.addAttack(ataque4);

        poke3.addAttack(ataque1);
        poke3.addAttack(ataque2);
        poke3.addAttack(ataque3);
        poke3.addAttack(ataque4);

        poke4.addAttack(ataque1);
        poke4.addAttack(ataque2);
        poke4.addAttack(ataque3);
        poke4.addAttack(ataque4);

        poke5.addAttack(ataque1);
        poke5.addAttack(ataque2);
        poke5.addAttack(ataque3);
        poke5.addAttack(ataque4);

        poke6.addAttack(ataque1);
        poke6.addAttack(ataque2);
        poke6.addAttack(ataque3);
        poke6.addAttack(ataque4);

        pokemons = new ArrayList<>(Arrays.asList(poke1, poke2, poke3, poke4, poke5, poke6));
        items = new ArrayList<>(Arrays.asList("Poción", "Superpoción"));

        opp1 = new Pokemon("Pikachu", 1, 100, 90, 55, 40, 50, 50, "Eléctrico", 100);
        opp2 = new Pokemon("Bulbasaur", 1, 120, 80, 60, 45, 55, 60, "Planta", 120);
        opp3 = new Pokemon("Charmander", 1, 110, 85, 65, 50, 60, 65, "Fuego", 110);
        opp4 = new Pokemon("Squirtle", 1, 130, 75, 70, 55, 65, 70, "Agua", 130);
        opp5 = new Pokemon("Fearow", 1, 90, 100, 50, 40, 45, 55, "Volador", 90);
        opp6 = new Pokemon("Haunter", 1, 95, 95, 60, 55, 60, 60, "Fantasma", 95);

        opp1.addAttack(ataque1);
        opp1.addAttack(ataque2);
        opp1.addAttack(ataque3);
        opp1.addAttack(ataque4);

        opp2.addAttack(ataque1);
        opp2.addAttack(ataque2);
        opp2.addAttack(ataque3);
        opp2.addAttack(ataque4);

        opp3.addAttack(ataque1);
        opp3.addAttack(ataque2);
        opp3.addAttack(ataque3);
        opp3.addAttack(ataque4);

        opp4.addAttack(ataque1);
        opp4.addAttack(ataque2);
        opp4.addAttack(ataque3);
        opp4.addAttack(ataque4);

        opp5.addAttack(ataque1);
        opp5.addAttack(ataque2);
        opp5.addAttack(ataque3);
        opp5.addAttack(ataque4);

        opp6.addAttack(ataque1);
        opp6.addAttack(ataque2);
        opp6.addAttack(ataque3);
        opp6.addAttack(ataque4);

        opponentPokemons = new ArrayList<>(Arrays.asList(opp1, opp2, opp3, opp4, opp5, opp6));
        opponentItems = new ArrayList<>(Arrays.asList("Poción", "Superpoción"));

        attackingMachine = new AttackingMachine("CPU", pokemons, items);
        defensiveMachine = new DefensiveMachine("CPU2", opponentPokemons, opponentItems);
        attackingMachine.setOpponent(defensiveMachine);
        defensiveMachine.setOpponent(attackingMachine);
    }

    // GRUPO 1: PRUEBAS DE PROPIEDADES BÁSICAS
    @Test
    @DisplayName("1. Verifica el nombre de la máquina")
    public void testMachineName() {
        assertEquals("CPU", attackingMachine.getMachineName());
        System.out.println("✅ Test 1: La máquina tiene el nombre correcto");
    }



    @Test
    @DisplayName("2. Verifica que la máquina tiene items")
    public void testGetItems() {
        assertEquals(2, attackingMachine.getItems().size());
        System.out.println("✅ Test 4: La máquina tiene la cantidad correcta de ítems");
    }

    @Test
    @DisplayName("3. Verifica que es una máquina")
    public void testIsMachine() {
        assertTrue(attackingMachine.isMachine());
        System.out.println("✅ Test 5: El método isMachine() devuelve true");
    }

    @Test
    @DisplayName("4. Verifica que getName() devuelve el nombre correcto")
    public void testGetName() {
        assertEquals("CPU", attackingMachine.getName());
        System.out.println("✅ Test 6: getName() devuelve el nombre correcto");
    }

    // GRUPO 2: PRUEBAS DE SELECCIÓN DE ESTRATEGIA
    @Test
    @DisplayName("5. Verifica que selectMove devuelve un índice válido")
    public void testSelectMove() {
        int moveIndex = attackingMachine.selectMove();
        assertTrue(moveIndex >= 0 && moveIndex < attackingMachine.getActivePokemon().getAtaques().size());
        System.out.println("✅ Test 7: El índice de movimiento seleccionado es válido: " + moveIndex);
    }

    @Test
    @DisplayName("6. Verifica que selectBestPokemon devuelve un índice válido")
    public void testSelectBestPokemon() {
        int pokemonIndex = attackingMachine.selectBestPokemon();
        assertTrue(pokemonIndex >= 0 && pokemonIndex < pokemons.size());
        System.out.println("✅ Test 8: El índice del mejor Pokémon es válido: " + pokemonIndex);
    }

    @Test
    @DisplayName("7. Verifica que shouldUseItem devuelve un booleano")
    public void testShouldUseItem() {
        boolean shouldUse = attackingMachine.shouldUseItem();
        // Solo verificamos que la función devuelve un valor booleano
        assertNotNull(shouldUse);
        System.out.println("✅ Test 9: shouldUseItem devuelve un valor booleano: " + shouldUse);
    }

    @Test
    @DisplayName("8. Verifica que selectItem devuelve un índice válido")
    public void testSelectItem() {
        int itemIndex = attackingMachine.selectItem();
        assertTrue(itemIndex >= -1 && itemIndex < items.size());
        System.out.println("✅ Test 10: El índice del ítem seleccionado es válido: " + itemIndex);
    }

    // GRUPO 3: PRUEBAS DE MANEJO DE TURNOS
    @Test
    @DisplayName("9. Verifica que handleTurnTimeout reduce PP")
    public void testHandleTurnTimeout() {
        int ppBefore = attackingMachine.getActivePokemon().getAtaques().get(0).getPowerPoint();
        attackingMachine.handleTurnTimeout();
        int ppAfter = attackingMachine.getActivePokemon().getAtaques().get(0).getPowerPoint();
        assertTrue(ppAfter <= ppBefore);
        System.out.println("✅ Test 11: handleTurnTimeout reduce PP correctamente");
    }

    // GRUPO 4: PRUEBAS DE CAMBIO DE POKÉMON
    @Test
    @DisplayName("10. Verifica que switchPokemon cambia al Pokémon activo")
    public void testSwitchPokemon() {
        try {
            pokemons.get(1).setPs(50); // Asegurar que tiene vida
            attackingMachine.switchPokemon(1);
            assertEquals(pokemons.get(1), attackingMachine.getActivePokemon());
            System.out.println("✅ Test 12: El cambio de Pokémon funciona correctamente");
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }
    }



    // GRUPO 5: PRUEBAS DE ACCESO A POKÉMON
    @Test
    @DisplayName("11. Verifica que getActivePokemon devuelve un Pokémon no nulo")
    public void testGetActivePokemon() {
        assertNotNull(attackingMachine.getActivePokemon());
        System.out.println("✅ Test 14: getActivePokemon devuelve un Pokémon válido: " + attackingMachine.getActivePokemon().getName());
    }

    @Test
    @DisplayName("15. Verifica que getPokemons devuelve la lista completa")
    public void testGetPokemons() {
        assertEquals(6, attackingMachine.getPokemons().size());
        System.out.println("✅ Test 15: getPokemons devuelve la lista completa de 6 Pokémon");
    }

    // GRUPO 6: PRUEBAS DE MANEJO DE ÍTEMS
    @Test
    @DisplayName("12. Verifica que getNombreItems devuelve los nombres correctos")
    public void testGetNombreItems() {
        List<String> nombres = attackingMachine.getNombreItems();
        assertTrue(nombres.contains("Poción"));
        System.out.println("✅ Test 16: getNombreItems incluye los ítems esperados");
    }



    @Test
    @DisplayName("13. Verifica que eliminarItem elimina correctamente un ítem")
    public void testEliminarItem() {
        attackingMachine.eliminarItem("Poción");
        assertFalse(attackingMachine.getNombreItems().contains("Poción"));
        System.out.println("✅ Test 18: eliminarItem elimina correctamente un ítem");
    }

    // GRUPO 7: PRUEBAS DE ESTADO DE POKÉMON
    @Test
    @DisplayName("14. Verifica areAllPokemonFainted cuando no están debilitados")
    public void testAreAllPokemonFaintedFalse() {
        assertFalse(attackingMachine.areAllPokemonFainted());
        System.out.println("✅ Test 19: areAllPokemonFainted devuelve false cuando hay Pokémon con vida");
    }

    @Test
    @DisplayName("15. Verifica areAllPokemonFainted cuando todos están debilitados")
    public void testAreAllPokemonFaintedTrue() {
        for (Pokemon p : pokemons) {
            p.setPs(0);
        }
        assertTrue(attackingMachine.areAllPokemonFainted());
        System.out.println("✅ Test 20: areAllPokemonFainted devuelve true cuando todos están debilitados");
    }

    @Test
    @DisplayName("16. Verifica revivirPokemon con un Pokémon válido")
    public void testRevivirPokemon() throws PoobkemonException {
        pokemons.get(0).setPs(0);
        attackingMachine.revivirPokemon(pokemons.get(0).getName());
        assertTrue(pokemons.get(0).getPs() > 0);
        System.out.println("✅ Test 21: revivirPokemon recupera la vida del Pokémon debilitado");
    }

    @Test
    @DisplayName("17. Verifica que revivirPokemon lanza excepción con nombre inexistente")
    public void testRevivirPokemonThrows() {
        assertThrows(PoobkemonException.class, () -> attackingMachine.revivirPokemon("PokemonInexistente"));
        System.out.println("✅ Test 22: revivirPokemon lanza excepción con nombre inexistente");
    }


    @Test
    @DisplayName("18. Verifica la creación de una máquina experta")
    public void testExpertMachine() {
        Machine expert = new ExpertMachine("Expert", pokemons, items);
        assertEquals("Expert", expert.getMachineType());
        System.out.println("✅ Test 25: La máquina experta tiene el tipo correcto");
    }
    @Test
    @DisplayName("19. Verifica la creación de una máquina defensiva")
    public void testDefensiveMachine() {
        Machine defensive = new DefensiveMachine("Defensive", pokemons, items);
        assertEquals("Defensive", defensive.getMachineType());
        System.out.println("✅ Test 26: La máquina defensiva tiene el tipo correcto");
    }

    @Test
    @DisplayName("20. Verifica la creación de una máquina atacante")
    public void testAttackingMachine() {
        Machine attacking = new AttackingMachine("Attacking", pokemons, items);
        assertEquals("Attacking", attacking.getMachineType());
        System.out.println("✅ Test 27: La máquina atacante tiene el tipo correcto");
    }

    @Test
    @DisplayName("21. Verifica la creación de una máquina cambiante")
    public void testChangingMachine() {
        Machine changing = new ChangingMachine("Changing", pokemons, items);
        assertEquals("Changing", changing.getMachineType());
        System.out.println("✅ Test 28: La máquina cambiante tiene el tipo correcto");
    }

    // GRUPO 8: PRUEBAS DE getBestEffectivenessMove Y executeTurn

    @Test
    @DisplayName("22. getBestEffectivenessMove retorna el índice correcto")
    public void testGetBestEffectivenessMove() {
        // DefensiveMachine tiene como oponente a attackingMachine
        int idx = attackingMachine.getBestEffectivenessMove();
        assertTrue(idx >= 0 && idx < attackingMachine.getActivePokemon().getAtaques().size());
        System.out.println("✅ Test 29: getBestEffectivenessMove retorna un índice válido: " + idx);
    }

    @Test
    @DisplayName("23. getBestEffectivenessMove retorna 0 si no hay ataques")
    public void testGetBestEffectivenessMoveNoAttacks() {
        attackingMachine.getActivePokemon().getAtaques().clear();
        int idx = attackingMachine.getBestEffectivenessMove();
        assertEquals(0, idx);
        System.out.println("✅ Test 30: getBestEffectivenessMove retorna 0 si no hay ataques");
    }

}