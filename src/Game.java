import ecs100.*;

import java.awt.*;

public class Game {

    public static void drawMiniMap(Engine engine, double cellSize, double yOffset) {
        // Used to draw player's fov
        double halfFOV = - engine.getRayCaster().getFov() / 2.0;

        // First draw the map's walls
        Map map = engine.getMap();
        UI.setColor(Color.black);
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                // Draw a wall if there's a wall, otherwise clear the space
                if (map.atPos(x, y) == '#')
                    UI.setColor(Color.black);
                else
                    UI.setColor(Color.white);

                // Flip map, I'm not sure why when drawn normally it's flipped vertically
                // But we gotta do this if we want it to match
                UI.fillRect(x * cellSize, yOffset + (map.getHeight() - y) * cellSize, cellSize, cellSize);
            }
        }

        // Draw our player
        Player player = engine.getPlayer();
        double playerX = player.getX() * cellSize;

        // Player must also be flipped to match the map
        double playerY = yOffset + (map.getHeight() - player.getY()) * cellSize;
        UI.setColor(Color.blue);
        UI.fillOval(playerX, playerY, cellSize/3.0, cellSize/3.0);
    }

    public static void main(String[] args) {
        System.out.println("Raycaster adapted from https://www.youtube.com/watch?v=xW8skO7MFYw");

        int columns = 70;
        int rows = 50;
        int gradRows = (int)((double)rows / 2.5);
        double cellSize = 8;

        double centreX = (columns * cellSize) / 2.0;
        double centreY = (rows * cellSize) / 2.0;
        double mouseDeadZone = 20;

        MouseHandler mouseHandler = new MouseHandler();
        UI.setMouseMotionListener(mouseHandler::handleMouse);

        Engine engine = new Engine(columns, rows, cellSize);
        Player player = engine.getPlayer();

        // Game loop here.
        boolean running = true;
        while (running) {
            UI.setColor(Color.black);
            UI.fillRect(0, 0, columns * cellSize, rows * cellSize);

            // Draw a gradient in the background
            for (int i = 0; i < gradRows; i++) {
                int lum = (int)(100 * (Math.pow(1 - (double)i/(double)gradRows, 2)));
                UI.setColor(new Color(lum, lum, lum));

                UI.fillRect(0, i * cellSize, columns * cellSize, cellSize);
                UI.fillRect(0, (rows - i - 1) * cellSize, columns * cellSize, cellSize);
            }

            // Look left and right
            if (mouseHandler.mouseX > centreX + mouseDeadZone) {
                player.addLookingAt(player.getLookSpeed());
            } else if (mouseHandler.mouseX < centreX - mouseDeadZone) {
                player.addLookingAt(-player.getLookSpeed());
            }

            double dX = Double.NaN;
            double dY = Double.NaN;

            // Move forwards and backwards
            if (mouseHandler.mouseY < centreY - mouseDeadZone) {
                dX = Math.sin(player.getLookingAt()) * player.getWalkSpeed();
                dY = Math.cos(player.getLookingAt()) * player.getWalkSpeed();


            } else if (mouseHandler.mouseY > centreY + mouseDeadZone) {
                dX = -Math.sin(player.getLookingAt()) * player.getWalkSpeed();
                dY = -Math.cos(player.getLookingAt()) * player.getWalkSpeed();
            }

            // Check that there's actually some movement
            if (!Double.isNaN(dX) && !Double.isNaN(dY)) {
                player.addX(dX);
                player.addY(dY);

                // Collision
                if (engine.getMap().atPos(player.getX(), player.getY()) == '#'){
                    player.addX(-dX);
                    player.addY(-dY);
                }
            }

            // Draw the scenery
            engine.draw();

            // Draw crosshair
            UI.setColor(Color.CYAN);
            UI.drawOval(centreX - 2, centreY - 2, 4, 4);
            UI.drawOval(centreX - mouseDeadZone, centreY - mouseDeadZone, mouseDeadZone*2, mouseDeadZone*2);

            // Draw minimap
            drawMiniMap(engine, cellSize, rows * cellSize);


            // Print which direction the player is looking at
            // This is quite resource expensive, I recommend commenting out if not needed
            UI.clearText();
            UI.print("Player facing: ");
            int a = (int)Math.round((player.getLookingAt() / (Math.PI * 2)) * 4);
            switch ((a <=0 ) ? 4 + a : a) {
                case 4, 0 -> UI.println('S');
                case 1 -> UI.println('W');
                case 2 -> UI.println('N');
                case 3 -> UI.println('E');
            }

            // ecs100 can handle at most like 30fps
            UI.sleep(1000. / 24.);
        }
    }
}