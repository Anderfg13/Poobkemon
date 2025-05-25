package domain;

/**
 * SpecialAttack representa un ataque especial en el juego Poobkemon.
 * Hereda de la clase Attack y modela movimientos que infligen daño basado en la estadística de ataque especial del Pokémon.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite crear ataques especiales con nombre, tipo, daño base, puntos de poder (PP), precisión y tipo de ataque.</li>
 *   <li>Soporta la inclusión opcional de un efecto especial asociado al ataque.</li>
 *   <li>Incluye un métodoclone() para crear una copia exacta del ataque especial.</li>
 * </ul>
 *

 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class SpecialAttack extends Attack {

    public SpecialAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType) {
        super(name, type, baseDamage, powerPoint, precision, attackType, null); // Add null for effect parameter
    }
    
    // Optional: Add an overloaded constructor that accepts an effect
    public SpecialAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        super(name, type, baseDamage, powerPoint, precision, attackType, effect);
    }

    /**
     * Calcula el daño infligido por este ataque especial.
     * Utiliza la estadística de ataque especial del Pokémon atacante y la defensa especial del Pokémon defensor.
     *
     * @param attacker El Pokémon que realiza el ataque.
     * @param defender El Pokémon que recibe el ataque.
     * @return El daño calculado que se inflige al defensor.
     */
    @Override
    public Attack clone() {
        return new SpecialAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), "Special", getEffect());
    }
}