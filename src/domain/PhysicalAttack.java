package domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PhysicalAttack extends Attack {
    
    public PhysicalAttack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType) {
        super(name, type, baseDamage, powerPoint, precision, attackType);
    }

    @Override
    public Attack clone() {
        return new PhysicalAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), "Physical");
    }
    
}