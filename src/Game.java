import ecs100.*;

import java.awt.*;

public class Game {


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
        player.setX(4.5);
        player.setY(4.5);

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
            // These aren't working
            if (mouseHandler.mouseY < centreY - mouseDeadZone) {
                dX = Math.sin(player.getLookingAt()) * player.getWalkSpeed();
                dY = Math.cos(player.getLookingAt()) * player.getWalkSpeed();


            } else if (mouseHandler.mouseY > centreY + mouseDeadZone) {
                dX = -Math.sin(player.getLookingAt()) * player.getWalkSpeed();
                dY = -Math.cos(player.getLookingAt()) * player.getWalkSpeed();
            }

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


            // Print which direction the player is looking at
            // This is quite resource expensive, I recommend commenting out if not needed
            UI.clearText();
            UI.print("Player facing: ");
            int a = (int)Math.round((player.getLookingAt() / (Math.PI * 2)) * 4);
            switch ((a <=0 ) ? 4 + a : a) {
                case 4, 0 -> UI.println('E');
                case 1 -> UI.println('N');
                case 2 -> UI.println('W');
                case 3 -> UI.println('S');
            }

            // ecs100 can handle at most like 30fps
            UI.sleep(1000. / 24.);
        }
    }
}