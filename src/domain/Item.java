package domain;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

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

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getEffectValue() {
        return effectValue;
    }

    public AttributeType getApplyTo() {
        return applyTo;
    }

    public void applyItemEffect(Pokemon pokemon) throws PoobkemonException {
        if (pokemon.getPs() == 0 && this.name != "Revive") {
            throw new PoobkemonException(PoobkemonException.CANT_USE_ITEM_ON_POKEMON_FAINTED);
        }
        System.out.println("Aplicando " + name + " a " + pokemon.getName());
        applyTo.apply(pokemon, effectValue);
    }

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

        AttributeType(BiConsumer<Pokemon, Integer> applier, Predicate<Pokemon> canApply) {
            this.applier = applier;
            this.canApply = canApply;
        }

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
