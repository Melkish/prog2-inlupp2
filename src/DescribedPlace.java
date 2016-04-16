/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlace extends Place {

    String description;

    public DescribedPlace(String name, String description){
        super(name);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
