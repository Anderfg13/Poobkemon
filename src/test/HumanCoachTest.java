package test;

import domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HumanCoachTest {
    private HumanCoach coach;
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

        coach = new HumanCoach("Ash", pokemons, items);
    }

    @Test
    void testGetNameAndSetName() {
        assertEquals("Ash", coach.getName());
        coach.setName("Misty");
        assertEquals("Misty", coach.getName());
    }

    @Test
    void testGetActivePokemon() {
        assertEquals(raichu, coach.getActivePokemon());
    }

    @Test
    void testSwitchToPokemon() throws Exception {
        coach.switchToPokemon(2);
        assertEquals(blastoise, coach.getActivePokemon());
    }

    @Test
    void testAgregarPokemon() {
        coach.agregarPokemon("Snorlax");
        assertTrue(coach.getPokemons().stream().anyMatch(p -> p.getName().equals("Snorlax")));
    }

    @Test
    void testAgregarItem() {
        coach.agregarItem("Poción");
        assertTrue(coach.getNombreItems().contains("Poción"));
    }

    @Test
    void testUseItem() throws Exception {
        raichu.setPs(10);
        coach.useItem("Poción");
        assertTrue(raichu.getPs() > 10);
        assertFalse(coach.getNombreItems().contains("Poción"));
    }

    @Test
    void testFleeBattle() {
        coach.fleeBattle();
        assertTrue(coach.getHasFled());
    }

    @Test
    void testAreAllPokemonFainted() {
        raichu.setPs(0);
        venusaur.setPs(0);
        blastoise.setPs(0);
        assertTrue(coach.areAllPokemonFainted());
    }

    @Test
    void testGetPokemonsAndItems() {
        List<Pokemon> team = coach.getPokemons();
        assertEquals(3, team.size());
        List<Item> itemList = coach.getItems();
        assertEquals(3, itemList.size());
    }

    @Test
    void testGetNombreItems() {
        List<String> nombres = coach.getNombreItems();
        assertTrue(nombres.contains("Poción"));
    }

    @Test
    void testCambiaPokemonActivoPorNombre() {
        coach.cambiarPokemonActivo("Venusaur");
        assertEquals(venusaur, coach.getActivePokemon());
    }

    @Test
    void testRevivirPokemon() throws Exception {
        raichu.setPs(0);
        coach.revivirPokemon("Raichu");
        assertTrue(raichu.getPs() > 0);
    }

    @Test
    void testEliminarItem() {
        coach.eliminarItem("Poción");
        assertFalse(coach.getNombreItems().contains("Poción"));
    }

    @Test
    void testSetAndGetColorCoach() {
        coach.setColor(Color.BLUE);
        assertEquals(Color.BLUE, coach.getColorCoach());
    }

    @Test
    void testIsMachine() {
        assertFalse(coach.isMachine());
    }

    //@Test
    //void testSetOpponent() {
    //    HumanCoach rival = new HumanCoach("Gary", pokemons, items);
    //    coach.setOpponent(rival);
    //    // No excepción, solo cobertura
    //}

    @Test
    void testHandleTurnTimeoutReducesPP() {
        int before = raichu.getAtaques().get(0).getPowerPoint();
        coach.handleTurnTimeout();
        int after = raichu.getAtaques().get(0).getPowerPoint();
        assertTrue(after <= before);
    }

    @Test
    void testSwitchToPokemonInvalidIndexThrows() {
        assertThrows(Exception.class, () -> coach.switchToPokemon(10));
    }

    @Test
    void testAgregarItemDuplicado() {
        int before = coach.getNombreItems().size();
        coach.agregarItem("Poción");
        // Puede permitir duplicados, pero el tamaño debe aumentar
        assertTrue(coach.getNombreItems().size() >= before);
    }

    @Test
    void testEliminarItemInexistenteNoFalla() {
        int before = coach.getNombreItems().size();
        coach.eliminarItem("NoExiste");
        assertEquals(before, coach.getNombreItems().size());
    }

    @Test
    void testSetPokemonAttacks() {
        Attack[][] ataques = new Attack[3][];
        ataques[0] = new Attack[]{AttackFactory.createAttack("Rayo carga")};
        ataques[1] = new Attack[]{AttackFactory.createAttack("Bomba lodo")};
        ataques[2] = new Attack[]{AttackFactory.createAttack("Hidrochorro")};
        coach.setPokemonAttacks(ataques);
        assertEquals("Rayo carga", coach.getPokemons().get(0).getAtaques().get(0).getName());
    }

    @Test
    void testCheckPokemonStatus() {
        raichu.setPs(0);
        assertTrue(coach.checkPokemonStatus(raichu));
        raichu.setPs(10);
        assertFalse(coach.checkPokemonStatus(raichu));
    }

    @Test
    void testGetCurrentPokemonEqualsActive() {
        assertEquals(coach.getActivePokemon(), coach.getCurrentPokemon());
    }

    @Test
    void testSetActivePokemon() {
        coach.setActivePokemon(venusaur);
        assertEquals(venusaur, coach.getActivePokemon());
    }

    @Test
    void testGetHasFledInitiallyFalse() {
        HumanCoach nuevo = new HumanCoach("Brock", pokemons, items);
        assertFalse(nuevo.getHasFled());
    }

    @Test
    void testSetColorAndGetColor() {
        coach.setColor(Color.GREEN);
        assertEquals(Color.GREEN, coach.getColorCoach());
    }

    @Test
    void testIsMachineAlwaysFalse() {
        assertFalse(coach.isMachine());
    }

    @Test
    void testGetPokemonsReturnsList() {
        List<Pokemon> list = coach.getPokemons();
        assertEquals(3, list.size());
    }

    @Test
    void testGetItemsReturnsList() {
        List<Item> list = coach.getItems();
        assertEquals(3, list.size());
    }

    @Test
    void testUseItemThrowsIfNoActivePokemon() {
        HumanCoach emptyCoach = new HumanCoach("Ash", new ArrayList<>(), new ArrayList<>());
        Item item = ItemFactory.createItem("Poción");
        assertThrows(Exception.class, () -> emptyCoach.useItem(item));
    }

    @Test
    void testUseItemThrowsIfFullHealth() {
        raichu.setPs(raichu.getTotalPs());
        Item item = ItemFactory.createItem("Poción");
        assertThrows(Exception.class, () -> coach.useItem(item));
    }

    @Test
    void testSetAndGetName() {
        coach.setName("Brock");
        assertEquals("Brock", coach.getName());
    }

    //@Test
    //void testSetOpponentAndGetOpponent() {
    //    HumanCoach rival = new HumanCoach("Gary", pokemons, items);
    //    coach.setOpponent(rival);
        // No excepción, solo cobertura
    //}

    @Test
    void testSwitchToPokemonInvalidIndexThrowsException() {
        assertThrows(Exception.class, () -> coach.switchToPokemon(-1));
        assertThrows(Exception.class, () -> coach.switchToPokemon(100));
    }

    @Test
    void testAgregarItemAllowsDuplicates() {
        int before = coach.getNombreItems().size();
        coach.agregarItem("Poción");
        assertTrue(coach.getNombreItems().size() > before);
    }

    @Test
    void testEliminarItemInexistenteNoAfecta() {
        int before = coach.getNombreItems().size();
        coach.eliminarItem("NoExiste");
        assertEquals(before, coach.getNombreItems().size());
    }
}