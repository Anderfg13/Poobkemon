package domain;

/**
 * Decorador que añade el efecto de parálisis a un Pokémon.
 */
public class ParalyzedPokemonDecorator extends PokemonDecorator {
    private static final double PARALYSIS_CHANCE = 0.25; // 25% de probabilidad de no moverse
    
    public ParalyzedPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(1);  // Establecer estado de parálisis
    }
    
    /**
     * Aplica el daño del efecto de parálisis al final del turno.
     * La parálisis reduce la velocidad del Pokémon a la mitad.
     * @return La velocidad reducida del Pokémon paralizado.
     */
    @Override
    public int getSpeed() {
        // La parálisis reduce la velocidad a la mitad
        return pokemon.getSpeed() / 2;
    }
    
    /**
     * Realiza un ataque del Pokémon paralizado.
     * Si el Pokémon está paralizado, hay una probabilidad de que no pueda atacar.
     * @param defensor El Pokémon defensor al que se ataca.
     * @param attack El ataque que se intenta realizar.
     * @return El daño infligido, o 0 si no puede atacar.
     */
    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar si puede atacar (probabilidad de parálisis)
        if (Math.random() < PARALYSIS_CHANCE) {
            // El Pokémon está paralizado y no puede moverse este turno
            return 0;
        }
        
        // Si no está paralizado este turno, ataca normalmente
        return super.attack(defensor, attack);
    }
    
    /**
     * Aplica el daño del efecto de parálisis al final del turno.
     * No se aplica daño adicional, pero se puede verificar si el Pokémon sigue paralizado.
     */
    @Override
    public String getName() {
        return pokemon.getName() + " (Paralizado)";
    }
}