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

public class PlaceRegistry extends JFrame {

    public HashMap<String, Place> placesByName = new HashMap<>();
    public HashMap<Position, Place> placesByPosition = new HashMap<>();
    public HashMap<String, Place> placesByCategories = new HashMap<>();
    String[] typesOfPlaces = {"Described place", "Named place"};
    String[] typesOfCategories = {"Bus", "Subway", "Train"};
    private JComboBox<String> chooseTypeOfPlace = new JComboBox<>(typesOfPlaces);
    Color myBlue = new Color(174, 218, 232);
    JFileChooser jfc = new JFileChooser(".");
    MapPanel mp = null;
    JTextField searchField = new JTextField(10);
    MouseListener mouseListener = new MouseListener();



    public static void main(String[] args) {
        new PlaceRegistry();
    }

    public PlaceRegistry() {
        super("My awesomely cool place registry");

        FileFilter fileFilter = new FileNameExtensionFilter("jpg", "gif", "png");
        jfc.setFileFilter(fileFilter);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);

        JMenuItem newMapItem = new JMenuItem("Open New Map");
        fileMenu.add(newMapItem);
        newMapItem.addActionListener(new OpenMapListener());

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

        searchField = new JTextField(10);
        upper.add(searchField);
        JButton searchButton = new JButton("Search");
        upper.add(searchButton);
        searchButton.addActionListener(new SearchListener());

        JButton hideButton = new JButton("Hide");
        upper.add(hideButton);
        hideButton.addActionListener(new HideListener());

        JButton removeButton = new JButton("Remove");
        upper.add(removeButton);
        removeButton.addActionListener(new RemoveListener());

        JButton whatIsHereButton = new JButton("What is here?");
        upper.add(whatIsHereButton);
        whatIsHereButton.addActionListener(new WhatIsHereListener());

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

        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }

    class NewPlaceListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            mp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            mp.addMouseListener(mouseListener);
        }
    }

    public class MouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            int x = mev.getX();
            int y = mev.getY();
            mp.validate();
            mp.repaint();
            mp.removeMouseListener(mouseListener);
            mp.setCursor(Cursor.getDefaultCursor());

            String chosenTypeOfPlace = (String) chooseTypeOfPlace.getSelectedItem();
            if (chosenTypeOfPlace.equalsIgnoreCase("Named place")) {
                NamedPlaceForm n = new NamedPlaceForm();
                int answer = JOptionPane.showConfirmDialog(PlaceRegistry.this, n, "Create new named place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = n.getName();
                Position p = new Position(x, y);
                NamedPlace namedPlace = new NamedPlace(name, p);
                placesByName.put(name ,namedPlace);
                placesByPosition.put(p, namedPlace);
                repaint();

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
                Position p = new Position(x, y);
                DescribedPlace describedPlace = new DescribedPlace(name, p, description);
                placesByName.put(name, describedPlace);
                placesByPosition.put(p, describedPlace);
                repaint();

                //TODO implement the triangle on the map
                //TODO bus = Color.RED, subway = Color.BLUE, train = Color.GREEN, others = Color.Black
            }
        }
    }

    public class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            //placesByName.remove();
            //placesByPosition.remove();
            //placesByCategories.remove();
            //TODO add functionality to remove an object
        }
    }

    public class HideListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            //TODO add functionality to hide an object
        }
    }

    public class WhatIsHereListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            //TODO add functionality to show the object at the position
            //placesByPosition.get();
        }
    }

    public class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String searchInput = searchField.getText();
            placesByName.get(searchInput);
            //TODO display the object
        }
    }


    public class MapPanel extends JPanel {
        private ImageIcon map;

        public MapPanel(String fileName) {
            map = new ImageIcon(fileName);
            int w = map.getIconWidth();
            int h = map.getIconHeight();
            setPreferredSize(new Dimension(w, h));
        }
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(map.getImage(), 0, 0, this);
            for (Position p : placesByPosition.keySet()) {
                Place place = placesByPosition.get(p);
                //TODO implement categories
                g.setColor(Color.BLACK);
                Polygon t = new Polygon(new int[]{p.getX(), p.getX()-15, p.getX()+15}, new int[]{p.getY(), p.getY()-25, p.getY()-25}, 3);
                g.fillPolygon(t);
            }
        }

    }


        public class OpenMapListener implements ActionListener {
            public void actionPerformed(ActionEvent ave) {
                JFileChooser jfc = new JFileChooser(".");
                int answer = jfc.showOpenDialog(PlaceRegistry.this);
                if (answer != JFileChooser.APPROVE_OPTION) {
                    return;
                }
                File file = jfc.getSelectedFile();
                String fileName = file.getAbsolutePath();
                mp = new MapPanel(fileName);
                JScrollPane scroll = new JScrollPane(mp);
                add(scroll, BorderLayout.CENTER);
                pack();
                validate();
                repaint();
        }
    }
}
