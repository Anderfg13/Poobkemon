package domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Pokemon representa una criatura jugable en el mundo de Poobkemon.
 * Implementa PokemonBase para ser compatible con el patrón Decorator.
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
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Pokemon implements PokemonBase {
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

    // Implementación de métodos getter de PokemonBase
    @Override
    public String getName() { return name; }
    
    @Override
    public String getType() { return type; }
    
    @Override
    public int getId() { return id; }
    
    @Override
    public int getTotalPs() { return total_ps; }
    
    @Override
    public int getPs() { return this.ps; }
    
    @Override
    public int getSpeed() { return speed; }
    
    @Override
    public int getEvasion() { return evasion; }
    
    @Override
    public int getSpecialAttack() { return specialAttack; }
    
    @Override
    public int getPhysicalAttack() { return physicalAttack; }
    
    @Override
    public int getSpecialDefense() { return specialDefense; }
    
    @Override
    public int getPhysicalDefense() { return physicalDefense; }
    
    @Override
    public int getStatus() { return status; }
    
    @Override
    public int getTurnStatus() { return turnStatus; }

    // Implementación de métodos setter de PokemonBase
    @Override
    public void setPs(int ps) { 
        if(ps > total_ps) {
            this.ps = total_ps;
            return;
        }
        this.ps = Math.max(ps, 0); 
    }
    
    @Override
    public void setSpeed(int speed) { this.speed = speed; }
    
    @Override
    public void setEvasion(int evasion) { this.evasion = evasion; }
    
    @Override
    public void setSpecialAttack(int specialAttack) { this.specialAttack = specialAttack; }
    
    @Override
    public void setSpecialDefense(int specialDefense) { this.specialDefense = specialDefense; }
    
    @Override
    public void setPhysicalAttack(int physicalAttack) { this.physicalAttack = physicalAttack; }
    
    @Override
    public void setPhysicalDefense(int physicalDefense) { this.physicalDefense = physicalDefense; }
    
    @Override
    public void setStatus(int status) { this.status = status; }
    
    @Override
    public void setTurnStatus(int turnStatus) { this.turnStatus = turnStatus; }

    // Implementación de métodos de ataque y comportamiento de PokemonBase
    @Override
    public void addAttack(Attack attack) {
        if (ataques.size() < 4) { // Máximo 4 ataques
            ataques.add(attack);
        } else {
            throw new IllegalStateException("El Pokémon ya tiene 4 ataques.");
        }
    }

    @Override
    public List<Attack> getAtaques() {
        return new ArrayList<>(ataques); // Retorna copia para evitar modificaciones externas
    }

    @Override
    public List<String> getNombreAtaques() {
        List<String> nombres = new ArrayList<>();
        for (Attack ataque : ataques) {
            nombres.add(ataque.getName());
        }
        return nombres;
    }

    @Override
    public void applyEffectDamage() {
        if (status != 0) {
            switch (status) {
                case 1: // Paralizado
                    // Lógica de parálisis - no causa daño directo
                    break;
                case 2: // Dormido
                    // Lógica de sueño - no causa daño directo
                    break;
                case 3: // Quemado
                    setPs(ps - (total_ps / 16)); // Daño por quemadura
                    break;
                case 4: // Congelado
                    // Lógica de congelación - no causa daño directo
                    break;
                case 5: // Envenenado
                    setPs(ps - (total_ps / 8)); // Daño por veneno
                    break;
            }
        }
    }

    @Override
    public int attack(PokemonBase defensor, Attack attack) {
        // Verificar que el ataque esté disponible
        if (!ataques.contains(attack)) {
            throw new IllegalArgumentException("El Pokémon no conoce este ataque.");
        }
        
        // Calcular daño usando el método del ataque
        int daño = attack.calcDaño(this, (Pokemon)defensor);
        
        // Aplicar daño al defensor si es mayor que 0
        if (daño > 0) {
            defensor.setPs(defensor.getPs() - daño);
        }
        
        return daño;
    }
    
    @Override
    public boolean hasStatus(int statusCode) {
        return this.status == statusCode;
    }

    // Métodos adicionales específicos de Pokemon (mantener compatibilidad)
    
    /**
     * Método sobrecargado para mantener compatibilidad con código existente
     * que usa Pokemon directamente en lugar de PokemonBase.
     */
    public int attack(Pokemon defensor, Attack attack) {
        return attack(((PokemonBase)defensor), attack);
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
            throw new UnsupportedOperationException("Debes usar attack(PokemonBase, Attack) para atacar al oponente.");
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
     * Establece la lista de ataques del Pokémon.
     * @param ataques Lista de ataques a asignar
     */
    public void setAtaques(ArrayList<Attack> ataques) {
        this.ataques = new ArrayList<>(ataques); // Crear una copia para evitar modificaciones externas
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
}
