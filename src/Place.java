/**
 * Created by Melke on 16/04/16.
 */
public abstract class Place {
    private String name;
    private int x;
    private int y;

    public Place(String name, int x, int y) {
        this.name =  name;
        this.x = x;
        this.y = y;
    }
    public String getName() {
        return this.name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
