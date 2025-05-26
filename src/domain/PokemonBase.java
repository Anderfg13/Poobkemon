package domain;

import java.util.List;

/**
 * Interfaz base para el patrón Decorator.
 * Define las operaciones comunes para Pokémon y sus estados.
 */
public interface PokemonBase {
    String getName();
    String getType();
    int getId();
    int getTotalPs();
    int getPs();
    int getSpeed();
    int getEvasion();
    int getSpecialAttack();
    int getPhysicalAttack();
    int getSpecialDefense();
    int getPhysicalDefense();
    int getStatus();
    int getTurnStatus();
    
    void setPs(int ps);
    void setSpeed(int speed);
    void setEvasion(int evasion);
    void setSpecialAttack(int specialAttack);
    void setSpecialDefense(int specialDefense);
    void setPhysicalAttack(int physicalAttack);
    void setPhysicalDefense(int physicalDefense);
    void setStatus(int status);
    void setTurnStatus(int turnStatus);
    
    void addAttack(Attack attack);
    List<Attack> getAtaques();
    List<String> getNombreAtaques();
    
    void applyEffectDamage();
    int attack(PokemonBase defensor, Attack attack);
}