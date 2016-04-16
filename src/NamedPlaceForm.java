import javax.swing.*;

/**
 * Created by Melke on 16/04/16.
 */
public class NamedPlaceForm extends JPanel {
    private JTextField nameField = new JTextField(10);

    public NamedPlaceForm() {
        JPanel firstRow = new JPanel();
        firstRow.add(new JLabel("Name: "));
        firstRow.add(nameField);
        add(firstRow);
    }

    public String getName() {
        return nameField.getText();
    }
}
