import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlace extends Place implements Serializable{

    String description;

    public DescribedPlace(String name, Position p, String description){
        super(name, p);
        this.description = description;
    }

    public String toString() {
        return super.getName() + "\n" + description;
    }
}
