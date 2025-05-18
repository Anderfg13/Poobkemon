package presentation;

import javax.sound.sampled.*;
import java.io.File;

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
                // Puede que el sistema no soporte el control de volumen
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