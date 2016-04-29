import javax.swing.*;

/**
 * Created by Melke on 16/04/16.
 */
public class DescribedPlaceForm extends JPanel {
    private JTextField nameField = new JTextField(10);
    private JTextField descriptionField = new JTextField(20);

    public DescribedPlaceForm() {
        JPanel firstRow = new JPanel();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        firstRow.add(new JLabel("Name: "));
        firstRow.add(nameField);
        add(firstRow);

        JPanel secondRow = new JPanel();
        secondRow.add(new JLabel("Description: "));
        secondRow.add(descriptionField);
        add(secondRow);

    }

    public String getName() {
        return nameField.getText();
    }

    public String getDescription() {
        return descriptionField.getText();
    }
}
