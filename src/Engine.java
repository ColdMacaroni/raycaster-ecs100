import ecs100.*;

import java.awt.*;

public class Engine {
    private final Map map;
    private Player player;
    private final RayCaster rayCaster;

    private int columns;
    private int rows;
    private double cellSize;

    // Calculate this once in the constructor rather than every tick
    private double midHeight;

    Engine(int columns, int rows, double cellSize) {
        this.map = new Map();

        this.columns = columns;
        this.rows = rows;
        this.cellSize = cellSize;
        this.midHeight = this.rows / 2.0 * this.cellSize;

        // Using 0.5 as a starting value so the player is in the centre of a cell.Z
        double playerStartX = 0.5;
        double playerStartY = 0.5;

        // Find an empty space for the player
        findpos: for (int y = 0; y < map.getHeight(); y++) {
            playerStartX = 0.5;
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.atPos(playerStartX, playerStartY) == '.')
                    break findpos;
                playerStartX++;
            }
            playerStartY++;
        }

        this.player = new Player(playerStartX, playerStartY);
        this.rayCaster = new RayCaster(map, this.columns);

    }

    /**
     * This function does all the raycasting and draws it from the screen
     */
    void draw() {
        // Update rays
        rayCaster.raycast(player.getX(), player.getY(), player.getLookingAt());

        for (int i = 0; i < columns; i++) {
            // 0 is far, 1 is close
            double dist = 1 - rayCaster.distances[i];

            // How far the halls should be drawn each way
            double height = dist * rows * cellSize;

            // Luminosity of the wall
            int lum = (int)(240 * dist * dist);
            UI.setColor(new Color(lum, lum, lum));

            UI.fillRect(i*cellSize, midHeight - height / 2.,
                        cellSize, height);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public double getCellSize() {
        return cellSize;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Map getMap() {
        return map;
    }
}
