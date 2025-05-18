package domain;

import java.util.ArrayList;
import java.util.List;


public class StatusAttack extends Attack {
    public static final List<StatusAttack> ataquesStatus = new ArrayList<>();
    private String affects;
    private int turnosDuracion;
    
    public StatusAttack(String name, String type, int damage, int powerPoints, int precision, String affects, int turnosDuracion, String attackType) {
        super(name, type, damage, powerPoints, precision, attackType);
        this.affects = affects;
        this.turnosDuracion = turnosDuracion;
    }
    
    public String getAffects() {
        return affects;
    }

    public int getTurnosDuracion() {
        return turnosDuracion;
    }

    public String getEffect() {
        return affects;
    }

    @Override
    public int calcDaño(Pokemon atacante, Pokemon defensor) {
        // No aplica daño directo, pero modifica atributos
        if (affects.equals("PhysicalDefense")) {
            defensor.setPhysicalDefense(defensor.getPhysicalDefense() + 10); // Ejemplo
        } else if (affects.equals("Speed")) {
            defensor.setSpeed(Math.max(defensor.getSpeed() - 10, 0)); // Ejemplo
        }
        usarAtaque();
        return 0;
    }

    @Override
    public Attack clone() {
        return new StatusAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), getEffect(), getTurnosDuracion(), getAttackType());
    }
}