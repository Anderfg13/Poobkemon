package domain;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementación de una Machine controlada por Google Gemini API. prueba
 */
public class GeminiMachine extends Machine {
    private static final String API_KEY = "AIzaSyCs_ggcWvYmpzYi7HgdlinmG4RhCJG-kbQ"; 
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent";
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private final Random random = new Random();
    private int turnCount = 0;
    private boolean isApiReachable = true; // Flag para verificar la conexion

    /**
     * Constructor
     * @param name Nombre de la máquina
     * @param pokemons Lista de Pokémon de la máquina
     * @param items Lista de items de la máquina
     */
    public GeminiMachine(String name, ArrayList<Pokemon> pokemons, ArrayList<String> items) {
        super(name, pokemons, items);
        this.machineType = "Gemini";
        System.out.println("GeminiMachine inicializada: " + name);
        verifyApiConnection(); // Verificar la conexion al inicio
    }

    /**
     * Verifica la conexión con la API de Gemini.
     */
    private void verifyApiConnection() {
        try {
            String prompt = "Hello, Gemini!"; // Un prompt simple para verificar la conexión
            String response = queryGeminiAPI(prompt);
            if (response == null || response.isEmpty()) {
                isApiReachable = false;
                System.err.println("Error: No se pudo obtener una respuesta válida de la API de Gemini.");
            } else {
                isApiReachable = true;
                System.out.println("Conexión con la API de Gemini verificada.");
            }
        } catch (Exception e) {
            isApiReachable = false;
            System.err.println("Error al verificar la conexión con la API de Gemini: " + e.getMessage());
        }
    }

