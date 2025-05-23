package domain;

import java.io.Serializable;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * AttributeType define los diferentes atributos que pueden ser modificados en un {@link Pokemon}
 * durante el juego Poobkemon. Cada tipo de atributo tiene una lógica asociada para aplicar su efecto
 * y una condición para determinar si puede ser aplicado.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite modificar atributos como vida, ataque, defensa, velocidad, evasión, etc.</li>
 *   <li>Cada atributo define cómo se aplica el cambio y bajo qué condición.</li>
 *   <li>Utiliza expresiones lambda para encapsular la lógica de modificación y condición.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public enum AttributeType implements Serializable {
    HP((p, v) -> p.setPs(Math.min(p.getPs() + v, p.getTotalPs())), p -> p.getPs() > 0),
    PHYSICAL_ATTACK((p, v) -> p.setPhysicalAttack(p.getPhysicalAttack() + v), p -> true),
    PHYSICAL_DEFENSE((p, v) -> p.setPhysicalDefense(p.getPhysicalDefense() + v), p -> true),
    SPECIAL_ATTACK((p, v) -> p.setSpecialAttack(p.getSpecialAttack() + v), p -> true),
    SPECIAL_DEFENSE((p, v) -> p.setSpecialDefense(p.getSpecialDefense() + v), p -> true),
    SPEED((p, v) -> p.setSpeed(p.getSpeed() + v), p -> true),
    EVASION((p, v) -> p.setEvasion(p.getEvasion() + v), p -> true);
    // Puedes seguir agregando más atributos y lógica según lo necesites

    /** Lógica para aplicar el efecto del atributo sobre un Pokémon. */
    private final BiConsumer<Pokemon, Integer> effect;
    /** Condición que debe cumplirse para aplicar el efecto. */
    private final Predicate<Pokemon> condition;

    /**
     * Crea un tipo de atributo con la lógica de efecto y condición especificadas.
     *
     * @param effect    Lógica para modificar el atributo en el Pokémon.
     * @param condition Condición para aplicar el efecto.
     */
    AttributeType(BiConsumer<Pokemon, Integer> effect, Predicate<Pokemon> condition) {
        this.effect = effect;
        this.condition = condition;
    }

    /**
     * Aplica el efecto de este atributo al Pokémon dado, si cumple la condición.
     *
     * @param p     Pokémon al que se le aplica el efecto.
     * @param value Valor a modificar en el atributo.
     */
    public void apply(Pokemon p, int value) {
        if (condition.test(p)) {
            effect.accept(p, value);
        }
    }
}