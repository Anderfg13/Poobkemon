package domain;

/**
 * Decorador que añade el efecto de congelación a un Pokémon.
 * El Pokémon no podrá atacar mientras esté congelado, con probabilidad de descongelarse.
 */
public class FrozenPokemonDecorator extends PokemonDecorator {
    
    private static final double THAW_CHANCE = 0.20;  // 20% de probabilidad de descongelarse
    
    public FrozenPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(4);  // Establecer estado de congelado
    }

    /*
     * Realiza un ataque del Pokémon congelado.
     * Si el Pokémon se descongela, puede atacar normalmente.
     */
    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar si el Pokémon se descongela
        if (Math.random() < THAW_CHANCE) {
            System.out.println(pokemon.getName() + " se ha descongelado.");
            pokemon.setStatus(0);  // Restablecer estado normal
            return super.attack(defensor, attack);
        }
        
        // Si sigue congelado, no puede atacar
        System.out.println(pokemon.getName() + " está congelado y no puede atacar.");
        return 0;
    }
    
    /**
     * Aplica el daño del efecto de congelación al final del turno.
     * Existe una probabilidad de descongelarse al final del turno.
     */
    @Override
    public void applyEffectDamage() {
        // Verificar si el Pokémon se descongela al final del turno
        if (Math.random() < THAW_CHANCE) {
            System.out.println(pokemon.getName() + " se ha descongelado.");
            pokemon.setStatus(0);  // Restablecer estado normal
        }
        
        // Llamar al método de la clase padre para mantener el comportamiento de delegación
        super.applyEffectDamage();
    }
    
    /**
     * Obtiene el nombre del Pokémon congelado.
     * 
     * @return Nombre del Pokémon con el estado de congelación añadido.
     */
    @Override
    public String getName() {
        return pokemon.getName() + " (Congelado)";
    }
}