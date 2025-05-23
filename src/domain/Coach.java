package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

/**
 * Coach es una clase abstracta que representa a un entrenador en el juego Poobkemon.
 * Gestiona el equipo de Pokémon, los ítems, el Pokémon activo y el puntaje del entrenador.
 * Proporciona métodos para cambiar de Pokémon, usar ítems, revivir Pokémon y manejar el flujo de batalla.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Permite agregar y gestionar Pokémon e ítems en el equipo del entrenador.</li>
 *   <li>Controla el Pokémon activo y permite cambiarlo según las reglas del juego.</li>
 *   <li>Permite usar y eliminar ítems, así como revivir Pokémon debilitados.</li>
 *   <li>Incluye métodos para verificar el estado de los Pokémon y el inventario.</li>
 *   <li>Gestiona el puntaje del entrenador y si ha huido de la batalla.</li>
 *   <li>Define métodos abstractos para obtener el nombre del entrenador y manejar el tiempo agotado del turno.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public abstract class Coach implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Número máximo de Pokémon permitidos en el equipo. */
    public static final int MAX_CANT_POKEMON = 6;
    //private String name; // Nombre del entrenador
    /** Lista de Pokémon del entrenador. */
    protected List<Pokemon> pokemons;
    /** Índice del Pokémon actualmente en batalla. */
    protected int activePokemonIndex;
    /** Lista de objetos del entrenador. */
    protected List<Item> items;
    /** Puntaje del entrenador. */
    private int score;
    /** Indica si el entrenador ha huido de la batalla. */
    private boolean fled = false;
    private Color colorCoach;


    /**
     * Crea un nuevo entrenador con una lista de Pokémon y una lista de nombres de ítems.
     *
     * @param pokemons Lista de Pokémon iniciales.
     * @param items    Lista de nombres de ítems iniciales.
     */
    public Coach(ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        this.pokemons = new ArrayList<>(pokemons);
        createItems(items);
        this.activePokemonIndex = 0; // Por defecto, el primer Pokémon es el activo
        this.score = 0;
    }

    /**
     * Devuelve el nombre del entrenador.
     * @return el nombre del entrenador
     */
    public abstract String getName();

    /**
     * Agrega un ítem al inventario del entrenador.
     * @param nombreItem Nombre del ítem a agregar.
     */
    public void agregarItem(String nombreItem) {
        Item item = ItemFactory.createItem(nombreItem);
        items.add(item);
    }

    /**
     * Crea la lista de ítems del entrenador a partir de nombres.
     * @param items Lista de nombres de ítems.
     */
    public void createItems(ArrayList<String> items) {
        this.items = new ArrayList<>();
        for (String nombreItem : items) {
            Item item = ItemFactory.createItem(nombreItem);
            this.items.add(item);
        }
    }

    /**
     * Agrega un Pokémon al equipo del entrenador.
     * @param nombrePokemon Nombre del Pokémon a agregar.
     */
    public void agregarPokemon(String nombrePokemon) {
        Pokemon pokemon = PokemonFactory.createPokemon(nombrePokemon);
        pokemons.add(pokemon);
    }

    /**
     * Asigna ataques a los Pokémon del entrenador.
     * @param pokemAttacks Matriz de ataques para cada Pokémon.
     */
    public void setPokemonAttacks(Attack[][] pokemAttacks) {
        for (Pokemon pokemon : pokemons) {
            pokemon.setAttacks(pokemAttacks[pokemons.indexOf(pokemon)]);
        }
    }

    /**
     * Cambia el Pokémon activo al índice especificado.
     * @param index Índice del Pokémon a activar.
     */
    public void switchPokemon(int index)  {
        this.activePokemonIndex = index;
    }

    /**
     * Devuelve la lista de Pokémon del entrenador.
     * @return Lista de Pokémon.
     */
    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    /**
     * Verifica si un Pokémon está debilitado.
     * @param pokemon Pokémon a verificar.
     * @return {@code true} si el Pokémon está debilitado, {@code false} en caso contrario.
     */
    public boolean checkPokemonStatus(Pokemon pokemon) {
        return pokemon.getPs() <= 0;
    }

    /**
     * Devuelve el Pokémon actualmente activo en batalla.
     * @return Pokémon activo.
     */
    public Pokemon getActivePokemon() {
        return pokemons.get(activePokemonIndex);
    }

    /**
     * Cambia al Pokémon activo al indicado por el jugador.
     * @param index Índice del Pokémon al que se desea cambiar.
     * @throws PoobkemonException si el índice es inválido o el Pokémon está debilitado.
     */
    public void switchToPokemon(int index) throws PoobkemonException {
        System.out.println(3);
        if (index < 0 || index >= pokemons.size()) {
            throw new PoobkemonException(PoobkemonException.INVALID_POKEMON_INDEX);
        }
        Pokemon selected = pokemons.get(index);
        if (selected.getPs() <= 0) {
            throw new PoobkemonException(PoobkemonException.FAINTED_POKEMON);
        }
        this.activePokemonIndex = index;
        System.out.println("El entrenador ha cambiado al Pokémon activo a: " + selected.getName());
    }

    /**
     * Cambia el Pokémon activo al que tenga el nombre especificado y esté vivo.
     * @param nombrePokemon Nombre del Pokémon a activar.
     */
    public void cambiarPokemonActivo(String nombrePokemon) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getName().equals(nombrePokemon) && pokemons.get(i).getPs() > 0) {
                activePokemonIndex = i;
                break;
            }
        }
    }

    /**
     * Verifica si todos los Pokémon del entrenador están debilitados.
     * @return {@code true} si todos los Pokémon están debilitados, {@code false} en caso contrario.
     */
    public boolean areAllPokemonFainted() {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getPs() > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Usa un ítem sobre el Pokémon activo.
     * @param item Ítem a usar.
     * @throws PoobkemonException Si no hay Pokémon activo, el Pokémon tiene la vida llena, o el ítem no puede usarse.
     */
    public void useItem(Item item) throws PoobkemonException {
        Pokemon activePokemon = getActivePokemon();
        if (activePokemon == null) {
            throw new PoobkemonException(PoobkemonException.NO_POKEMONS_SELECTED);
        }
        if (activePokemon.getPs() == activePokemon.getTotalPs()) {
            throw new PoobkemonException(PoobkemonException.FULL_POKEMON_HEALTH);
        }
        if(activePokemon.getPs() <= 0 && item.getName()=="Revivir") {
            throw new PoobkemonException(PoobkemonException.POKEMON_HAS_BEEN_FAINTED);
        }
        item.applyItemEffect(activePokemon);
    }

    /**
     * Usa un ítem por nombre y lo elimina del inventario.
     * @param nombreItem Nombre del ítem a usar.
     * @throws PoobkemonException Si ocurre un error al usar el ítem.
     */
    public void usarItem(String nombreItem) throws PoobkemonException {
        // ...lógica de uso...
        // Elimina el ítem del inventario:
        items.removeIf(item -> item.getName().equals(nombreItem));
    }

    /**
     * Usa un ítem por nombre sobre el Pokémon activo y lo elimina del inventario.
     * @param nombreItem Nombre del ítem a usar.
     * @throws PoobkemonException Si el ítem no está disponible.
     */
    public void useItem(String nombreItem) throws PoobkemonException {
        Item item = null;
        for (Item it : items) {
            if (it.getName().equals(nombreItem)) {
                item = it;
                break;
            }
        }
        if (item == null) throw new PoobkemonException("No tienes ese ítem.");
        // Aplica el ítem al pokémon activo de este coach
        item.applyItemEffect(getActivePokemon());
        // Elimina el ítem del inventario
        items.remove(item);
    }

    /**
     * Devuelve el puntaje actual del entrenador.
     * @return Puntaje.
     */
    public int getScore() {
        return score;
    }

    /**
     * Suma puntos al puntaje del entrenador.
     * @param points Puntos a agregar.
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Maneja el tiempo agotado del turno.
     * Debe ser implementado por las subclases para definir la penalización o acción a tomar.
     */
    public abstract void handleTurnTimeout();

    /**
     * Devuelve el Pokémon actualmente activo del entrenador.
     * @return Pokémon activo.
     */
    public Pokemon getCurrentPokemon() {
        return pokemons.get(activePokemonIndex);
    }

    /**
     * Indica si el entrenador ha huido de la batalla.
     * @return {@code true} si ha huido, {@code false} en caso contrario.
     */
    public boolean getHasFled() {
        return fled;
    }

    /**
     * Marca al entrenador como que ha huido de la batalla.
     */
    public void fleeBattle() {
        this.fled = true;
    }

    /**
     * Devuelve la lista de nombres de los ítems en el inventario.
     * @return Lista de nombres de ítems.
     */
    public List<String> getNombreItems() {
        List<String> nombres = new ArrayList<>();
        for (Item item : items) {
            nombres.add(item.getName());
        }
        return nombres;
    }

    /**
     * Revive un Pokémon debilitado por nombre, restaurando la mitad de su vida máxima.
     * @param nombrePokemon Nombre del Pokémon a revivir.
     * @throws PoobkemonException Si el Pokémon no se puede revivir.
     */
    public void revivirPokemon(String nombrePokemon) throws PoobkemonException {
        for (Pokemon p : pokemons) {
            if (p.getName().equals(nombrePokemon) && p.getPs() == 0) {
                p.setPs(p.getTotalPs() / 2); // Revive con la mitad de la vida
                return;
            }
        }
        throw new PoobkemonException("No se pudo revivir el pokémon.");
    }

    /**
     * Elimina un ítem del inventario por nombre.
     * @param nombreItem Nombre del ítem a eliminar.
     */
    public void eliminarItem(String nombreItem) {
        items.removeIf(item -> item.getName().equals(nombreItem));
    }

    /**
     * Devuelve la lista de ítems del entrenador.
     * @return Lista de ítems.
     */
    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Establece un Pokémon específico como el activo, si está en el equipo y no está debilitado.
     * @param pokemon El Pokémon que se establecerá como activo.
     */
    public void setActivePokemon(Pokemon pokemon) {
        if (pokemon == null) return;
        // Buscar el índice del pokemon en la lista
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i) == pokemon) {
                // Verificar que el Pokémon no esté debilitado
                if (pokemons.get(i).getPs() > 0) {
                    activePokemonIndex = i;
                    System.out.println("El entrenador ha establecido a " + pokemon.getName() + " como Pokémon activo.");
                }
                break;
            }
        }
    }

    /**
     * Indica si el entrenador es una máquina.
     * @return {@code true} si es máquina, {@code false} si es humano.
     */
    public boolean isMachine() {
        return false;
    }

    public Color getColorCoach() {
        return colorCoach;
    }

    public void setColor(Color colorCoach) {
        this.colorCoach = colorCoach;
    }
}
