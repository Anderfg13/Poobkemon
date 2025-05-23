package presentation;

import java.io.File;
import javax.sound.sampled.*;

import domain.Log;

/**
 * Musica es una clase utilitaria para gestionar la reproducción de música de fondo en la interfaz de Poobkemon.
 * Permite cargar, reproducir, pausar y ajustar el volumen de archivos de audio en formato compatible con {@link Clip}.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Carga archivos de audio desde una ruta especificada y los prepara para su reproducción en bucle.</li>
 *   <li>Permite reproducir y pausar la música, manteniendo la posición actual para reanudar.</li>
 *   <li>Incluye control de volumen por defecto al cargar la pista.</li>
 *   <li>Permite consultar si la música está actualmente encendida o pausada.</li>
 *   <li>Maneja errores de carga y reproducción de forma segura, mostrando mensajes en consola.</li>
 * </ul>
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class Musica {
    private Clip clip;
    private int framePosition = 0;
    private boolean encendida = false;

    public Musica(String ruta) {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(ruta));
            clip = AudioSystem.getClip();
            clip.open(audioIn);
            setVolumenPorDefecto(0.8f); // Volumen por defecto (70%)
        } catch (Exception e) {
            Log.record(e);
            System.out.println("No se pudo cargar la música: " + e.getMessage());
        }
    }

    private void setVolumenPorDefecto(float volumen) {
        if (clip != null) {
            try {
                FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                float min = control.getMinimum();
                float max = control.getMaximum();
                float dB = min + (max - min) * volumen;
                control.setValue(dB);
            } catch (Exception e) {
                Log.record(e);
            }
        }
    }

    public void reproducir() {
        if (clip != null && !encendida) {
            clip.setFramePosition(framePosition);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            encendida = true;
        }
    }

    public void pausar() {
        if (clip != null && encendida) {
            framePosition = clip.getFramePosition();
            clip.stop();
            encendida = false;
        }
    }

    public boolean estaEncendida() {
        return encendida;
    }
}