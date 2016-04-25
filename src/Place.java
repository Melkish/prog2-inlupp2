/**
 * Created by Melke on 16/04/16.
 */
public abstract class Place {
    private String name;
    private Position p;

    public Place(String name, Position p) {
        this.name =  name;
        this.p = p;
    }
    public String getName() {
        return this.name;
    }

    public Position getPosition() {
        return p;
    }
}
