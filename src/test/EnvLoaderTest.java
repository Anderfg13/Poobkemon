package test;

import domain.EnvLoader;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EnvLoaderTest {

    @Test
    void testLoadEnvLoadsKeyValuePairs() throws IOException {
        Path tempFile = Files.createTempFile("test", ".env");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("API_KEY=12345\n");
            writer.write("DB_HOST=localhost\n");
            writer.write("# Esto es un comentario\n");
            writer.write("EMPTY=\n");
            writer.write("\n");
        }
        Map<String, String> env = EnvLoader.loadEnv(tempFile.toString());
        assertEquals("12345", env.get("API_KEY"));
        assertEquals("localhost", env.get("DB_HOST"));
        assertEquals("", env.get("EMPTY"));
        assertFalse(env.containsKey("# Esto es un comentario"));
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLoadEnvIgnoresCommentsAndBlankLines() throws IOException {
        Path tempFile = Files.createTempFile("test", ".env");
        try (FileWriter writer = new FileWriter(tempFile.toFile())) {
            writer.write("# Comentario\n");
            writer.write("\n");
            writer.write("VAR1=valor1\n");
        }
        Map<String, String> env = EnvLoader.loadEnv(tempFile.toString());
        assertEquals(1, env.size());
        assertEquals("valor1", env.get("VAR1"));
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testLoadEnvReturnsEmptyMapIfFileNotFound() {
        Map<String, String> env = EnvLoader.loadEnv("no_existe.env");
        assertTrue(env.isEmpty());
    }
}