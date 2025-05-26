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
    
    @Override
    public int getSpeed() {
        // La parálisis reduce la velocidad a la mitad
        return pokemon.getSpeed() / 2;
    }
    
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
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Paralizado)";
    }
}