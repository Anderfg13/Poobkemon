package test;

import domain.Pokemon;
import domain.Attack;
import domain.AttackFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PokemonTest {
    private Pokemon Raichu;
    private Pokemon Venusaur;
    private Attack impactrueno;
    private Attack placaje;

    @BeforeEach
    public void setUp() {
        Raichu = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Eléctrico", 100);
        Venusaur = new Pokemon("Venusaur", 2, 120, 80, 49, 49, 65, 65, "Planta", 100);
        impactrueno = AttackFactory.getAttack("Impactrueno");
        placaje = AttackFactory.getAttack("Placaje");
        Raichu.addAttack(impactrueno);
        Raichu.addAttack(placaje);
        Venusaur.addAttack(placaje);
    }

    @Test
    public void shouldReturnCorrectNameAndType() {
        assertEquals("Raichu", Raichu.getName());
        assertEquals("Eléctrico", Raichu.getType());
    }

    @Test
    public void shouldNotAllowMoreThanFourAttacks() {
        Attack ataque1 = AttackFactory.getAttack("Ataque Rápido");
        Attack ataque2 = AttackFactory.getAttack("Gruñido");
        Attack ataque3 = AttackFactory.getAttack("Látigo");
        Raichu.addAttack(ataque1);
        Raichu.addAttack(ataque2);
        assertThrows(IllegalStateException.class, () -> Raichu.addAttack(ataque3));
    }

    @Test
    public void shouldSetAndGetStatus() {
        Raichu.setStatus(3); // Quemado
        assertEquals(3, Raichu.getStatus());
    }

    @Test
    public void shouldApplyEffectDamageForBurn() {
        Raichu.setStatus(3); // Quemado
        int initialHP = Raichu.getPs();
        Raichu.applyEffectDamage();
        assertTrue(Raichu.getPs() < initialHP);
    }

    @Test
    public void shouldSetPsWithinBounds() {
        Raichu.setPs(2000);
        assertEquals(Raichu.getTotalPs(), Raichu.getPs());
        Raichu.setPs(-10);
        assertEquals(0, Raichu.getPs());
    }
}