package domain;

import java.util.List;

/**
 * Decorador abstracto que mantiene una referencia al Pokémon que está decorando.
 */
public abstract class PokemonDecorator implements PokemonBase {
    protected PokemonBase pokemon;
    
    public PokemonDecorator(PokemonBase pokemon) {
        this.pokemon = pokemon;
    }
    
    @Override
    public String getName() { return pokemon.getName(); }
    
    @Override
    public String getType() { return pokemon.getType(); }
    
    @Override
    public int getId() { return pokemon.getId(); }
    
    @Override
    public int getTotalPs() { return pokemon.getTotalPs(); }
    
    @Override
    public int getPs() { return pokemon.getPs(); }
    
    @Override
    public int getSpeed() { return pokemon.getSpeed(); }
    
    @Override
    public int getEvasion() { return pokemon.getEvasion(); }
    
    @Override
    public int getSpecialAttack() { return pokemon.getSpecialAttack(); }
    
    @Override
    public int getPhysicalAttack() { return pokemon.getPhysicalAttack(); }
    
    @Override
    public int getSpecialDefense() { return pokemon.getSpecialDefense(); }
    
    @Override
    public int getPhysicalDefense() { return pokemon.getPhysicalDefense(); }
    
    @Override
    public int getStatus() { return pokemon.getStatus(); }
    
    @Override
    public int getTurnStatus() { return pokemon.getTurnStatus(); }
    
    @Override
    public void setPs(int ps) { pokemon.setPs(ps); }
    
    @Override
    public void setSpeed(int speed) { pokemon.setSpeed(speed); }
    
    @Override
    public void setEvasion(int evasion) { pokemon.setEvasion(evasion); }
    
    @Override
    public void setSpecialAttack(int specialAttack) { pokemon.setSpecialAttack(specialAttack); }
    
    @Override
    public void setSpecialDefense(int specialDefense) { pokemon.setSpecialDefense(specialDefense); }
    
    @Override
    public void setPhysicalAttack(int physicalAttack) { pokemon.setPhysicalAttack(physicalAttack); }
    
    @Override
    public void setPhysicalDefense(int physicalDefense) { pokemon.setPhysicalDefense(physicalDefense); }
    
    @Override
    public void setStatus(int status) { pokemon.setStatus(status); }
    
    @Override
    public void setTurnStatus(int turnStatus) { pokemon.setTurnStatus(turnStatus); }
    
    @Override
    public void addAttack(Attack attack) { pokemon.addAttack(attack); }
    
    @Override
    public List<Attack> getAtaques() { return pokemon.getAtaques(); }
    
    @Override
    public List<String> getNombreAtaques() { return pokemon.getNombreAtaques(); }
    
    @Override
    public void applyEffectDamage() { pokemon.applyEffectDamage(); }
    
    @Override
    public int attack(PokemonBase defensor, Attack attack) { 
        return pokemon.attack(defensor, attack); 
    }
}