package domain;

import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.logging.Level;

/**
 * Clase utilitaria para registrar errores o excepciones en un archivo de log.
 * <p>
 * El archivo de log se guarda con el nombre "Plan15.log" y contiene
 * los errores capturados con nivel SEVERE. Utiliza la clase {@link java.util.logging}.
 * </p>
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
