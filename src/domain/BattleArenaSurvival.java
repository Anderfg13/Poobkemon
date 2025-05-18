package domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BattleArenaSurvival extends BattleArena {
    
    public BattleArenaSurvival() {
        super();
    }

    @Override
    public void attack(String moveName, String itself) throws PoobkemonException {
        Coach currentCoach = getCurrentCoach();
        Coach opponentCoach = getOpponentCoach();

        Pokemon attacker = currentCoach.getActivePokemon();
        Pokemon defender = opponentCoach.getActivePokemon();

        // Encuentra el ataque por nombre
        Attack attack = attacker.getAtaques().stream()
            .filter(a -> a.getName().equals(moveName))
            .findFirst()
            .orElse(null); // Si no encuentra el ataque, devuelve null

        if (attack.getPowerPoint() > 0){
            if ("itself".equals(itself)) {
            // Realiza el ataque sobre sí mismo
            
            attacker.attack(attacker, attack);
            } else {
            // Realiza el ataque sobre el oponente
            attacker.attack(defender, attack);
            }
        } else {
            System.out.println("No puedes usar este ataque, no tienes PP.");
        }
    }

    
    public void setupSurvivalBattle(String coachName1, String coachName2) throws PoobkemonException {
        // Obtener todos los Pokémon disponibles
        List<String> allPokemon = Poobkemon.getAvailablePokemon();
        
        // Verificar que hay suficientes Pokémon
        if (allPokemon.size() < 12) {
            throw new PoobkemonException("No hay suficientes Pokémon disponibles para el modo Survival.");
        }
        
        // Mezclar la lista para obtener Pokémon aleatorios
        Collections.shuffle(allPokemon);
        
        // Seleccionar 6 Pokémon para cada jugador
        ArrayList<String> pokemons1 = new ArrayList<>(allPokemon.subList(0, 6));
        ArrayList<String> pokemons2 = new ArrayList<>(allPokemon.subList(6, 12));
        
        // Asignar 4 movimientos aleatorios a cada Pokémon
        String[][] pokemAttacks1 = assignRandomMoves(pokemons1);
        String[][] pokemAttacks2 = assignRandomMoves(pokemons2);
        
        // Configurar los entrenadores con los Pokémon y movimientos asignados
        setupCoaches(coachName1, coachName2, pokemons1, pokemons2, 
                    new ArrayList<>(), new ArrayList<>(), pokemAttacks1, pokemAttacks2);
    }

    // Método auxiliar para asignar 4 movimientos aleatorios a cada Pokémon
    private String[][] assignRandomMoves(List<String> pokemons) throws PoobkemonException {
        // Combinar todos los tipos de ataques disponibles
        List<String> allMoves = new ArrayList<>();
        allMoves.addAll(Poobkemon.getPhysicalAttacks());
        allMoves.addAll(Poobkemon.getSpecialAttacks());
        allMoves.addAll(Poobkemon.getStatusAttacks());

        // Verificar que hay suficientes movimientos disponibles
        if (allMoves.size() < 4) {
            throw new PoobkemonException("No hay suficientes movimientos disponibles para asignar a los Pokémon.");
        }

        String[][] pokemAttacks = new String[pokemons.size()][4];

        for (int i = 0; i < pokemons.size(); i++) {
            Collections.shuffle(allMoves); // Mezclar los movimientos
            for (int j = 0; j < 4; j++) {
                pokemAttacks[i][j] = allMoves.get(j); // Asignar los primeros 4 movimientos
            }
            // Mostrar los movimientos asignados en consola
            System.out.println("Pokémon: " + pokemons.get(i) + " - Movimientos: " + 
                String.join(", ", pokemAttacks[i]));
        }
        return pokemAttacks;
    }
}
