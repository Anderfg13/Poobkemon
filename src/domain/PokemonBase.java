package domain;

import java.io.Serializable;
import java.util.List;

/**
 * Interfaz componente para el patrón Decorator
 * Define los métodos básicos que deben implementar tanto los Pokémon como sus decoradores
 */
public interface PokemonBase extends Serializable {
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
    void setPhysicalAttack(int physicalAttack);
    void setSpecialDefense(int specialDefense);
    void setPhysicalDefense(int physicalDefense);
    void setStatus(int status);
    void setTurnStatus(int turnStatus);
    
    void addAttack(Attack attack);
    List<Attack> getAtaques();
    List<String> getNombreAtaques();
    void applyEffectDamage();
    int attack(PokemonBase defensor, Attack attack);
    boolean hasStatus(int statusCode);
}