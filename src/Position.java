/**
 * Created by Melke on 16/04/16.
 */
public class Position {
    private int x;
    private int y;

    public Position (int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    //TODO make this class be able ti identify  places from a certain position
    //TODO this class has to be able to work as keys in a hash map
}
