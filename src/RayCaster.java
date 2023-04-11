import ecs100.UI;

import java.util.ArrayList;

public class RayCaster {
    private final double fov = Math.PI / 4.0;
    // The map to check the rays against
    private final Map map;
    // How many rays to cast
    private final int rays;

    // I'm choosing to use an array rather than returning bc speed
    public double[] distances;

    public final double maxDepth;

    public final double rayStep = 0.05;

    RayCaster(Map map, int rays) {
        this.map = map;
        this.rays = rays;
        this.distances = new double[rays];

        this.maxDepth = (map.getWidth() + map.getHeight()) / 2.0;
    }

    /** Saves distance that each ray travels in `this.distances` */
    void raycast(double posX, double posY, double midAngle) {
        for (int i = 0; i < rays; i++) {
            // Start at the leftmost angle of the FOV, and then go in
            // increments until the rightmost
            double angle = (midAngle - fov/2.0) + ((double)i/(double)rays) * fov;
            double dist = 0;

            // To be multiplied by step, so we test every bit of the distance
            double unitX = Math.sin(angle);
            double unitY = Math.cos(angle);


            boolean hitWall = false;
            while (!hitWall && dist < maxDepth) {
                // Move a bit forward
                dist += rayStep;

                // Make our unitX and unitY relative to the player, size of dist
                double testX = posX + (unitX * dist);
                double testY = posY + (unitY * dist);

                // Because our map is in integer units, we can just round at no loss.
                // The real reason to increment via doubles is bc of the player's position
                // Map calls Math.floor on doubles :^)

                // Check if our point is out of bounds
                if (testX < 0 || testX > map.getWidth() || testY < 0 || testY > map.getHeight()) {
                    hitWall = true;

                    // You've gone far enough
                    dist = maxDepth;
                } else if (map.atPos(testX, testY) == '#') {
                        hitWall = true;
                }
            }

            // Store normalised distance
            // TODO! Urgent! Fix fish-eye. I have no idea how. Pull requests appreciated
            distances[i] = dist * Math.cos(midAngle - angle) / maxDepth;
        }
    }

    public int getRays() {
        return rays;
    }

    public double getFov() {
        return fov;
    }

    public double getMaxDepth() {
        return maxDepth;
    }
}
