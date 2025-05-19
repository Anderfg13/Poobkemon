package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class Attack {
    protected String name;
    protected String type;
    protected int baseDamage;
    protected int precision;
    protected int powerPoint;
    private String attackType;
    protected String effect; // Nuevo campo para el efecto del ataque
    
    public Attack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        this.name = name;
        this.type = type;
        this.baseDamage = baseDamage;
        this.powerPoint = powerPoint;
        this.precision = precision;
        this.attackType = attackType;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    
    public int getBaseDamage() {
        return baseDamage;
    }

    public String getType() {
        return type;
    }

    public int getPrecision() {
        return precision;
    }
    
    public void setPowerPoint(int pp) {
    	this.powerPoint = pp;
    }

    public int getPowerPoint() {
        return powerPoint;
    }

    public void usarAtaque() {
        this.powerPoint = Math.max(this.powerPoint - 1, 0);
    }

    public String getAttackType() {
        return this.attackType;
    }
    
    /**
     * Obtiene el efecto especial del ataque.
     * @return El efecto del ataque, o null si no tiene.
     */
    public String getEffect() {
        return effect;
    }
    
    /**
     * Obtiene el máximo de puntos de poder del ataque.
     * @return El máximo de PP del ataque.
     */
    public int getMaxPowerPoint() {
        // Valores predeterminados según el tipo de ataque
        // Puedes ajustar esto según tus necesidades
        switch (attackType) {
            case "Physical":
            case "Special":
                return 15;
            case "Status":
                return 20;
            default:
                return 10;
        }
    }

    public int calcDaño(Pokemon atacante, Pokemon defensor) {
        if (powerPoint <= 0) return 0;

        Random rand = new Random();
        if (rand.nextInt(100) + 1 > precision) {
            System.out.println("El ataque falló debido a la precisión.");
            return 0;
        }

        Integer tipoAtaque = efectivity.numberType.get(this.getType());
        Integer tipoDefensor = efectivity.numberType.get(defensor.getType());
        if (tipoAtaque == null) {
            throw new RuntimeException("Tipo de ataque no reconocido: " + this.getType());
        }
        if (tipoDefensor == null) {
            throw new RuntimeException("Tipo de defensor no reconocido: " + defensor.getType());
        }
        double efectividad = efectivity.efectividad(tipoAtaque, tipoDefensor);
        int danioBase = (int) ((atacante.getSpecialAttack() * baseDamage * efectividad) / defensor.getSpecialDefense());
        danioBase = Math.max(danioBase, 1); // Mínimo 1 de daño

        System.out.println("Efectividad: " + efectividad + ", Daño calculado: " + danioBase);
        usarAtaque();
        return danioBase;
    }

    @Override
    public abstract Attack clone();
}
