package domain;

/**
 * Decorador que añade el efecto de envenenamiento a un Pokémon.
 * El Pokémon recibirá daño progresivo cada turno.
 */
public class PoisonedPokemonDecorator extends PokemonDecorator {
    
    private int poisonTurns = 0;  // Contador de turnos envenenado
    
    public PoisonedPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(5);  // Establecer estado de envenenado
    }
    
    @Override
    public void applyEffectDamage() {
        // Incrementar contador de turnos
        poisonTurns++;
        
        // El daño por veneno aumenta con cada turno: 1/16, 2/16, 3/16...
        int baseDamage = Math.max(1, pokemon.getTotalPs() / 16);
        int poisonDamage = baseDamage * Math.min(poisonTurns, 8);  // Máximo 8/16 de daño
        
        // Aplicar daño por envenenamiento
        int currentPs = pokemon.getPs();
        int newPs = Math.max(1, currentPs - poisonDamage);  // Asegurar que no baje de 1 PS
        
        System.out.println(pokemon.getName() + " sufre por envenenamiento " + 
                          poisonDamage + " puntos de daño.");
                          
        pokemon.setPs(newPs);
        
        // Llamar al método de la clase padre para mantener el comportamiento de delegación
        super.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Envenenado)";
    }
    
    /**
     * Devuelve el número de turnos que el Pokémon ha estado envenenado.
     */
    public int getPoisonTurns() {
        return poisonTurns;
    }
}