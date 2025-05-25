package domain;

/**
 * Decorador que añade el efecto de parálisis a un Pokémon.
 * Los Pokémon paralizados tienen una probabilidad de no poder atacar.
 */
public class ParalyzedPokemonDecorator extends PokemonDecorator {
    private static final double PARALYSIS_CHANCE = 0.25; // 25% de probabilidad de no poder moverse
    
    public ParalyzedPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        // Establecer el estado de parálisis
        pokemon.setStatus(1);
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
            // El Pokémon está paralizado y no puede moverse
            return 0;
        }
        
        // Si no está paralizado este turno, ataca normalmente
        return pokemon.attack(defensor, attack);
    }
    
    @Override
    public void applyEffectDamage() {
        // La parálisis no causa daño directo
        pokemon.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Paralizado)";
    }
}