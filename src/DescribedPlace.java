/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlace extends Place {

    String description;

    public DescribedPlace(String name, Position p, String description){
        super(name, p);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
