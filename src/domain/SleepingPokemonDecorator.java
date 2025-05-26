package domain;

import java.util.Random;

/**
 * Decorador que añade el efecto de sueño a un Pokémon.
 * El Pokémon no podrá atacar mientras esté dormido.
 */
public class SleepingPokemonDecorator extends PokemonDecorator {
    
    private static final Random random = new Random();
    
    public SleepingPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(2);  // Establecer estado de sueño
        
        // Asignar turnos aleatorios de sueño (1-3 turnos)
        if (pokemon.getTurnStatus() <= 0) {
            pokemon.setTurnStatus(random.nextInt(3) + 1);
        }
    }
    
    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar si el Pokémon está dormido
        int turnsAsleep = pokemon.getTurnStatus();
        
        // Reducir contador de turnos
        pokemon.setTurnStatus(turnsAsleep - 1);
        
        // Si aún está dormido, no puede atacar
        if (turnsAsleep > 0) {
            System.out.println(pokemon.getName() + " está dormido y no puede atacar.");
            return 0;
        }
        
        // Si el contador llegó a 0, el Pokémon despierta
        if (turnsAsleep == 1) {
            System.out.println(pokemon.getName() + " ha despertado.");
            pokemon.setStatus(0);  // Restablecer estado normal
        }
        
        // Si ya despertó, puede atacar normalmente
        return super.attack(defensor, attack);
    }
    
    /**
     * Obitene el nombre del Pokémon con el estado de sueño añadido.
     */
    @Override
    public String getName() {
        return pokemon.getName() + " (Dormido)";
    }
}