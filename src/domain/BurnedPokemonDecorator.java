package domain;

/**
 * Decorador que añade el efecto de quemadura a un Pokémon.
 * Los Pokémon quemados reciben daño cada turno y tienen reducido su ataque físico.
 */
public class BurnedPokemonDecorator extends PokemonDecorator {
    
    public BurnedPokemonDecorator(PokemonBase pokemon) {
        super(pokemon);
        pokemon.setStatus(3);
    }
    
    @Override
    public int getPhysicalAttack() {
        // La quemadura reduce el ataque físico a la mitad
        return pokemon.getPhysicalAttack() / 2;
    }
    
    @Override
    public void applyEffectDamage() {
        // Aplicar daño por quemadura (1/16 de la vida total)
        int burnDamage = Math.max(1, pokemon.getTotalPs() / 16);
        pokemon.setPs(pokemon.getPs() - burnDamage);
        
        // Continuar con otros efectos
        pokemon.applyEffectDamage();
    }
    
    @Override
    public String getName() {
        return pokemon.getName() + " (Quemado)";
    }
}