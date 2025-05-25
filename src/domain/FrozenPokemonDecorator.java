package domain;

/**
 * Decorador que añade el efecto de congelación a un Pokémon.
 * Los Pokémon congelados no pueden atacar hasta que se descongelen.
 */
public class FrozenPokemonDecorator extends PokemonDecorator {
    private static final double THAW_CHANCE = 0.20; // 20% de probabilidad de descongelarse cada turno
    
    public FrozenPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(4);
    }
    
    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar si se descongela
        if (Math.random() < THAW_CHANCE) {
            // Se descongela y puede atacar
            pokemon.setStatus(0);
            return pokemon.attack(defensor, attack);
        }
        
        // Sigue congelado, no puede atacar
        return 0;
    }
    
    @Override
    public void applyEffectDamage() {
        // La congelación no causa daño directo
        // Verificar si se descongela al final del turno
        if (Math.random() < THAW_CHANCE) {
            pokemon.setStatus(0);
        }
        pokemon.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Congelado)";
    }
}