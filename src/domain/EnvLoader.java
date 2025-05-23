package domain;

import java.io.*;
import java.util.*;

/**
 * EnvLoader es una clase utilitaria para cargar variables de entorno desde un archivo .env.
 * Permite leer pares clave-valor definidos en un archivo de texto plano y almacenarlos en un mapa,
 * facilitando la gestión de configuraciones sensibles como claves API sin exponerlas en el código fuente.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Lee archivos .env con formato clave=valor, ignorando líneas comentadas o vacías.</li>
 *   <li>Devuelve un mapa con todas las variables de entorno encontradas.</li>
 *   <li>Maneja errores de lectura mostrando mensajes informativos en consola.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class EnvLoader {
    /**
     * Carga las variables de entorno desde un archivo .env especificado.
     *
     * @param path Ruta al archivo .env.
     * @return Un mapa con las variables de entorno cargadas (clave, valor).
     */
    public static Map<String, String> loadEnv(String path) {
        Map<String, String> env = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().startsWith("#") && line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    env.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo .env: " + e.getMessage());
        }
        return env;
    }
}