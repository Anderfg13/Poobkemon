package domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ItemFactory es una clase de utilidad que gestiona la creación y el registro de ítems utilizables en el juego Poobkemon.
 * Permite instanciar objetos {@link Item} a partir de su nombre, centralizando la definición de sus efectos, descripciones y atributos objetivo.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Registra todos los ítems disponibles en el juego, asociando su nombre con los parámetros de construcción.</li>
 *   <li>Permite crear instancias de {@link Item} de forma sencilla a partir de su nombre.</li>
 *   <li>Incluye un método para obtener la lista de nombres de todos los ítems registrados.</li>
 *   <li>Utiliza una clase auxiliar interna para almacenar los datos de cada ítem (descripción, valor de efecto y atributo objetivo).</li>
 *   <li>Facilita la extensión y mantenimiento del catálogo de ítems del juego.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class ItemFactory {
    // Mapa que asocia nombres de ítems con sus parámetros de construcción
    private static final Map<String, ItemData> ITEM_REGISTRY = new HashMap<>();

    // Clase auxiliar para almacenar datos del ítem
    private static record ItemData(
        String description,
        int effectValue,
        Item.AttributeType applyTo
    ) {}

    // Registramos todos los ítems disponibles
    static {
        // Ejemplo: Pociones, Revive, etc.
        ITEM_REGISTRY.put("Poción", new ItemData(
            "Restaura 20 PS de un Pokémon.", 
            20, 
            Item.AttributeType.HP
        ));

        ITEM_REGISTRY.put("Superpoción", new ItemData(
            "Restaura 50 PS de un Pokémon.", 
            50, 
            Item.AttributeType.HP
        ));

        ITEM_REGISTRY.put("Hyperpoción", new ItemData(
            "Restaura 200 PS de un Pokémon.", 
            200, 
            Item.AttributeType.HP
        ));

        ITEM_REGISTRY.put("Revive", new ItemData(
            "Revive a un Pokémon debilitado y restaura la mitad de sus PS.", 
            180,  // PS máximos / 2 (ejemplo para Blastoise: 362 / 2 ≈ 180)
            Item.AttributeType.REVIVE
        ));

        //ITEM_REGISTRY.put("Ataque X", new ItemData(
        //    "Aumenta el ataque físico de un Pokémon.", 
        //    10, 
        //    Item.AttributeType.PHYSICAL_ATTACK
        //));

    
    }

    /**
     * Crea una instancia de {@link Item} a partir de su nombre.
     * @param name Nombre del ítem a crear.
     * @return Instancia de {@link Item} con los parámetros correspondientes.
     * @throws IllegalArgumentException Si el nombre del ítem no está registrado.
     */
    public static Item createItem(String name) {
        ItemData data = ITEM_REGISTRY.get(name);
        if (data == null) {
            throw new IllegalArgumentException("Ítem no reconocido: " + name);
        }
        return new Item(name, data.description(), data.effectValue(), data.applyTo());
    }

    /**
     * Obtiene una lista con los nombres de todos los ítems registrados.
     * @return Lista de nombres de ítems.
     */
    public static List<String> getItemNames() {
        return new ArrayList<>(ITEM_REGISTRY.keySet());
    }
}
