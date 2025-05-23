package domain;

import java.util.ArrayList;
import java.util.List;

public class StatusAttack extends Attack {
    public static final List<StatusAttack> ataquesStatus = new ArrayList<>();
    private AttributeType affects;
    private int turnosDuracion;
    
    public StatusAttack(String name, String type, int damage, int powerPoints, int precision, AttributeType affects, int turnosDuracion, String attackType) {
        super(name, type, damage, powerPoints, precision, attackType, affects.name());
        this.affects = affects;
        this.turnosDuracion = turnosDuracion;
    }
    
    public AttributeType getAffects() {
        return affects;
    }

    public int getTurnosDuracion() {
        return turnosDuracion;
    }

    public String getEffect() {
        return affects.name();
    }

    @Override
    public int calcDaño(Pokemon atacante, Pokemon defensor) {
        switch (affects) {

            default:
                // Mejora de stats o curación, se aplica al atacante
                System.out.println("[StatusAttack] " + atacante.getName() + " mejora " + affects + " en +10.");
                affects.apply(atacante, 10); // O el valor que desees
                break;
        }
        usarAtaque();
        return 0;
    }

    @Override
    public Attack clone() {
        return new StatusAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), affects, getTurnosDuracion(), getAttackType());
    }
}