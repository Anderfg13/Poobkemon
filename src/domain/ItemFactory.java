package domain;


import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

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

        //ITEM_REGISTRY.put("Revive", new ItemData(
            //"Revive a un Pokémon debilitado y restaura la mitad de sus PS.", 
            //180,  // PS máximos / 2 (ejemplo para Blastoise: 362 / 2 ≈ 180)
            //Item.AttributeType.REVIVE
        //));

        //ITEM_REGISTRY.put("Ataque X", new ItemData(
        //    "Aumenta el ataque físico de un Pokémon.", 
        //    10, 
        //    Item.AttributeType.PHYSICAL_ATTACK
        //));

        // Añadir más ítems aquí...
    }

    // Método para crear un ítem basado en su nombre
    public static Item createItem(String name) {
        ItemData data = ITEM_REGISTRY.get(name);
        if (data == null) {
            throw new IllegalArgumentException("Ítem no reconocido: " + name);
        }
        return new Item(name, data.description(), data.effectValue(), data.applyTo());
    }

    // Método para obtener los nombres de todos los ítems registrados
    public static List<String> getItemNames() {
        return new ArrayList<>(ITEM_REGISTRY.keySet());
    }
}
