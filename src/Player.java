public class Player {
    private double x;
    private double y;
    final private double fov = Math.PI * 0.75;

    private double lookingAt = 0;

    // How many radians per second
    final private double lookSpeed = Math.PI/2.0;
    final private double walkSpeed = 0.1;

    Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getFov() {
        return fov;
    }

    public double getLookingAt() {
        return lookingAt;
    }

    public void setLookingAt(double lookingAt) {
        // Keep to one revolution
        this.lookingAt = lookingAt % (2 * Math.PI);
    }

    public double getLookSpeed() {
        return lookSpeed;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }
}
