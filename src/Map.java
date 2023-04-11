public class Map {
    private final String map;
    private final int width;
    private final int height;

    Map() {

        map = (
                "#############" +
                "#...........#" +
                "#...........#" +
                "#.....#.....#" +
                "#....###....#" +
                "#.....#.....#" +
                "#...........#" +
                "#############");

        width = 13;
        height = 8;
    }

    Map(String map, int width, int height) {
        this.map = map;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public char atPos(int idx) {
        return map.charAt(idx);
    }

    public char atPos(int x, int y) {
        return atPos(y * width + x);
    }

    public char atPos(double x, double y) {
        return atPos((int)Math.floor(x), (int)Math.floor(y));
    }
}
