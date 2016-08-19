import java.io.Serializable;

/**
 * Created by Melke on 16/04/16.
 */
public abstract class Place implements Serializable{
    private String name;
    private Position position;
    private String category;
    private boolean hidden = false;
    private boolean selected = false;
    private boolean showInfo = false;

    public Place(String name, Position position, String category) {
        this.name =  name;
        this.position = position;
        this.category = category;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Place) {
            Place place = (Place) other;
            Position p = place.getPosition();
            return this.position.equals(p);
        } else
            return false;
    }

    @Override
    public int hashCode() {
        return position.hashCode();
    }

    public String getName() {
        return this.name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setHidden(boolean hide) {
        this.hidden = hide;
    }

    public boolean getHidden() {
        return hidden;
    }

    public Position getPosition() {
        return position;
    }

    public void setSelected(boolean select){
        this.selected = select;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setShowInfo(boolean show) {
        this.showInfo = show;
    }

    public boolean getShowInfo() {
        return showInfo;
    }
}
