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
    private Attack esfuerzo;

    @BeforeEach
    public void setUp() {
        Raichu = new Pokemon("Raichu", 1, 100, 90, 55, 40, 50, 50, "Electrico", 100);
        Venusaur = new Pokemon("Venusaur", 2, 120, 80, 49, 49, 65, 65, "Planta", 100);
        impactrueno = AttackFactory.getAttack("Impactrueno");
        esfuerzo = AttackFactory.getAttack("Esfuerzo");
        Raichu.addAttack(impactrueno);
        Raichu.addAttack(esfuerzo);
        Venusaur.addAttack(esfuerzo);
    }

    @Test
    public void shouldReturnCorrectNameAndType() {
        assertEquals("Raichu", Raichu.getName());
        assertEquals("Electrico", Raichu.getType());
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

    @Test
    public void shouldReturnCorrectId() {
        assertEquals(1, Raichu.getId());
        assertEquals(2, Venusaur.getId());
    }

    @Test
    public void shouldSetAndGetTurnStatus() {
        Raichu.setTurnStatus(3);
        assertEquals(3, Raichu.getTurnStatus());
    }

    @Test
    public void shouldSetAttacksFromArray() {
        Attack ataque1 = AttackFactory.getAttack("Impactrueno");
        Attack ataque2 = AttackFactory.getAttack("Esfuerzo");
        Attack[] ataques = new Attack[]{ataque1, ataque2};
        Raichu.setAttacks(ataques);
        assertEquals(2, Raichu.getAtaques().size());
        assertTrue(Raichu.getAtaques().contains(ataque1));
        assertTrue(Raichu.getAtaques().contains(ataque2));
    }

    @Test
    public void shouldAttackItselfIfToItselfTrue() {
        Attack ataque = AttackFactory.getAttack("Esfuerzo");
        Raichu.setPs(Raichu.getTotalPs());
        int initialPs = Raichu.getPs();
        int damage = Raichu.attack(true, ataque);
        assertTrue(damage >= 0);
        assertEquals(initialPs - damage, Raichu.getPs());
    }

    @Test
    public void shouldThrowIfAttackToOpponentWithThisMethod() {
        Attack ataque = AttackFactory.getAttack("Esfuerzo");
        assertThrows(UnsupportedOperationException.class, () -> Raichu.attack(false, ataque));
    }

    @Test
    public void shouldReturnPPMaxDeAtaque() {
        Attack ataque = AttackFactory.getAttack("Esfuerzo");
        Raichu.setAttacks(new Attack[]{ataque});
        int pp = Raichu.getPPMaxDeAtaque("Esfuerzo");
        assertEquals(ataque.getPowerPoint(), pp);
        assertEquals(0, Raichu.getPPMaxDeAtaque("NoExiste"));
    }
}