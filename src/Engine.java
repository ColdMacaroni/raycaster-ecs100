import ecs100.*;

public class Engine {
    final private Map map;
    private Player player;

    Engine() {
        map = new Map();

        // Using 0.5 as a starting value so the player is in the centre of a cell.
        double playerStartX = 0.5;
        double playerStartY = 0.5;

        // Find an empty space for the player
        for (int y = 0; y < map.getHeight(); y++) {
            playerStartX = 0.5;
            for (int x = 0; x < map.getWidth(); x++) {
                if (map.atPos(playerStartX, playerStartY) == '.')
                    break;
                playerStartX++;
            }
            playerStartY++;
        }

        player = new Player(playerStartX, playerStartY);

        // Create raycaster object?
    }

    /**
     * This function does all the raycasting and draws it from the screen
     */
    void draw() {
        // Call raycaster object.

    }
}
