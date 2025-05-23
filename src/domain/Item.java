package domain;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Item representa un objeto utilizable en el juego Poobkemon, capaz de modificar atributos de un {@link Pokemon}.
 * Cada ítem tiene un nombre, descripción, valor de efecto y un atributo objetivo sobre el que actúa.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite aplicar efectos como curación, aumento de estadísticas o revivir a un Pokémon.</li>
 *   <li>Define reglas para el uso de cada ítem según el estado del Pokémon objetivo.</li>
 *   <li>Utiliza el enumerado {@link AttributeType} para encapsular la lógica de modificación de atributos.</li>
 *   <li>Incluye métodos para verificar si el ítem puede ser usado en un Pokémon específico.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Item {
    private String name;
    private String description;
    private int effectValue;
    private AttributeType applyTo;

    public Item(String name, String description, int effectValue, AttributeType applyTo) {
        this.name = name;
        this.description = description;
        this.effectValue = effectValue;
        this.applyTo = applyTo;
    }

    /**
     * Devuelve el nombre del ítem.
     * @return Nombre del ítem.
     */
    public String getName() {
        return name;
    }

    /**
     * Devuelve la descripción del ítem.
     * @return Descripción del ítem.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Devuelve el valor del efecto que aplica el ítem.
     * @return Valor del efecto.
     */
    public int getEffectValue() {
        return effectValue;
    }

    /**
     * Devuelve el atributo objetivo sobre el que actúa el ítem.
     * @return Atributo objetivo.
     */
    public AttributeType getApplyTo() {
        return applyTo;
    }

    /**
     * Aplica el efecto del ítem al Pokémon especificado.
     * @param pokemon Pokémon objetivo.
     * @throws PoobkemonException Si el ítem no puede ser aplicado por el estado del Pokémon.
     */
    public void applyItemEffect(Pokemon pokemon) throws PoobkemonException {
        if (pokemon.getPs() == 0 && this.name != "Revive") {
            throw new PoobkemonException(PoobkemonException.CANT_USE_ITEM_ON_POKEMON_FAINTED);
        }
        System.out.println("Aplicando " + name + " a " + pokemon.getName());
        applyTo.apply(pokemon, effectValue);
    }

    /**
     * Verifica si el ítem puede ser usado sobre el Pokémon objetivo.
     * @param target Pokémon objetivo.
     * @param pokemonsMuertos Lista de Pokémon muertos (para ítems de revivir).
     * @return {@code true} si el ítem puede ser usado, {@code false} en caso contrario.
     */
    public boolean canUse(Pokemon target, List<Pokemon> pokemonsMuertos) {
        if (name.equalsIgnoreCase("Revivir")) {
            // Revivir solo puede usarse si hay pokémon muertos
            return !pokemonsMuertos.isEmpty();
        } else if (applyTo == AttributeType.HP) {
            // Pociones solo pueden usarse si el Pokémon no tiene la vida completa
            return target.getPs() < target.getTotalPs();
        }
        // Otros ítems pueden tener reglas adicionales
        return true;
    }

    /**
     * AttributeType define los diferentes atributos que pueden ser modificados por un ítem.
     * Cada tipo de atributo encapsula la lógica para aplicar su efecto y la condición para su uso.
     */
    public enum AttributeType {
        HP((p, v) -> p.setPs(Math.min(p.getPs() + v, p.getTotalPs())), p -> p.getPs() > 0),
        REVIVE((p, v) -> p.setPs(v), p -> p.getPs() == 0),
        PHYSICAL_ATTACK((p, v) -> p.setPhysicalAttack(p.getPhysicalAttack() + v), p -> true),
        PHYSICAL_DEFENSE((p, v) -> p.setPhysicalDefense(p.getPhysicalDefense() + v), p -> true),
        SPECIAL_ATTACK((p, v) -> p.setSpecialAttack(p.getSpecialAttack() + v), p -> true),
        SPECIAL_DEFENSE((p, v) -> p.setSpecialDefense(p.getSpecialDefense() + v), p -> true),
        SPEED((p, v) -> p.setSpeed(p.getSpeed() + v), p -> true),
        EVASION((p, v) -> p.setEvasion(p.getEvasion() + v), p -> true);

        private final BiConsumer<Pokemon, Integer> applier;
        private final Predicate<Pokemon> canApply;

        /**
         * Crea un tipo de atributo con la lógica de aplicación y la condición de uso.
         * @param applier Lógica para modificar el atributo en el Pokémon.
         * @param canApply Condición para aplicar el efecto.
         */
        AttributeType(BiConsumer<Pokemon, Integer> applier, Predicate<Pokemon> canApply) {
            this.applier = applier;
            this.canApply = canApply;
        }

        /**
         * Aplica el efecto de este atributo al Pokémon dado, si cumple la condición.
         * @param p Pokémon objetivo.
         * @param value Valor a modificar en el atributo.
         */
        public void apply(Pokemon p, int value) {
            if (!canApply.test(p)) {
                throw new IllegalStateException(
                    String.format("No se puede usar %s sobre %s en su estado actual (PS=%d).",
                                  this.name(), p.getName(), p.getPs())
                );
            }
            applier.accept(p, value);
        }
    }
}
