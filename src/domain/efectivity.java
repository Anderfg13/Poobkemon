package domain;

import java.io.Serializable;
import java.util.HashMap;

/**
 * {@code efectivity} gestiona la lógica de efectividad de los ataques entre tipos de Pokémon en el juego Poobkemon.
 * Proporciona una matriz de efectividad y un mapeo de nombres de tipos a índices para calcular el multiplicador de daño
 * según el tipo del atacante y el defensor.
 *
 * <p>Características principales:
 * <ul>
 *   <li>Define una matriz de efectividad de 18x18 para todos los tipos elementales soportados.</li>
 *   <li>Incluye un {@link HashMap} que asocia el nombre del tipo (por ejemplo, "Fuego", "Agua") con su índice en la matriz.</li>
 *   <li>Permite consultar el multiplicador de efectividad entre dos tipos mediante el método {@link #efectividad(int, int)}.</li>
 *   <li>Inicializa automáticamente los valores de la matriz y el mapa de tipos al cargar la clase.</li>
 * </ul>
 *
 *
 * @author  Anderson Fabian Garcia Nieto
 * @author  Christian Alfonso Romero Martinez
 * @version 1.0
 */
public class efectivity implements Serializable {
    private static final long serialVersionUID = 1L;
    /** Matriz de efectividad de ataques entre tipos. */
    private static double[][] matriz = new double[18][18];
    /**
     * Mapa que asocia el nombre de cada tipo con su índice correspondiente en la matriz de efectividad.
     * Ejemplo: "Fuego" → 6, "Agua" → 1, etc.
     */
    public static HashMap<String, Integer> numberType = new HashMap<>();

    // Bloque estático para inicializar el mapa de tipos y la matriz de efectividad.
    static {
        // Inicializar el mapa de tipos
        numberType.put("Acero", 0);
        numberType.put("Agua", 1);
        numberType.put("Bicho", 2);
        numberType.put("Dragon", 3);
        numberType.put("Electrico", 4);
        numberType.put("Fantasma", 5);
        numberType.put("Fuego", 6);
        numberType.put("Hada", 7);
        numberType.put("Hielo", 8);
        numberType.put("Lucha", 9);
        numberType.put("Normal", 10);
        numberType.put("Planta", 11);
        numberType.put("Psiquico", 12);
        numberType.put("Roca", 13);
        numberType.put("Siniestro", 14);
        numberType.put("Tierra", 15);
        numberType.put("Veneno", 16);
        numberType.put("Volador", 17);

        // Inicializar la matriz de efectividad
        valores();
    }

    /**
     * Devuelve el multiplicador de efectividad entre dos tipos.
     *
     * @param tipoAtacante Índice del tipo atacante (según {@link #numberType}).
     * @param tipoDefensor Índice del tipo defensor (según {@link #numberType}).
     * @return Multiplicador de daño (por ejemplo, 2.0 si es súper efectivo, 0.5 si es poco efectivo, 1.0 si es normal).
     */
    public static double efectividad(int tipoAtacante, int tipoDefensor) {
        return matriz[tipoAtacante][tipoDefensor];
    }

