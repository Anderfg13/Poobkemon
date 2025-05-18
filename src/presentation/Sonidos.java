package presentation;

import javax.sound.sampled.*;
import java.io.File;

public class Sonidos {
    public static void reproducirClick() {
        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File("mult/button_click.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el sonido: " + e.getMessage());
        }
    }
}