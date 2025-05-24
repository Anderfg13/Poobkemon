package test;

import domain.Log;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;

class LogTest {

    @Test
    void testRecordCreatesLogFile() {
        Exception ex = new Exception("Test exception for logging");
        // Elimina el archivo de log si existe antes de la prueba
        File logFile = new File(Log.nombre + ".log");
        if (logFile.exists()) {
            logFile.delete();
        }
        assertFalse(logFile.exists());

        // Llama al método de log
        Log.record(ex);

        // Verifica que el archivo de log se haya creado
        assertTrue(logFile.exists());
        // Verifica que el archivo contiene el mensaje de la excepción
        try {
            String content = Files.readString(logFile.toPath());
            assertTrue(content.contains("Test exception for logging"));
        } catch (Exception e) {
            fail("No se pudo leer el archivo de log: " + e.getMessage());
        }
    }

    @Test
    void testRecordHandlesInternalError() {
        // Cambia el nombre del log a una ruta inválida para forzar un error interno
        String originalName = Log.nombre;
        Log.nombre = "/ruta/no/existe/Poobkemon Garcia Romero";
        Exception ex = new Exception("Test error interno");
        // No debe lanzar excepción, solo imprimir la traza y terminar el programa
        try {
            Log.record(ex);
        } catch (Exception e) {
            // Si lanza excepción, la prueba falla
            fail("Log.record debería manejar internamente los errores: " + e.getMessage());
        } finally {
            Log.nombre = originalName; // Restaura el nombre original
        }
    }
}