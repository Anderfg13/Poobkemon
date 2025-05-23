package domain;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

public enum AttributeType {
    HP((p, v) -> p.setPs(Math.min(p.getPs() + v, p.getTotalPs())), p -> p.getPs() > 0),
    PHYSICAL_ATTACK((p, v) -> p.setPhysicalAttack(p.getPhysicalAttack() + v), p -> true),
    PHYSICAL_DEFENSE((p, v) -> p.setPhysicalDefense(p.getPhysicalDefense() + v), p -> true),
    SPECIAL_ATTACK((p, v) -> p.setSpecialAttack(p.getSpecialAttack() + v), p -> true),
    SPECIAL_DEFENSE((p, v) -> p.setSpecialDefense(p.getSpecialDefense() + v), p -> true),
    SPEED((p, v) -> p.setSpeed(p.getSpeed() + v), p -> true),
    EVASION((p, v) -> p.setEvasion(p.getEvasion() + v), p -> true);
    // Puedes seguir agregando más atributos y lógica según lo necesites

    private final BiConsumer<Pokemon, Integer> effect;
    private final Predicate<Pokemon> condition;

    AttributeType(BiConsumer<Pokemon, Integer> effect, Predicate<Pokemon> condition) {
        this.effect = effect;
        this.condition = condition;
    }

    public void apply(Pokemon p, int value) {
        if (condition.test(p)) {
            effect.accept(p, value);
        }
    }
}