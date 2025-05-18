package domain;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
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


    public void addAttack(Attack attack) {
        if (ataques.size() < 4) { // Máximo 4 ataques
            ataques.add(attack);
        } else {
            throw new IllegalStateException("El Pokémon ya tiene 4 ataques.");
        }
    }

    public void setPs(int ps) { 
    	if(ps > total_ps) {
    		this.ps = total_ps;
    		return;
    	}
    	this.ps = Math.max(ps, 0); }

    public List<Attack> getAtaques() {
        return ataques;
    }

    public List<String> getNombreAtaques() {
        List<String> nombres = new ArrayList<>();
        for (Attack ataque : ataques) {
            nombres.add(ataque.getName());
        }
        return nombres;
    }

    //Hacerlo sin los swiches
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

    public void reducePP() {
        for (Attack ataque : ataques) {
            ataque.setPowerPoint(ataque.getPowerPoint()-1); // Reduce el PP del ataque
        }
    }

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

    public int getPPDeAtaque(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque.getPowerPoint();
            }
        }
        return 0; // O lanza excepción si no lo encuentra
    }

    public int getPPMaxDeAtaque(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque.getPowerPoint();
            }
        }
        return 0;
    }

    public Attack getAtaquePorNombre(String nombreAtaque) {
        for (Attack ataque : ataques) {
            if (ataque.getName().equals(nombreAtaque)) {
                return ataque;
            }
        }
        return null; // O lanza una excepción si prefieres
    }
}