    /**
     * Selecciona el mejor Pokémon según análisis de Gemini
     */
    @Override
    public int selectBestPokemon() {
        if (!isApiReachable) {
            System.err.println("Advertencia: La API de Gemini no está disponible. Usando selección aleatoria.");
            List<Integer> pokemonsVivos = new ArrayList<>();
            for (int i = 0; i < pokemons.size(); i++) {
                if (i != activePokemonIndex && pokemons.get(i).getPs() > 0) {
                    pokemonsVivos.add(i);
                }
            }
            if (!pokemonsVivos.isEmpty()) {
                return pokemonsVivos.get(random.nextInt(pokemonsVivos.size()));
            }
            return activePokemonIndex;
        }

        Pokemon currentPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        List<String> availablePokemons = new ArrayList<>();
        for (int i = 0; i < pokemons.size(); i++) {
            if (i != activePokemonIndex && pokemons.get(i).getPs() > 0) {
                availablePokemons.add(pokemons.get(i).getName());
            }
        }
        
        if (availablePokemons.isEmpty()) {
            return activePokemonIndex;
        }
        
        String prompt = buildPromptForPokemonChange(currentPokemon, opponentPokemon, availablePokemons);
        
        try {
            String response = queryGeminiAPI(prompt);
            String selectedPokemon = parseGeminiResponseForPokemon(response, availablePokemons);
            
            if (selectedPokemon != null) {
                System.out.println("GeminiMachine seleccionó el Pokémon: " + selectedPokemon);
                
                // Encontrar el índice del Pokémon seleccionado
                for (int i = 0; i < pokemons.size(); i++) {
                    if (pokemons.get(i).getName().equals(selectedPokemon)) {
                        return i;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Gemini para selección de Pokémon: " + e.getMessage());
            isApiReachable = false; // Desactivar si falla la llamada
        }
        
        // Fallback: seleccionar un Pokémon aleatorio con vida
        List<Integer> pokemonsVivos = new ArrayList<>();
        for (int i = 0; i < pokemons.size(); i++) {
            if (i != activePokemonIndex && pokemons.get(i).getPs() > 0) {
                pokemonsVivos.add(i);
            }
        }
        
        if (!pokemonsVivos.isEmpty()) {
            return pokemonsVivos.get(random.nextInt(pokemonsVivos.size()));
        }
        
        return activePokemonIndex;
    }

    /**
     * Selecciona el mejor movimiento según análisis de Gemini
     */
    @Override
    public int selectMove() {
        if (!isApiReachable) {
            System.err.println("Advertencia: La API de Gemini no está disponible. Usando selección aleatoria.");
            Pokemon currentPokemon = getActivePokemon();
            List<Attack> ataques = currentPokemon.getAtaques();
            return random.nextInt(ataques.size());
        }

        Pokemon currentPokemon = getActivePokemon();
        Pokemon opponentPokemon = opponent.getActivePokemon();
        
        List<String> availableMoves = new ArrayList<>();
        List<Attack> ataques = currentPokemon.getAtaques();
        
        for (Attack ataque : ataques) {
            availableMoves.add(ataque.getName());
        }
        
        String prompt = buildPromptForMoveSelection(currentPokemon, opponentPokemon, availableMoves);
        
        try {
            String response = queryGeminiAPI(prompt);
            String selectedMove = parseGeminiResponseForMove(response, availableMoves);
            
            if (selectedMove != null) {
                System.out.println("GeminiMachine seleccionó el movimiento: " + selectedMove);
                
                // Encontrar el índice del movimiento seleccionado
                for (int i = 0; i < ataques.size(); i++) {
                    if (ataques.get(i).getName().equals(selectedMove)) {
                        return i;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Gemini para selección de movimiento: " + e.getMessage());
            isApiReachable = false; // Desactivar si falla la llamada
        }
        
        // Fallback: seleccionar movimiento aleatorio
        return random.nextInt(ataques.size());
    }

    /**
     * Decide si usar un item
     */
    @Override
    public boolean shouldUseItem() {
        if (!isApiReachable) {
            System.err.println("Advertencia: La API de Gemini no está disponible. Usando decisión aleatoria.");
            Pokemon currentPokemon = getActivePokemon();
            int currentHP = currentPokemon.getPs();
            int maxHP = currentPokemon.getTotalPs();
            return currentHP < maxHP * 0.3 && random.nextDouble() < 0.3;
        }

        // Basado en porcentaje de vida y condición del equipo
        Pokemon currentPokemon = getActivePokemon();
        int currentHP = currentPokemon.getPs();
        int maxHP = currentPokemon.getTotalPs();
        
        // Si tiene poca vida y hay items, podría usar uno
        if (currentHP < maxHP * 0.3 && !items.isEmpty()) {
            try {
                String prompt = "Should I use an item when my Pokemon " + 
                    currentPokemon.getName() + " has " + currentHP + "/" + maxHP + 
                    " HP? Respond with only YES or NO based on strategic value.";
                
                String response = queryGeminiAPI(prompt);
                if (response.contains("YES")) {
                    return true;
                }
            } catch (Exception e) {
                System.err.println("Error al consultar Gemini para uso de item: " + e.getMessage());
                isApiReachable = false; // Desactivar si falla la llamada
            }
            
            // 30% de probabilidad por defecto si tiene poca vida
            return random.nextDouble() < 0.3;
        }
        
        return false;
    }

    /**
     * Selecciona el mejor item
     */
    @Override
    public int selectItem() {
        if (!isApiReachable) {
            System.err.println("Advertencia: La API de Gemini no está disponible. Usando selección aleatoria.");
            if (items.isEmpty()) {
                return -1;
            }
            return random.nextInt(items.size());
        }

        if (items.isEmpty()) {
            return -1;
        }
        
        List<String> itemNames = new ArrayList<>();
        for (Item item : items) {
            itemNames.add(item.getName());
        }
        
        try {
            String prompt = "I have these items: " + String.join(", ", itemNames) + 
                ". My current Pokemon has " + getActivePokemon().getPs() + "/" + 
                getActivePokemon().getTotalPs() + " HP. Which item should I use? " +
                "Respond with the exact name of one item.";
            
            String response = queryGeminiAPI(prompt);
            
            // Buscar el item mencionado en la respuesta
            for (int i = 0; i < itemNames.size(); i++) {
                if (response.contains(itemNames.get(i))) {
                    return i;
                }
            }
        } catch (Exception e) {
            System.err.println("Error al consultar Gemini para selección de item: " + e.getMessage());
            isApiReachable = false; // Desactivar si falla la llamada
        }
        
        // Selección aleatoria por defecto
        return random.nextInt(items.size());
    }

    /**
     * Construye el prompt para enviar a Gemini para selección de movimiento
     */
    private String buildPromptForMoveSelection(Pokemon currentPokemon, Pokemon opponentPokemon, List<String> availableMoves) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI controlling a Pokémon in a battle. ");
        prompt.append("Turn number: ").append(++turnCount).append("\n\n");
        
        // Información del Pokémon actual
        prompt.append("YOUR CURRENT POKÉMON:\n");
        prompt.append("Name: ").append(currentPokemon.getName()).append("\n");
        prompt.append("Type: ").append(currentPokemon.getType()).append("\n");
        prompt.append("HP: ").append(currentPokemon.getPs()).append("/").append(currentPokemon.getTotalPs()).append("\n");
        
        // Información del Pokémon rival
        prompt.append("\nOPPONENT'S POKÉMON:\n");
        prompt.append("Name: ").append(opponentPokemon.getName()).append("\n");
        prompt.append("Type: ").append(opponentPokemon.getType()).append("\n");
        prompt.append("HP: ").append(opponentPokemon.getPs()).append("/").append(opponentPokemon.getTotalPs()).append("\n\n");
        
        // Opciones disponibles
        prompt.append("YOUR AVAILABLE MOVES:\n");
        for (String move : availableMoves) {
            prompt.append("- ").append(move).append("\n");
        }
        prompt.append("\n");
        
        prompt.append("Which move should I use? Consider type advantages, remaining HP, and potential damage.");
        prompt.append("Respond with ONLY the name of the move you recommend, exactly as written above.");
        
        return prompt.toString();
    }

    /**
     * Construye el prompt para enviar a Gemini para cambio de Pokémon
     */
    private String buildPromptForPokemonChange(Pokemon currentPokemon, Pokemon opponentPokemon, List<String> availablePokemons) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("You are an AI controlling Pokémon in a battle. ");
        prompt.append("Turn number: ").append(turnCount).append("\n\n");
        
        // Información del Pokémon actual
        prompt.append("YOUR CURRENT POKÉMON:\n");
        prompt.append("Name: ").append(currentPokemon.getName()).append("\n");
        prompt.append("Type: ").append(currentPokemon.getType()).append("\n");
        prompt.append("HP: ").append(currentPokemon.getPs()).append("/").append(currentPokemon.getTotalPs()).append("\n");
        
        // Información del Pokémon rival
        prompt.append("\nOPPONENT'S POKÉMON:\n");
        prompt.append("Name: ").append(opponentPokemon.getName()).append("\n");
        prompt.append("Type: ").append(opponentPokemon.getType()).append("\n");
        prompt.append("HP: ").append(opponentPokemon.getPs()).append("/").append(opponentPokemon.getTotalPs()).append("\n\n");
        
        // Opciones disponibles
        prompt.append("YOUR AVAILABLE POKÉMON TO SWITCH TO:\n");
        for (String pokemon : availablePokemons) {
            prompt.append("- ").append(pokemon).append("\n");
        }
        prompt.append("\n");
        
        prompt.append("Which Pokemon should I switch to? Consider type advantages and remaining HP.");
        prompt.append("Respond with ONLY the name of the Pokemon you recommend, exactly as written above.");
        
        return prompt.toString();
    }

    /**
     * Envía una consulta a la API de Gemini
     */
    private String queryGeminiAPI(String prompt) throws IOException, InterruptedException {
        // Implementación simplificada usando String para el cuerpo de la solicitud
        String requestBody = String.format(
            "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}],\"generationConfig\":{\"temperature\":0.2,\"maxOutputTokens\":100}}",
            prompt.replace("\"", "\\\"").replace("\n", "\\n")
        );
        
        // Crear la petición HTTP
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL + "?key=" + API_KEY))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        
        // Enviar la petición y recibir la respuesta
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        if (response.statusCode() != 200) {
            throw new IOException("Error en la API de Gemini: " + response.statusCode() + " - " + response.body());
        }
        
        return response.body();
    }

    /**
     * Analiza la respuesta de Gemini y extrae el movimiento seleccionado
     * (Versión simplificada sin usar JSONObject)
     */
    private String parseGeminiResponseForMove(String responseJson, List<String> availableMoves) {
        // Método simple para extraer el texto de la respuesta
        String text = extractTextFromResponse(responseJson);
        System.out.println("Respuesta de Gemini (movimiento): " + text);
        
        // Buscar el movimiento exacto en la respuesta
        for (String move : availableMoves) {
            if (text.contains(move)) {
                return move;
            }
        }
        
        return null;
    }

    /**
     * Analiza la respuesta de Gemini y extrae el Pokémon seleccionado
     * (Versión simplificada sin usar JSONObject)
     */
    private String parseGeminiResponseForPokemon(String responseJson, List<String> availablePokemons) {
        // Método simple para extraer el texto de la respuesta
        String text = extractTextFromResponse(responseJson);
        System.out.println("Respuesta de Gemini (Pokemon): " + text);
        
        // Buscar el Pokemon exacto en la respuesta
        for (String pokemon : availablePokemons) {
            if (text.contains(pokemon)) {
                return pokemon;
            }
        }
        
        return null;
    }
    
    /**
     * Extrae el texto de la respuesta JSON (método simplificado)
     */
    private String extractTextFromResponse(String responseJson) {
        // Buscar el campo "text" en la respuesta JSON
        int textIndex = responseJson.indexOf("\"text\":");
        if (textIndex == -1) return "";
        
        int startIndex = responseJson.indexOf("\"", textIndex + 7) + 1;
        int endIndex = responseJson.indexOf("\"", startIndex);
        
        if (startIndex >= 0 && endIndex >= 0) {
            return responseJson.substring(startIndex, endIndex);
        }
        
        return "";
    }
}