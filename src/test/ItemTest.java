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

    @Test
    public void shouldReturnCorrectNameDescriptionEffectAndApplyTo() {
        assertEquals("Poción", pocion.getName());
        assertTrue(pocion.getDescription().contains("Restaura"));
        assertEquals(20, pocion.getEffectValue());
        assertEquals(Item.AttributeType.HP, pocion.getApplyTo());
    }

    @Test
    public void shouldAllowPotionUseWhenNotFullHP() {
        raichu.setPs(80);
        assertTrue(pocion.canUse(raichu, new java.util.ArrayList<>()));
    }

    @Test
    public void shouldNotAllowPotionUseWhenFullHP() {
        raichu.setPs(raichu.getTotalPs());
        assertFalse(pocion.canUse(raichu, new java.util.ArrayList<>()));
    }

    @Test
    public void shouldApplyReviveEffect() throws PoobkemonException {
        revive = ItemFactory.createItem("Revive");
        raichu.setPs(0);
        revive.applyItemEffect(raichu);
        assertTrue(raichu.getPs() > 0);
    }

    @Test
    public void shouldApplyStatBoosts() throws PoobkemonException {
        Item xAtaque = new Item("X-Ataque", "Aumenta el ataque físico", 2, Item.AttributeType.PHYSICAL_ATTACK);
        int before = raichu.getPhysicalAttack();
        xAtaque.applyItemEffect(raichu);
        assertEquals(before + 2, raichu.getPhysicalAttack());
    }

    @Test
    public void shouldApplyDefenseBoost() throws PoobkemonException {
        Item xDefensa = new Item("X-Defensa", "Aumenta la defensa física", 3, Item.AttributeType.PHYSICAL_DEFENSE);
        int before = raichu.getPhysicalDefense();
        xDefensa.applyItemEffect(raichu);
        assertEquals(before + 3, raichu.getPhysicalDefense());
    }

    @Test
    public void shouldApplySpecialAttackBoost() throws PoobkemonException {
        Item xEsp = new Item("X-Especial", "Aumenta el ataque especial", 4, Item.AttributeType.SPECIAL_ATTACK);
        int before = raichu.getSpecialAttack();
        xEsp.applyItemEffect(raichu);
        assertEquals(before + 4, raichu.getSpecialAttack());
    }

    @Test
    public void shouldApplySpecialDefenseBoost() throws PoobkemonException {
        Item xEspDef = new Item("X-EspecialDef", "Aumenta la defensa especial", 2, Item.AttributeType.SPECIAL_DEFENSE);
        int before = raichu.getSpecialDefense();
        xEspDef.applyItemEffect(raichu);
        assertEquals(before + 2, raichu.getSpecialDefense());
    }

    @Test
    public void shouldApplySpeedBoost() throws PoobkemonException {
        Item xVel = new Item("X-Velocidad", "Aumenta la velocidad", 5, Item.AttributeType.SPEED);
        int before = raichu.getSpeed();
        xVel.applyItemEffect(raichu);
        assertEquals(before + 5, raichu.getSpeed());
    }

    @Test
    public void shouldApplyEvasionBoost() throws PoobkemonException {
        Item xEvasion = new Item("X-Evasión", "Aumenta la evasión", 1, Item.AttributeType.EVASION);
        int before = raichu.getEvasion();
        xEvasion.applyItemEffect(raichu);
        assertEquals(before + 1, raichu.getEvasion());
    }
}