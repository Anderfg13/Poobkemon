package domain;

import java.util.ArrayList;

public class BattleArenaNormal extends BattleArena {

    public BattleArenaNormal() {
        super();
    }


    /**
     * Aplica los efectos de estado a los Pokémon activos de ambos entrenadores.
     */
    private void applyStatusEffects() {
        for (Coach coach : getCoaches()) {
            Pokemon activePokemon = coach.getActivePokemon();
            if (activePokemon.getStatus() != 0) {
                System.out.println("Pokémon " + activePokemon.getName() + " está afectado por su estado.");
                activePokemon.applyEffectDamage();
            }
        }
    }


    
}