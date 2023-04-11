import ecs100.*;

import java.awt.*;

public class Game {
    static int columns = 70;
    static int rows = 50;
    static double cellSize = 8;
    static double centreX = (columns * cellSize) / 2.0;
    static double centreY = (rows * cellSize) / 2.0;
    static double mouseDeadZone = 20;
    public static void main(String[] args) {
        System.out.println("Raycaster adapted from https://www.youtube.com/watch?v=xW8skO7MFYw");

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

            double normalisedDistanceFromCenterX = Math.abs(centreX - mouseHandler.mouseX) / centreX;
            double normalisedDistanceFromCenterY = Math.abs(centreY - mouseHandler.mouseY) / centreY;

            // Draw a gradient in the background
            drawGradientBackground();

            if (!inDeadZone(mouseHandler.mouseX, mouseHandler.mouseY)) {
                // Look left and right
                lookingAt(mouseHandler, player, normalisedDistanceFromCenterX);

                movePlayer(mouseHandler, player, engine.getMap(), normalisedDistanceFromCenterY);
            }
            // Draw the scenery
            engine.draw();

            // Draw crosshair
            drawCrosshair();


            // Print which direction the player is looking at
            // This is quite resource expensive, I recommend commenting out if not needed
            UI.clearText();
            UI.print("Player facing: " + getPlayerFacing(player));
            // ecs100 can handle at most like 30fps
            UI.sleep(1000. / 24.);
        }
    }

    static void drawGradientBackground() {
        int gradRows = (int)((double)rows / 2.5);
        for (int i = 0; i < gradRows; i++) {
            int lum = (int)(100 * (Math.pow(1 - (double)i/(double)gradRows, 2)));
            UI.setColor(new Color(lum, lum, lum));

            UI.fillRect(0, i * cellSize, columns * cellSize, cellSize);
            UI.fillRect(0, (rows - i - 1) * cellSize, columns * cellSize, cellSize);
        }
    }

    static void lookingAt(MouseHandler mouseHandler, Player player, double normalisedDistanceFromCenterX) {
        if (mouseHandler.mouseX > centreX) {
            player.addLookingAt(player.getLookSpeed() * normalisedDistanceFromCenterX);
        } else if (mouseHandler.mouseX < centreX) {
            player.addLookingAt(-player.getLookSpeed() * normalisedDistanceFromCenterX);
        }
    }

    static void movePlayer(MouseHandler mouseHandler, Player player, Map map, double normalisedDistanceFromCenterY) {
        double dX = 0.0;
        double dY = 0.0;

        // Move forwards and backwards
        // These aren't working
        if (mouseHandler.mouseY < centreY) {
            dX = Math.sin(player.getLookingAt());
            dY = Math.cos(player.getLookingAt());
        } else if (mouseHandler.mouseY > centreY) {
            dX = -Math.sin(player.getLookingAt());
            dY = -Math.cos(player.getLookingAt());
        }
        dX *= normalisedDistanceFromCenterY * player.getWalkSpeed();
        dY *= normalisedDistanceFromCenterY * player.getWalkSpeed();

        player.addX(dX);
        player.addY(dY);

        // Collision
        if (map.atPos(player.getX(), player.getY()) == '#'){
            player.addX(-dX);
            player.addY(-dY);
        }
    }
    static boolean inDeadZone(double x, double y) {
        if (y <= centreY - mouseDeadZone && y >= centreY + mouseDeadZone) {
            return true;
        }
        if (x <= centreX - mouseDeadZone && x >= centreX + mouseDeadZone) {
            return true;
        }
        return false;
    }

    static void drawCrosshair() {
        UI.setColor(Color.CYAN);
        UI.drawOval(centreX - 2, centreY - 2, 4, 4);
        UI.drawOval(centreX - mouseDeadZone, centreY - mouseDeadZone, mouseDeadZone*2, mouseDeadZone*2);
    }

    static String getPlayerFacing(Player player) {
        int a = (int)Math.round((player.getLookingAt() / (Math.PI * 2)) * 4);
        return switch ((a <=0 ) ? 4 + a : a) {
            case 4, 0 -> "E";
            case 1 -> "N";
            case 2 -> "W";
            case 3 -> "S";
            default -> throw new IllegalStateException("Unexpected value: " + ((a <= 0) ? 4 + a : a));
        };
    }
}