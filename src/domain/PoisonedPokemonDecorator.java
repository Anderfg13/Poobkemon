package domain;

/**
 * Decorador que añade el efecto de veneno a un Pokémon.
 * Los Pokémon envenenados reciben daño progresivamente mayor cada turno.
 */
public class PoisonedPokemonDecorator extends PokemonDecorator {
    private int poisonTurns;
    
    public PoisonedPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(5);
        this.poisonTurns = 0;
    }
    
    @Override
    public void applyEffectDamage() {
        // Incrementar turnos de veneno
        poisonTurns++;
        
        // Daño base por veneno (1/8 de la vida total) + daño progresivo
        int basePoisonDamage = Math.max(1, pokemon.getTotalPs() / 8);
        int progressiveDamage = poisonTurns * (pokemon.getTotalPs() / 32); // Daño adicional por turno
        int totalDamage = basePoisonDamage + progressiveDamage;
        
        pokemon.setPs(pokemon.getPs() - totalDamage);
        
        // Continuar con otros efectos
        pokemon.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Envenenado)";
    }
    
    public int getPoisonTurns() {
        return poisonTurns;
    }
}