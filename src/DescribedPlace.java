/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlace extends Place {

    String description;

    public DescribedPlace(String name, int x, int y, String description){
        super(name, x, y);
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
