package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * StatusAttack representa un ataque de estado en el juego Poobkemon.
 * Hereda de la clase Attack y modela movimientos que alteran atributos, aplican estados alterados
 * o modifican condiciones de batalla en lugar de infligir daño directo.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite crear ataques de estado que afectan atributos específicos de un Pokémon durante un número determinado de turnos.</li>
 *   <li>Almacena el tipo de atributo afectado y la duración del efecto en turnos.</li>
 *   <li>Incluye un método calcDaño que aplica el efecto correspondiente en vez de causar daño directo.</li>
 *   <li>Incluye un método clone() para crear una copia exacta del ataque de estado.</li>
 *   <li>Gestiona una lista estática de todos los ataques de estado registrados.</li>
 * </ul>
 *
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class StatusAttack extends Attack {
    public static final List<StatusAttack> ataquesStatus = new ArrayList<>();
    private AttributeType affects;
    private int turnosDuracion;
    
    public StatusAttack(String name, String type, int damage, int powerPoints, int precision, AttributeType affects, int turnosDuracion, String attackType) {
        super(name, type, damage, powerPoints, precision, attackType, affects.name());
        this.affects = affects;
        this.turnosDuracion = turnosDuracion;
    }
    
    /**
     * Obtiene el tipo de atributo que este ataque de estado afecta.
     * @return
     */
    public AttributeType getAffects() {
        return affects;
    }

    /**
     * Obtiene la duración del efecto de este ataque de estado en turnos.
     * @return
     */
    public int getTurnosDuracion() {
        return turnosDuracion;
    }

    /**
     * Establece la duración del efecto de este ataque de estado en turnos.
     * @param turnosDuracion
     */
    public String getEffect() {
        return affects.name();
    }

    /**
     * Calcula el daño infligido por este ataque de estado.
     * En lugar de infligir daño, aplica el efecto correspondiente al Pokémon atacante. 
     */
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

    /**
     * Clona este ataque de estado creando una nueva instancia con los mismos atributos.
     * @return Una copia exacta de este ataque de estado.
     */
    @Override
    public Attack clone() {
        return new StatusAttack(getName(), getType(), getBaseDamage(), getPowerPoint(), getPrecision(), affects, getTurnosDuracion(), getAttackType());
    }
}