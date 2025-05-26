package test;

import domain.Pokemon;
import domain.Attack;
import domain.AttackFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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



    @Test
    public void shouldGetCorrectStats() {
        assertEquals(90, Raichu.getSpeed());
        assertEquals(55, Raichu.getSpecialAttack());
        assertEquals(40, Raichu.getPhysicalAttack());
        assertEquals(50, Raichu.getSpecialDefense());
        assertEquals(50, Raichu.getPhysicalDefense());
        assertEquals(100, Raichu.getEvasion());
    }

    @Test
    public void shouldSetStats() {
        Raichu.setSpeed(95);
        Raichu.setSpecialAttack(60);
        Raichu.setPhysicalAttack(45);
        Raichu.setSpecialDefense(55);
        Raichu.setPhysicalDefense(55);
        Raichu.setEvasion(110);
        
        assertEquals(95, Raichu.getSpeed());
        assertEquals(60, Raichu.getSpecialAttack());
        assertEquals(45, Raichu.getPhysicalAttack());
        assertEquals(55, Raichu.getSpecialDefense());
        assertEquals(55, Raichu.getPhysicalDefense());
        assertEquals(110, Raichu.getEvasion());
    }



    @Test
    public void shouldApplyEffectDamageForPoison() {
        Raichu.setStatus(5); // Envenenado
        int initialHP = Raichu.getPs();
        Raichu.applyEffectDamage();
        assertTrue(Raichu.getPs() < initialHP);
    }



    @Test
    public void shouldMaintainCorrectStatusTurns() {
        Raichu.setStatus(2); // Dormido
        Raichu.setTurnStatus(3); // Dormido por 3 turnos
        assertEquals(2, Raichu.getStatus());
        assertEquals(3, Raichu.getTurnStatus());
    }

    @Test
    public void shouldGetCorrectTotalPs() {
        assertEquals(100, Raichu.getTotalPs());
        assertEquals(120, Venusaur.getTotalPs());
    }
    
    @Test
    public void shouldHandleNormalStatus() {
        Raichu.setStatus(0); // Normal
        int initialHP = Raichu.getPs();
        Raichu.applyEffectDamage();
        assertEquals(initialHP, Raichu.getPs()); // No damage when normal
    }
    
    @Test 
    public void shouldReturnEmptyListWhenNoPokemonAttacks() {
        Pokemon newPokemon = new Pokemon("Pikachu", 3, 90, 100, 50, 30, 40, 35, "Electrico", 95);
        assertTrue(newPokemon.getAtaques().isEmpty());
        assertTrue(newPokemon.getNombreAtaques().isEmpty());
    }

        @Test
    public void shouldSetAttacksFromArrayList() {
        Attack ataque1 = AttackFactory.getAttack("Impactrueno");
        Attack ataque2 = AttackFactory.getAttack("Esfuerzo");
        ArrayList<Attack> ataques = new ArrayList<>();
        ataques.add(ataque1);
        ataques.add(ataque2);
        
        Raichu.setAtaques(ataques);
        assertEquals(2, Raichu.getAtaques().size());
        assertTrue(Raichu.getAtaques().contains(ataque1));
        assertTrue(Raichu.getAtaques().contains(ataque2));
    
    }

    @Test
    public void shouldReturnCorrectAttackNamesWithNoAttacks() {
        Pokemon newPokemon = new Pokemon("Pikachu", 3, 90, 100, 50, 30, 40, 35, "Electrico", 95);
        assertTrue(newPokemon.getNombreAtaques().isEmpty());
    }



@Test
public void shouldApplyEffectDamageForSleep() {
    Raichu.setStatus(2); // Dormido
    Raichu.setTurnStatus(3); // Dormido por 3 turnos
    int initialHP = Raichu.getPs();
    Raichu.applyEffectDamage(); // Sleep doesn't deal damage
    assertEquals(initialHP, Raichu.getPs());
    assertEquals(2, Raichu.getStatus());
    assertEquals(3, Raichu.getTurnStatus());
}

@Test
public void shouldSetTotalPsCorrectly() {
    Raichu.setTotalPs(150);
    assertEquals(150, Raichu.getTotalPs());
    // También verifica que no afecta el ps actual si no lo cambias manualmente
    int currentPs = Raichu.getPs();
    Raichu.setTotalPs(200);
    assertEquals(200, Raichu.getTotalPs());
    assertEquals(currentPs, Raichu.getPs());
}
}