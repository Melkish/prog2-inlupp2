import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

/**
 * Created by Melke on 16/04/16.
 */

public class PlaceRegistry extends JFrame{

    String[] typesOfPlaces = {"Described place", "Named place"};
    private JComboBox<String> chooseTypeOfPlace = new JComboBox<>(typesOfPlaces);
    Color myBlue = new Color(174, 218, 232);

    public static void main(String[] args) {
        new PlaceRegistry();
    }

    public PlaceRegistry() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newMapItem = new JMenuItem("Open New Map");
        fileMenu.add(newMapItem);
        //TODO newMapItem.addActionListener(new OpenMapListener());

        JMenuItem loadPlacesItem = new JMenuItem("Load places");
        fileMenu.add(loadPlacesItem);

        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);

        JPanel upper = new JPanel();
        add(upper, BorderLayout.NORTH);
        upper.setBackground(myBlue);

        JLabel newLabel = new JLabel("New: ");
        upper.add(newLabel);
        upper.add(chooseTypeOfPlace);
        chooseTypeOfPlace.addActionListener(new NewPlaceListener());

        JTextField searchField = new JTextField(10);
        upper.add(searchField);
        JButton searchButton = new JButton("Search");
        upper.add(searchButton);
        //TODO searchButton.addActionListener();

        JButton hideButton = new JButton("Hide");
        upper.add(hideButton);
        //TODO hideButton.addActionListener();

        JButton removeButton = new JButton("Remove");
        upper.add(removeButton);
        //TODO removeButton.addActionListener();

        JButton whatIsHereButton = new JButton("What is here?");
        upper.add(whatIsHereButton);
        //TODO whatIsHereButton.addActionListener();

        JPanel right = new JPanel();
        add(right, BorderLayout.EAST);
        right.setBackground(myBlue);

        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        JLabel categoriesLabel = new JLabel("Categories");
        right.add(categoriesLabel);
        JTextArea categoriesTextArea = new JTextArea("Bus" + "\n" + "Subway" + "\n" + "Train");
        right.add(categoriesTextArea);
        categoriesTextArea.setEditable(false);
        JButton hideCategoriesButton = new JButton("Hide Categories");
        right.add(hideCategoriesButton);
        //TODO fix so that you can choose a category in the list and add actionListeners

        JPanel middle = new JPanel();
        add(middle, BorderLayout.CENTER);
        middle.setBackground(myBlue);



        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }

    class NewPlaceListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String chosenTypeOfPlace = (String) chooseTypeOfPlace.getSelectedItem();
            //TODO add so that the user can choose a place on the map by clicking on it
            //TODO change the cursor to a cross when the user has to choose a place
            if (chosenTypeOfPlace.equalsIgnoreCase("Named place")) {
                NamedPlaceForm n = new NamedPlaceForm();
                int answer = JOptionPane.showConfirmDialog(PlaceRegistry.this, n, "Create new named place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = n.getName();
                NamedPlace namedPlace = new NamedPlace(name);
                //TODO add to some sort of data structure that isn't an array
                //TODO implement the triangle on the map
                //TODO bus = Color.RED, subway = Color.BLUE, train = Color.GREEN, others = Color.Black

            } else if (chosenTypeOfPlace.equalsIgnoreCase("Described place")) {
                DescribedPlaceForm d = new DescribedPlaceForm();
                int answer = JOptionPane.showConfirmDialog(PlaceRegistry.this, d, "Create new described place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = d.getName();
                String description = d.getDescription();
                DescribedPlace describedPlace = new DescribedPlace(name, description);
                //TODO add to some sort of data structure that isn't an array
                //TODO implement the triangle on the map
                //TODO bus = Color.RED, subway = Color.BLUE, train = Color.GREEN, others = Color.Black
            }
        }
    }

    /* class Show extends JFrame {
        JFileChooser jfc = new JFileChooser(".");

        Show() {
            super("");

            FileFilter fileFilter = new FileNameExtensionFilter("jpg", "gif", "png");
            new OpenMapListener();

        }

    }

    class OpenMapListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            JFileChooser jfc = new JFileChooser(".");
            int answer = jfc.showDialog(Show.this);
        }
    } */
}
