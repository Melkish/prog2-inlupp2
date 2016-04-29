import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public class Position implements Serializable{
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

    public boolean intersectsWith(int clickedX, int clickedY) {
    if(clickedX > x-15 && clickedX < x+15 && clickedY < y && clickedY > y-25) {
        return true;
    } else {
        return false;
    }
    }
}
