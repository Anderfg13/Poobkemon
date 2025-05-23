package presentation;

import java.io.File;
import javax.sound.sampled.*;

import domain.Log;

/**
 * Sonidos es una clase utilitaria para gestionar la reproducción de efectos de sonido en la interfaz de Poobkemon.
 * Permite reproducir sonidos específicos, como el clic de botones, para mejorar la experiencia de usuario.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Reproduce sonidos almacenados en archivos locales al invocar métodos estáticos.</li>
 *   <li>Incluye manejo de errores para evitar interrupciones si el archivo de sonido no está disponible.</li>
 *   <li>Facilita la integración de efectos sonoros en los componentes de la interfaz gráfica.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Sonidos {

    /**
     * Reproduce el sonido de clic de botón.
     * Carga el archivo "mult/button_click.wav" y lo reproduce una vez.
     * Si ocurre un error al reproducir el sonido, muestra un mensaje en consola.
     */
    public static void reproducirClick() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("mult/button_click.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            Log.record(e);
            System.out.println("No se pudo reproducir el sonido: " + e.getMessage());
        }
    }
}