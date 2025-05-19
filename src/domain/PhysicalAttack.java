package domain;

public class PhysicalAttack extends Attack {
    
    public PhysicalAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType) {
        super(name, type, baseDamage, powerPoint, precision, attackType, null); // Pasando null como efecto
    }
    
    // Sobrecarga del constructor para permitir especificar un efecto
    public PhysicalAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        super(name, type, baseDamage, powerPoint, precision, attackType, effect);
    }

    @Override
    public Attack clone() {
        return new PhysicalAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), "Physical", getEffect());
    }
}