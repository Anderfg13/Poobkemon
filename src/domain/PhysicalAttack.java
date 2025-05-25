package domain;

/**
 * {@code PhysicalAttack} representa un ataque físico en el juego Poobkemon.
 * Hereda de la clase {@link Attack} y modela movimientos que infligen daño basado en la estadística de ataque físico del Pokémon.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite crear ataques físicos con nombre, tipo, daño base, puntos de poder (PP), precisión y tipo de ataque.</li>
 *   <li>Soporta la inclusión opcional de un efecto especial asociado al ataque.</li>
 *   <li>Incluye un método {@code clone()} para crear una copia exacta del ataque físico.</li>
 * </ul>
 *
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class PhysicalAttack extends Attack {
    
    public PhysicalAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType) {
        super(name, type, baseDamage, powerPoint, precision, attackType, null); // Pasando null como efecto
    }
    
    // Sobrecarga del constructor para permitir especificar un efecto
    public PhysicalAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        super(name, type, baseDamage, powerPoint, precision, attackType, effect);
    }

    /**
     * Calcula el daño infligido por este ataque físico.
     * Utiliza la estadística de ataque físico del Pokémon atacante y la defensa física del Pokémon defensor.
     *
     * @param attacker El Pokémon que realiza el ataque.
     * @param defender El Pokémon que recibe el ataque.
     * @return El daño calculado que se inflige al defensor.
     */
    @Override
    public Attack clone() {
        return new PhysicalAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), "Physical", getEffect());
    }
}