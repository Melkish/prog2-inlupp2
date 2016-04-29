import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public class NamedPlace extends Place implements Serializable{

    public NamedPlace(String name, Position p, String category) {
        super(name, p, category);
    }

    public String toString() {
        return super.getName();
    }
}
