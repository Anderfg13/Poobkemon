package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class HumanCoach extends Coach {
    private Map<Integer, Consumer<HumanCoach>> actionMap = new HashMap<>();
    private String name;
    private Coach opponent;

    public HumanCoach(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(pokemons, items);
        this.name = name;

        initializeActionMap();
    }

    private void initializeActionMap() {
        actionMap.put(0, hc -> {
            try {
                performAttack();
            } catch (PoobkemonException e) {
                System.out.println("Error al realizar el ataque: " + e.getMessage());
            }
        });
        actionMap.put(1, hc -> {
            try {
                performItem();
            } catch (PoobkemonException e) {
                System.out.println("Error al usar el ítem: " + e.getMessage());
            }
        });
        actionMap.put(2, hc -> {
            try {
                performSwitch();
            } catch (PoobkemonException e) {
                System.out.println("Error al cambiar de Pokémon: " + e.getMessage());
            }
        });
        actionMap.put(3, hc -> performFlee());
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void handleTurnTimeout() {
        Pokemon active = getActivePokemon();
        if (active != null) {
            active.reducePP(); // Método a implementar en Pokemon
        }
    }

    // Métodos auxiliares
    private void performAttack() throws PoobkemonException {
        Pokemon attacker = getActivePokemon();
        int moveIdx = getSelectedMoveIndexFromUI();

        if (moveIdx < 0 || moveIdx >= attacker.getAtaques().size()) {
            //throw new PoobkemonException(PoobkemonException.INVALID_MOVE);
        }

        Attack atk = attacker.getAtaques().get(moveIdx);
        attacker.attack(opponent.getActivePokemon(), atk);
    }

    private void performItem() throws PoobkemonException {
        int itemIdx = getSelectedItemIndexFromUI();

        if (itemIdx < 0 || itemIdx >= items.size()) {
            //throw new PoobkemonException(PoobkemonException.INVALID_ITEM);
        }

        useItem(itemIdx);
        items.remove(itemIdx);
    }

    private void useItem(int itemIdx) throws PoobkemonException {
        Item item = items.get(itemIdx);
        Pokemon target = getActivePokemon();

        if (target == null) {
            //throw new PoobkemonException(PoobkemonException.NO_ACTIVE_POKEMON);
        }

        item.applyItemEffect(target); // Método a implementar en Item
    }

    private void performSwitch() throws PoobkemonException {
        int idx = getSelectedSwitchIndexFromUI();
        switchToPokemon(idx); // Método ya implementado en Coach
    }

    private void performFlee() {
        for (Pokemon pokemon : pokemons) {
            pokemon.setPs(0);
        }
    }

    // Métodos de UI (versión básica)
    private int getSelectedMoveIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    private int getSelectedItemIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    private int getSelectedSwitchIndexFromUI() {
        // Implementación temporal
        return 0;
    }

    public void setOpponent(Coach opponent) {
        this.opponent = opponent;
    }

    public String getName() {
        return name;
    }
}