import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public class NamedPlace extends Place implements Serializable{

    public NamedPlace(String name, Position p) {
        super(name, p);
    }

    public String toString() {
        return super.getName();
    }
}
