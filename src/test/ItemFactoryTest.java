package test;

import domain.Item;
import domain.ItemFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemFactoryTest {

    @Test
    void testCreateItemReturnsCorrectItem() {
        Item potion = ItemFactory.createItem("Poción");
        assertEquals("Poción", potion.getName());
        assertEquals("Restaura 20 PS de un Pokémon.", potion.getDescription());
        assertEquals(20, potion.getEffectValue());
        assertEquals(Item.AttributeType.HP, potion.getApplyTo());

        Item revive = ItemFactory.createItem("Revive");
        assertEquals("Revive", revive.getName());
        assertEquals(Item.AttributeType.REVIVE, revive.getApplyTo());
    }

    @Test
    void testGetItemNamesContainsRegisteredItems() {
        List<String> names = ItemFactory.getItemNames();
        assertTrue(names.contains("Poción"));
        assertTrue(names.contains("Superpoción"));
        assertTrue(names.contains("Revive"));
    }

    @Test
    void testCreateItemThrowsForUnknownItem() {
        Exception ex = assertThrows(IllegalArgumentException.class, () -> {
            ItemFactory.createItem("NoExiste");
        });
        assertTrue(ex.getMessage().contains("Ítem no reconocido"));
    }
}