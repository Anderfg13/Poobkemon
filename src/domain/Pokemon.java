package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Pokemon representa una criatura jugable en el mundo de Poobkemon.
 * Modela las estadísticas, ataques, estado y comportamiento de combate de un Pokémon.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Almacena atributos como nombre, tipo, puntos de salud, velocidad, ataque, defensa, evasión y estado.</li>
 *   <li>Permite gestionar hasta 4 ataques distintos por Pokémon.</li>
 *   <li>Incluye métodos para atacar a otros Pokémon, atacarse a sí mismo (por confusión u otros efectos) y aplicar daño por estados alterados.</li>
 *   <li>Permite modificar y consultar estadísticas y ataques del Pokémon.</li>
 *   <li>Gestiona el estado alterado y los turnos restantes de dicho estado.</li>
 *   <li>Incluye utilidades para reducir PP de ataques y obtener información sobre los mismos.</li>
 * </ul>
 *

 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Pokemon implements PokemonBase, Serializable {
    private static final long serialVersionUID = 1L;
    protected String name;
    protected int id;
    protected int total_ps;
    protected int ps;
    protected int speed;
    protected int specialAttack;
    protected int physicalAttack;
    protected int specialDefense;
    protected int physicalDefense;
    protected String type;
    protected int evasion;
    protected int status; // 0: normal, 1: paralizado, 2: dormido, 3: quemado, 4: congelado, 5: envenenado
    protected int turnStatus; // Turnos restantes de estado (si aplica)
    protected ArrayList<Attack> ataques = new ArrayList<>();

    public Pokemon(String name, int id, int ps, int speed, int specialAttack,
                   int physicalAttack, int specialDefense, int physicalDefense,
                   String type, int evasion) {
        this.name = name;
        this.id = id;
        this.total_ps = ps;
        this.ps = ps;
        this.speed = speed;
        this.specialAttack = specialAttack;
        this.physicalAttack = physicalAttack;
        this.specialDefense = specialDefense;
        this.physicalDefense = physicalDefense;
        this.type = type;
        this.evasion = evasion;
        this.status = 0; // Normal
        this.turnStatus = 0; // Sin turnos de estado
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getId() { return id; }
    public int getTotalPs() { return total_ps; }
    public int getPs() { return this.ps; }
    public int getSpeed() { return speed; }
    public int getEvasion() { return evasion; }
    public int getSpecialAttack() { return specialAttack; }
    public int getPhysicalAttack() { return physicalAttack; }
    public int getSpecialDefense() { return specialDefense; }
    public int getPhysicalDefense() { return physicalDefense; }
    public int getStatus() { return status; }
    public int getTurnStatus() { return turnStatus; }


    public void setSpeed(int speed) { this.speed = speed; }
    public void setEvasion(int evasion) { this.evasion = evasion; }
    public void setSpecialAttack(int specialAttack) { this.specialAttack = specialAttack; }
    public void setSpecialDefense(int specialDefense) { this.specialDefense = specialDefense; }
    public void setPhysicalAttack(int physicalAttack) { this.physicalAttack = physicalAttack; }
    public void setPhysicalDefense(int physicalDefense) { this.physicalDefense = physicalDefense; }
    public void setStatus(int status) { this.status = status; }
    public void setTurnStatus(int turnStatus) { this.turnStatus = turnStatus; }

    /**
     * Añade un ataque al Pokémon.
     * @param attack El ataque a añadir.
     * @throws IllegalStateException si el Pokémon ya tiene 4 ataques.
     */
    public void addAttack(Attack attack) {
        if (ataques.size() < 4) { // Máximo 4 ataques
            ataques.add(attack);
        } else {
            throw new IllegalStateException("El Pokémon ya tiene 4 ataques.");
        }
    }

    /**
     * Establece los puntos de salud del Pokémon.
     * Si el valor es mayor que el total de PS, se ajusta al máximo.
     * Si es negativo, se ajusta a 0.
     * @param ps Puntos de salud a establecer.
     */
    public void setPs(int ps) { 
    	if(ps > total_ps) {
    		this.ps = total_ps;
    		return;
    	}
    this.ps = Math.max(ps, 0); }

    /**
     * Obtiene la lista de ataques del Pokémon.
     * @return Lista de ataques.
     */
    public List<Attack> getAtaques() {
        return ataques;
    }

    /**
     * Obtiene el número total de ataques del Pokémon.
     * @return Número de ataques.
     */
    public List<String> getNombreAtaques() {
        List<String> nombres = new ArrayList<>();
        for (Attack ataque : ataques) {
            nombres.add(ataque.getName());
        }
        return nombres;
    }

    /**
     * Aplica el daño por estado al Pokémon.
     * Dependiendo del estado, se aplica un daño específico.
     * Por ejemplo, quemado y envenenado causan daño periódico.
     */
    public void applyEffectDamage() {
        if (status != 0) {
            // Lógica para aplicar daño por estado
            switch (status) {
                case 1: // Paralizado
                    // Lógica de parálisis
                    break;
                case 2: // Dormido
                    // Lógica de sueño
                    break;
                case 3: // Quemado
                    setPs(ps - (total_ps / 16)); // Daño por quemadura
                    break;
                case 4: // Congelado
                    // Lógica de congelación
                    break;
                case 5: // Envenenado
                    setPs(ps - (total_ps / 8)); // Daño por veneno
                    break;
            }
        }
    }   

    /**
     * Reduce el PP de todos los ataques del Pokémon en 1.
     * Si un ataque llega a 0 PP, se puede eliminar o no usar.
     */
    public void reducePP() {
        for (Attack ataque : ataques) {
            ataque.setPowerPoint(ataque.getPowerPoint()-1); // Reduce el PP del ataque
        }
    }

    /**
     * Establece los ataques del Pokémon a partir de un array de Attack.
     * @param ataques Array de ataques a asignar.
     */
    public void setAttacks(Attack[] ataques) {
        this.ataques = new ArrayList<>();
        for (Attack ataque : ataques) {
            this.ataques.add(ataque);
        }
    }

    /**
     * Ataca a otro Pokémon con un ataque específico.
     * @return el daño causado
     */
    public int attack(Pokemon defensor, Attack attack) {
        int daño = attack.calcDaño(this, defensor);
        if (daño > 0) {
            defensor.setPs(defensor.getPs() - daño);
        }
        return daño;
    }

    /**
    * Establece la lista de ataques del Pokémon.
    * @param ataques Lista de ataques a asignar
    */
    public void setAtaques(ArrayList<Attack> ataques) {
        this.ataques = new ArrayList<>(ataques); // Crear una copia para evitar modificaciones externas
    }

    /**
     * Realiza un ataque a sí mismo o al oponente.
     * @param toItself si true, se ataca a sí mismo.
     * @param attack el ataque a usar.
     * @return el daño causado
     */
    public int attack(boolean toItself, Attack attack) {
        if (!ataques.contains(attack)) {
            throw new IllegalArgumentException("El Pokémon no conoce este ataque.");
        }

        if (toItself) {
            return attackItself(attack);
        } else {
            throw new UnsupportedOperationException("Debes usar attack(Pokemon, Attack) para atacar al oponente.");
        }
    }

    /**
     * Aplica un ataque a sí mismo (por ejemplo, por confusión).
     * @return el daño causado
     */
    private int attackItself(Attack attack) {
        int daño = attack.calcDaño(this, this); // Atacándose a sí mismo
        if (daño > 0) {
            setPs(ps - daño);
        }
        return daño;
    }

    /**
     * Obtiene el PP actual de un ataque específico por su nombre.
     * @param nombreAtaque Nombre del ataque.
     * @return PP actual del ataque, o 0 si no se encuentra.
     */
    public int getPPDeAtaque(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque.getPowerPoint();
            }
        }
        return 0; // O lanza excepción si no lo encuentra
    }

    /**
     * Obtiene el PP máximo de un ataque específico por su nombre.
     * @param nombreAtaque Nombre del ataque.
     * @return PP máximo del ataque, o 0 si no se encuentra.
     */
    public int getPPMaxDeAtaque(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque.getPowerPoint();
            }
        }
        return 0;
    }

    /**
     * Obtiene un ataque específico por su nombre.
     * @param nombreAtaque Nombre del ataque.
     * @return El ataque correspondiente, o null si no se encuentra.
     */
    public Attack getAtaquePorNombre(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque;
            }
        }
        return null; // O lanza una excepción si prefieres
    }

    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Implementar método para trabajar con PokemonBase
        if (defensor instanceof Pokemon) {
            return attack((Pokemon)defensor, attack);
        }
        // Implementación para decoradores
        return 0;
    }
}
