package domain;

/**
 * Decorador que añade el efecto de sueño a un Pokémon.
 * Los Pokémon dormidos no pueden atacar hasta que despierten.
 */
public class SleepingPokemonDecorator extends PokemonDecorator {
    private int turnsAsleep;
    private static final int MAX_SLEEP_TURNS = 3; // Máximo 3 turnos dormido
    
    public SleepingPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(2);
        this.turnsAsleep = 1 + (int)(Math.random() * MAX_SLEEP_TURNS); // 1-3 turnos
        pokemon.setTurnStatus(this.turnsAsleep);
    }
    
    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar si aún está dormido
        if (pokemon.getTurnStatus() > 0) {
            // Reducir turnos de sueño
            pokemon.setTurnStatus(pokemon.getTurnStatus() - 1);
            
            // Si aún le quedan turnos, no puede atacar
            if (pokemon.getTurnStatus() > 0) {
                return 0; // No puede atacar mientras duerme
            } else {
                // Despierta en este turno
                pokemon.setStatus(0);
            }
        }
        
        // Si despertó, puede atacar
        return pokemon.attack(defensor, attack);
    }
    
    @Override
    public void applyEffectDamage() {
        // El sueño no causa daño directo, pero reduce turnos
        if (pokemon.getTurnStatus() > 0) {
            pokemon.setTurnStatus(pokemon.getTurnStatus() - 1);
            if (pokemon.getTurnStatus() <= 0) {
                pokemon.setStatus(0); // Despierta
            }
        }
        pokemon.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Dormido)";
    }
}