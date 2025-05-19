package domain;

public class SpecialAttack extends Attack {

    public SpecialAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType) {
        super(name, type, baseDamage, powerPoint, precision, attackType, null); // Add null for effect parameter
    }
    
    // Optional: Add an overloaded constructor that accepts an effect
    public SpecialAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        super(name, type, baseDamage, powerPoint, precision, attackType, effect);
    }

    @Override
    public Attack clone() {
        return new SpecialAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), "Special", getEffect());
    }
}