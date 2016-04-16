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
        //TODO chooseTypeOfPlace.addActionListener();

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
}
