public class Player {
    private double x;
    private double y;

    private double lookingAt = 0.0;

    // How many radians per second
    private final double lookSpeed = 0.03;
    private final double walkSpeed = 0.07;

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

    public double getLookingAt() {
        return lookingAt;
    }

    public void setLookingAt(double lookingAt) {
        // Bound between 0 and 2pi
        this.lookingAt = lookingAt % (2 * Math.PI);

        if (this.lookingAt <= 0 )
            this.lookingAt = Math.PI * 2 - this.lookingAt;
    }

    public void addLookingAt(double angle) {
        setLookingAt(lookingAt + angle);
    }

    public void addY(double n) {
        this.y += n;
    }

    public void addX(double n) {
        this.x += n;
    }

    public double getLookSpeed() {
        return lookSpeed;
    }

    public double getWalkSpeed() {
        return walkSpeed;
    }
}
