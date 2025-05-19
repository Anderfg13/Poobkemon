package domain;

import java.util.ArrayList;
import java.util.List;

public abstract class Coach {
    public static final int MAX_CANT_POKEMON = 6;
    //private String name; // Nombre del entrenador
    protected List<Pokemon> pokemons; // Lista de Pokémon del entrenador
    protected int activePokemonIndex; // Índice del Pokémon actualmente en batalla
    protected List<Item> items; // Lista de objetos del entrenador
    private int score;
    private boolean fled = false;

    public Coach(ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        this.pokemons = new ArrayList<>(pokemons);
        createItems(items);
        this.activePokemonIndex = 0; // Por defecto, el primer Pokémon es el activo
        this.score = 0;
    }
    /**
     * Returns the name of the coach.
     * @return the coach's name
     */
    public abstract String getName();

    public void agregarItem(String nombreItem) {
        Item item = ItemFactory.createItem(nombreItem);
        items.add(item);
    }

    public void createItems(ArrayList<String> items) {
        this.items = new ArrayList<>();
        for (String nombreItem : items) {
            Item item = ItemFactory.createItem(nombreItem);
            this.items.add(item);
        }
    }


    public void agregarPokemon(String nombrePokemon) {
        Pokemon pokemon = PokemonFactory.createPokemon(nombrePokemon);
        pokemons.add(pokemon);
    }

    public void setPokemonAttacks(Attack[][] pokemAttacks) {
        for (Pokemon pokemon : pokemons) {
            pokemon.setAttacks(pokemAttacks[pokemons.indexOf(pokemon)]);
        }
    }

    public void switchPokemon(int index)  {

        this.activePokemonIndex = index;
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public boolean checkPokemonStatus(Pokemon pokemon) {
        return pokemon.getPs() <= 0;
    }
    

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

    public void cambiarPokemonActivo(String nombrePokemon) {
        for (int i = 0; i < pokemons.size(); i++) {
            if (pokemons.get(i).getName().equals(nombrePokemon) && pokemons.get(i).getPs() > 0) {
                activePokemonIndex = i;
                break;
            }
        }
    }

    public boolean areAllPokemonFainted() {
    	
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getPs() > 0) {
                return false;
            }
        }
        return true;
    }

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

    public void usarItem(String nombreItem) throws PoobkemonException {
        // ...lógica de uso...
        // Elimina el ítem del inventario:
        items.removeIf(item -> item.getName().equals(nombreItem));
    }

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

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Maneja el tiempo agotado del turno.
     * Puedes penalizar al entrenador (por ejemplo, perder PP o cambiar de pokémon).
     */
    public abstract void handleTurnTimeout();

    /**
     * Devuelve el pokemon actual del entrenador
     */
    public Pokemon getCurrentPokemon() {
        return pokemons.get(activePokemonIndex);
    }

    public boolean getHasFled() {
        return fled;
    }

    public void fleeBattle() {
        this.fled = true;
    }

    public List<String> getNombreItems() {
        List<String> nombres = new ArrayList<>();
        for (Item item : items) {
            nombres.add(item.getName());
        }
        return nombres;
    }

    public void revivirPokemon(String nombrePokemon) throws PoobkemonException {
        for (Pokemon p : pokemons) {
            if (p.getName().equals(nombrePokemon) && p.getPs() == 0) {
                p.setPs(p.getTotalPs() / 2); // Revive con la mitad de la vida
                return;
            }
        }
        throw new PoobkemonException("No se pudo revivir el pokémon.");
    }

    public void eliminarItem(String nombreItem) {
        items.removeIf(item -> item.getName().equals(nombreItem));
    }

    public List<Item> getItems() {
        return items;
    }
    
    /**
     * Establece un Pokémon específico como el activo.
     * @param pokemon El Pokémon que se establecerá como activo
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

    public boolean isMachine() {
        return false;
    }
}
