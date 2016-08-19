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

    public boolean intersectsWith(int clickedX, int clickedY) {
        if(clickedX > x-15 && clickedX < x+15 && clickedY < y+25 && clickedY > y-25) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode(){
        int hashCode = x * 10000000 + y;
        //Denna hashCode kommer att vara unik så länge kartan inte är större än 9999 pixlar på bredden eller höjden.
        return hashCode;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Position) {
            Position p = (Position) other;
            boolean equalPosition = this.intersectsWith(p.getX(), p.getY());
            return equalPosition;
        } else
            return false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
