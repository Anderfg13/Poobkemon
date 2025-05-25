package test;

import domain.Item;
import domain.ItemFactory;
import domain.Pokemon;
import domain.PoobkemonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    private Pokemon raichu;
    private Item pocion;
    private Item superpocion;
    private Item revive;

    @BeforeEach
    public void setUp() {
        raichu = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Eléctrico", 100);
        pocion = ItemFactory.createItem("Poción");
        superpocion = ItemFactory.createItem("Superpoción");
    }

    @Test
    public void shouldRestoreHPWithPotion() throws PoobkemonException {
        raichu.setPs(50);
        pocion.applyItemEffect(raichu);
        assertEquals(70, raichu.getPs()); // Poción suma 20
    }

    @Test
    public void shouldNotExceedMaxHP() throws PoobkemonException {
        raichu.setPs(95);
        pocion.applyItemEffect(raichu);
        assertEquals(100, raichu.getPs()); // No debe pasar el máximo
    }

    @Test
    public void shouldNotUsePotionOnFaintedPokemon() {
        raichu.setPs(0);
        assertThrows(PoobkemonException.class, () -> pocion.applyItemEffect(raichu));
    }

    @Test
    public void shouldRestoreHPWithSuperPotion() throws PoobkemonException {
        raichu.setPs(50);
        superpocion.applyItemEffect(raichu);
        assertEquals(100, raichu.getPs()); // Superpoción suma 40
    }
    @Test
    public void shouldNotUseSuperPotionOnFaintedPokemon() {
        raichu.setPs(0);
        assertThrows(PoobkemonException.class, () -> superpocion.applyItemEffect(raichu));
    }



    @Test
    public void shouldUseItemOnInactivePokemon() throws PoobkemonException {
        Pokemon inactiveRaichu = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Eléctrico", 100);
        inactiveRaichu.setPs(50);
        pocion.applyItemEffect(inactiveRaichu);
        assertEquals(70, inactiveRaichu.getPs()); // Poción suma 20
    }

}