    /**
     * Inicializa los valores de la matriz de efectividad entre tipos.
     * Cada posición [i][j] representa la efectividad del tipo i contra el tipo j.
     */
    private static void valores() {
        matriz[0][0] = 0.5;
        matriz[0][1] = 0.5;
        matriz[0][2] = 1;
        matriz[0][3] = 1;
        matriz[0][4] = 0.5;
        matriz[0][5] = 1;
        matriz[0][6] = 0.5;
        matriz[0][7] = 2;
        matriz[0][8] = 2;
        matriz[0][9] = 1;
        matriz[0][10] = 1;
        matriz[0][11] = 1;
        matriz[0][12] = 1;
        matriz[0][13] = 2;
        matriz[0][14] = 1;
        matriz[0][15] = 1;
        matriz[0][16] = 1;
        matriz[0][17] = 1;

        matriz[1][0] = 1;
        matriz[1][1] = 0.5;
        matriz[1][2] = 1;
        matriz[1][3] = 0.5;
        matriz[1][4] = 1;
        matriz[1][5] = 1;
        matriz[1][6] = 2;
        matriz[1][7] = 1;
        matriz[1][8] = 1;
        matriz[1][9] = 1;
        matriz[1][10] = 1;
        matriz[1][11] = 0.5;
        matriz[1][12] = 1;
        matriz[1][13] = 2;
        matriz[1][14] = 1;
        matriz[1][15] = 2;
        matriz[1][16] = 1;
        matriz[1][17] = 1;

        matriz[2][0] = 0.5;
        matriz[2][1] = 1;
        matriz[2][2] = 1;
        matriz[2][3] = 1;
        matriz[2][4] = 1;
        matriz[2][5] = 0.5;
        matriz[2][6] = 0.5;
        matriz[2][7] = 0.5;
        matriz[2][8] = 1;
        matriz[2][9] = 0.5;
        matriz[2][10] = 1;
        matriz[2][11] = 2;
        matriz[2][12] = 2;
        matriz[2][13] = 1;
        matriz[2][14] = 2;
        matriz[2][15] = 1;
        matriz[2][16] = 0.5;
        matriz[2][17] = 0.5;

        matriz[3][0] = 0.5;
        matriz[3][1] = 1;
        matriz[3][2] = 1;
        matriz[3][3] = 2;
        matriz[3][4] = 1;
        matriz[3][5] = 1;
        matriz[3][6] = 1;
        matriz[3][7] = 0;
        matriz[3][8] = 1;
        matriz[3][9] = 1;
        matriz[3][10] = 1;
        matriz[3][11] = 1;
        matriz[3][12] = 1;
        matriz[3][13] = 1;
        matriz[3][14] = 1;
        matriz[3][15] = 1;
        matriz[3][16] = 1;
        matriz[3][17] = 1;

        matriz[4][0] = 1;
        matriz[4][1] = 2;
        matriz[4][2] = 1;
        matriz[4][3] = 0.5;
        matriz[4][4] = 0.5;
        matriz[4][5] = 1;
        matriz[4][6] = 1;
        matriz[4][7] = 1;
        matriz[4][8] = 1;
        matriz[4][9] = 1;
        matriz[4][10] = 1;
        matriz[4][11] = 0.5;
        matriz[4][12] = 1;
        matriz[4][13] = 1;
        matriz[4][14] = 1;
        matriz[4][15] = 0;
        matriz[4][16] = 1;
        matriz[4][17] = 2;

        matriz[5][0] = 1;
        matriz[5][1] = 1;
        matriz[5][2] = 1;
        matriz[5][3] = 1;
        matriz[5][4] = 1;
        matriz[5][5] = 2;
        matriz[5][6] = 1;
        matriz[5][7] = 1;
        matriz[5][8] = 1;
        matriz[5][9] = 1;
        matriz[5][10] = 0;
        matriz[5][11] = 1;
        matriz[5][12] = 2;
        matriz[5][13] = 1;
        matriz[5][14] = 0.5;
        matriz[5][15] = 1;
        matriz[5][16] = 1;
        matriz[5][17] = 1;

        matriz[6][0] = 2;
        matriz[6][1] = 0.5;
        matriz[6][2] = 2;
        matriz[6][3] = 0.5;
        matriz[6][4] = 1;
        matriz[6][5] = 1;
        matriz[6][6] = 0.5;
        matriz[6][7] = 1;
        matriz[6][8] = 2;
        matriz[6][9] = 1;
        matriz[6][10] = 1;
        matriz[6][11] = 2;
        matriz[6][12] = 1;
        matriz[6][13] = 0.5;
        matriz[6][14] = 1;
        matriz[6][15] = 1;
        matriz[6][16] = 1;
        matriz[6][17] = 1;

        matriz[7][0] = 0.5;
        matriz[7][1] = 1;
        matriz[7][2] = 1;
        matriz[7][3] = 2;
        matriz[7][4] = 1;
        matriz[7][5] = 1;
        matriz[7][6] = 0.5;
        matriz[7][7] = 1;
        matriz[7][8] = 1;
        matriz[7][9] = 2;
        matriz[7][10] = 1;
        matriz[7][11] = 1;
        matriz[7][12] = 1;
        matriz[7][13] = 1;
        matriz[7][14] = 2;
        matriz[7][15] = 1;
        matriz[7][16] = 0.5;
        matriz[7][17] = 1;

        matriz[8][0] = 0.5;
        matriz[8][1] = 0.5;
        matriz[8][2] = 1;
        matriz[8][3] = 2;
        matriz[8][4] = 1;
        matriz[8][5] = 1;
        matriz[8][6] = 0.5;
        matriz[8][7] = 1;
        matriz[8][8] = 0.5;
        matriz[8][9] = 1;
        matriz[8][10] = 1;
        matriz[8][11] = 2;
        matriz[8][12] = 1;
        matriz[8][13] = 1;
        matriz[8][14] = 1;
        matriz[8][15] = 2;
        matriz[8][16] = 1;
        matriz[8][17] = 2;

        matriz[9][0] = 2;
        matriz[9][1] = 1;
        matriz[9][2] = 0.5;
        matriz[9][3] = 1;
        matriz[9][4] = 1;
        matriz[9][5] = 0;
        matriz[9][6] = 1;
        matriz[9][7] = 0.5;
        matriz[9][8] = 2;
        matriz[9][9] = 1;
        matriz[9][10] = 2;
        matriz[9][11] = 1;
        matriz[9][12] = 0.5;
        matriz[9][13] = 2;
        matriz[9][14] = 2;
        matriz[9][15] = 1;
        matriz[9][16] = 0.5;
        matriz[9][17] = 0.5;

        matriz[10][0] = 0.5;
        matriz[10][1] = 1;
        matriz[10][2] = 1;
        matriz[10][3] = 1;
        matriz[10][4] = 1;
        matriz[10][5] = 0;
        matriz[10][6] = 1;
        matriz[10][7] = 1;
        matriz[10][8] = 1;
        matriz[10][9] = 1;
        matriz[10][10] = 1;
        matriz[10][11] = 1;
        matriz[10][12] = 1;
        matriz[10][13] = 0.5;
        matriz[10][14] = 1;
        matriz[10][15] = 1;
        matriz[10][16] = 1;
        matriz[10][17] = 1;

        matriz[11][0] = 0.5;
        matriz[11][1] = 2;
        matriz[11][2] = 0.5;
        matriz[11][3] = 0.5;
        matriz[11][4] = 1;
        matriz[11][5] = 1;
        matriz[11][6] = 0.5;
        matriz[11][7] = 1;
        matriz[11][8] = 1;
        matriz[11][9] = 1;
        matriz[11][10] = 1;
        matriz[11][11] = 0.5;
        matriz[11][12] = 1;
        matriz[11][13] = 2;
        matriz[11][14] = 1;
        matriz[11][15] = 2;
        matriz[11][16] = 0.5;
        matriz[11][17] = 0.5;

        matriz[12][0] = 0.5;
        matriz[12][1] = 1;
        matriz[12][2] = 1;
        matriz[12][3] = 1;
        matriz[12][4] = 1;
        matriz[12][5] = 1;
        matriz[12][6] = 1;
        matriz[12][7] = 1;
        matriz[12][8] = 1;
        matriz[12][9] = 2;
        matriz[12][10] = 1;
        matriz[12][11] = 1;
        matriz[12][12] = 0.5;
        matriz[12][13] = 1;
        matriz[12][14] = 0;
        matriz[12][15] = 1;
        matriz[12][16] = 2;
        matriz[12][17] = 1;

        matriz[13][0] = 0.5;
        matriz[13][1] = 1;
        matriz[13][2] = 2;
        matriz[13][3] = 1;
        matriz[13][4] = 1;
        matriz[13][5] = 1;
        matriz[13][6] = 2;
        matriz[13][7] = 1;
        matriz[13][8] = 2;
        matriz[13][9] = 0.5;
        matriz[13][10] = 1;
        matriz[13][11] = 1;
        matriz[13][12] = 1;
        matriz[13][13] = 1;
        matriz[13][14] = 1;
        matriz[13][15] = 0.5;
        matriz[13][16] = 1;
        matriz[13][17] = 2;

        matriz[14][0] = 1;
        matriz[14][1] = 1;
        matriz[14][2] = 1;
        matriz[14][3] = 1;
        matriz[14][4] = 2;
        matriz[14][5] = 1;
        matriz[14][6] = 0.5;
        matriz[14][7] = 1;
        matriz[14][8] = 0.5;
        matriz[14][9] = 1;
        matriz[14][10] = 1;
        matriz[14][11] = 2;
        matriz[14][12] = 1;
        matriz[14][13] = 0.5;
        matriz[14][14] = 0.5;
        matriz[14][15] = 1;
        matriz[14][16] = 1;
        matriz[14][17] = 1;

        matriz[15][0] = 2;
        matriz[15][1] = 1;
        matriz[15][2] = 0.5;
        matriz[15][3] = 1;
        matriz[15][4] = 2;
        matriz[15][5] = 1;
        matriz[15][6] = 2;
        matriz[15][7] = 1;
        matriz[15][8] = 1;
        matriz[15][9] = 1;
        matriz[15][10] = 1;
        matriz[15][11] = 0.5;
        matriz[15][12] = 1;
        matriz[15][13] = 2;
        matriz[15][14] = 1;
        matriz[15][15] = 1;
        matriz[15][16] = 2;
        matriz[15][17] = 0;

        matriz[16][0] = 0;
        matriz[16][1] = 1;
        matriz[16][2] = 1;
        matriz[16][3] = 1;
        matriz[16][4] = 1;
        matriz[16][5] = 0.5;
        matriz[16][6] = 1;
        matriz[16][7] = 2;
        matriz[16][8] = 1;
        matriz[16][9] = 1;
        matriz[16][10] = 1;
        matriz[16][11] = 2;
        matriz[16][12] = 1;
        matriz[16][13] = 0.5;
        matriz[16][14] = 1;
        matriz[16][15] = 0.5;
        matriz[16][16] = 0.5;
        matriz[16][17] = 1;

        matriz[17][0] = 0.5;
        matriz[17][1] = 1;
        matriz[17][2] = 2;
        matriz[17][3] = 1;
        matriz[17][4] = 0.5;
        matriz[17][5] = 1;
        matriz[17][6] = 1;
        matriz[17][7] = 1;
        matriz[17][8] = 1;
        matriz[17][9] = 2;
        matriz[17][10] = 1;
        matriz[17][11] = 2;
        matriz[17][12] = 1;
        matriz[17][13] = 0.5;
        matriz[17][14] = 1;
        matriz[17][15] = 1;
        matriz[17][16] = 1;
        matriz[17][17] = 1;
    }
}
