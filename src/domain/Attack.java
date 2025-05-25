package domain;

import java.io.Serializable;
import java.util.Random;
/**
 * Attack representa un ataque que puede ser utilizado por un Poobkemon durante una batalla.
 * Esta clase es abstracta y debe ser extendida para definir ataques concretos.
 * 
 * <p>Cada ataque tiene un nombre, tipo elemental, daño base, precisión, puntos de poder (PP), 
 * tipo de ataque (físico, especial o de estado) y un posible efecto especial.
 * 
 * <p>Proporciona métodos para calcular el daño, usar el ataque, obtener información relevante
 * y clonar el ataque.
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public abstract class Attack implements Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected String type;
    protected int baseDamage;
    protected int precision;
    protected int powerPoint;
    private String attackType;
    protected String effect; // Nuevo campo para el efecto del ataque
    /**
     * Crea un nuevo ataque con los parámetros especificados.
     *
     * @param name        Nombre del ataque.
     * @param type        Tipo elemental del ataque.
     * @param baseDamage  Daño base del ataque.
     * @param powerPoint  Puntos de poder iniciales del ataque.
     * @param precision   Precisión del ataque (0-100).
     * @param attackType  Tipo de ataque (Physical, Special, Status).
     * @param effect      Efecto especial del ataque.
     */
    public Attack(String name, String type, int baseDamage, int powerPoint, int precision, String attackType, String effect) {
        this.name = name;
        this.type = type;
        this.baseDamage = baseDamage;
        this.powerPoint = powerPoint;
        this.precision = precision;
        this.attackType = attackType;
        this.effect = effect;
    }
    /**
     * Obtiene el nombre del ataque.
     * @return Nombre del ataque.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene el daño base del ataque.
     * @return Daño base.
     */
    public int getBaseDamage() {
        return baseDamage;
    }
    /**
     * Obtiene el tipo elemental del ataque.
     * @return Tipo elemental.
     */
    public String getType() {
        return type;
    }
    /**
     * Obtiene el tipo elemental del ataque.
     * @return Tipo elemental.
     */
    public int getPrecision() {
        return precision;
    }
    /**
     * Establece los puntos de poder (PP) del ataque.
     * @param pp Nuevo valor de PP.
     */    
    public void setPowerPoint(int pp) {
    	this.powerPoint = pp;
    }
    /**
     * Obtiene los puntos de poder (PP) actuales del ataque.
     * @return Puntos de poder restantes.
     */
    public int getPowerPoint() {
        return powerPoint;
    }
    /**
     * Reduce en 1 los puntos de poder (PP) del ataque, sin bajar de 0.
     */
    public void usarAtaque() {
        this.powerPoint = Math.max(this.powerPoint - 1, 0);
    }
    /**
     * Obtiene el tipo de ataque (Physical, Special, Status).
     * @return Tipo de ataque.
     */
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
    /**
     * Calcula el daño infligido por este ataque de un atacante a un defensor.
     * Considera la precisión, la efectividad de tipos y las estadísticas de los Poobkemon.
     *
     * @param atacante Poobkemon que realiza el ataque.
     * @param defensor Poobkemon que recibe el ataque.
     * @return Daño infligido (0 si falla o no quedan PP).
     */
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
    
    /**
     * Crea y retorna una copia profunda de este ataque.
     * Debe ser implementado por las subclases.
     * 
     * @return Una nueva Attack con los mismos valores.
     */
    @Override
    public abstract Attack clone();
}
