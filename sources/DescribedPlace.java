import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlace extends Place implements Serializable{

    private String description;
    private String type = "described";

    public DescribedPlace(String name, Position p, String description, String category){
        super(name, p, category);
        this.description = description;
    }

    public String toString() {
        return super.getName() + "\n" + description;
    }

    public String getDescription() {
        return this.description;
    }

    public String getType() {
        return this.type;
    }
}
