import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public abstract class Place implements Serializable{
    private String name;
    private Position p;
    private boolean hidden = false;

    public Place(String name, Position p) {
        this.name =  name;
        this.p = p;
    }
    public String getName() {
        return this.name;
    }

    public void hideHidden() {
        this.hidden = true;
    }

    public void showHidden() {
        this.hidden = false;
    }

    public boolean getHidden() {
        return hidden;
    }

    public Position getPosition() {
        return p;
    }
}
