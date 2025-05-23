package domain;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Log es una clase utilitaria para registrar errores o excepciones en un archivo de log.
 * Utiliza la API {@link java.util.logging} para almacenar trazas de excepciones y mensajes de error en un archivo persistente.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Registra excepciones en un archivo de log con nombre configurable.</li>
 *   <li>El archivo se crea o se abre en modo de adición ("append") para no sobrescribir registros anteriores.</li>
 *   <li>Utiliza el formato {@link SimpleFormatter} para almacenar la traza de la excepción.</li>
 *   <li>Permite centralizar el manejo de errores y facilitar la depuración del sistema.</li>
 *   <li>En caso de error al escribir en el log, imprime la traza por consola y detiene el programa.</li>
 * </ul>
 *
 * <p>Ejemplo de uso:
 * <pre>
 *     try {
 *         // Código que puede lanzar una excepción
 *     } catch (Exception e) {
 *         Log.record(e);
 *     }
 * </pre>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Log {

    /**
     * Nombre base del logger y del archivo de log.
     */
    public static String nombre = "Poobkemon Garcia Romero";

    /**
     * Registra una excepción en el archivo de log especificado.
     * <p>
     * El archivo de log se crea o se abre en modo de adición ("append") y se
     * registra la traza de la excepción con un formato simple.
     * </p>
     *
     * @param e la excepción que se desea registrar.
     * @throws RuntimeException si ocurre un error al intentar escribir en el archivo de log.
     */
    public static void record(Exception e) {
        try {
            Logger logger = Logger.getLogger(nombre);
            logger.setUseParentHandlers(false);
            FileHandler file = new FileHandler(nombre + ".log", true);
            file.setFormatter(new SimpleFormatter());
            logger.addHandler(file);
            logger.log(Level.SEVERE, e.toString(), e);
            file.close();
        } catch (Exception oe) {
            // En caso de que falle el registro, imprime la traza y detiene el programa
            oe.printStackTrace();
            System.exit(0);
        }
    }
}
