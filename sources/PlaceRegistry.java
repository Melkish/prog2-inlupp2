import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
/*
 * Created by Melke on 16/04/16.
 */

public class PlaceRegistry extends JFrame {

    public HashMap<String, HashSet<Place>> placesByName = new HashMap<>();
    public HashMap<Position, Place> placesByPosition = new HashMap<>();
    public HashMap<String, HashSet<Place>> placesByCategory = new HashMap<>();
    String[] typesOfPlaces = {"Described place", "Named place"};
    String[] typesOfCategories = {"Bus", "Subway", "Train", ""};
    JList<String> categoryList = new JList<>(typesOfCategories);
    JComboBox<String> chooseTypeOfPlace = new JComboBox<>(typesOfPlaces);
    Position lastSelectedPosition;
    boolean unsavedChanges = false;
    Color myBlue = new Color(174, 218, 232);
    JFileChooser jfc = new JFileChooser(".");
    MapPanel mp = null;
    JTextField searchField = new JTextField(10);
    MouseListener mouseAddListener = new MouseAddListener();
    MouseListener mapMouseListener = new MapMouseListener();


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
        loadPlacesItem.addActionListener(new OpenListener());

        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        saveItem.addActionListener(new SaveListener());

        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exitItem);
        exitItem.addActionListener(new exitListener());

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
        categoryList.setVisibleRowCount(5);
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.addListSelectionListener(new ShowCategoriesListener());
        JScrollPane listScroll = new JScrollPane(categoryList);
        Dimension d = categoryList.getPreferredSize();
        d.width = 120;
        listScroll.setPreferredSize(d);
        right.add(listScroll);

        JButton hideCategoriesButton = new JButton("Hide Categories");
        right.add(hideCategoriesButton);
        hideCategoriesButton.addActionListener(new HideCategoriesListener());

        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.addWindowListener(exitWindowListener);

        setVisible(true);
    }

    WindowListener exitWindowListener = new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            if (unsavedChanges == true) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "You have unsaved changes, are you sure you would like to continue?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        }
    };

    class exitListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            if (unsavedChanges == true) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "You have unsaved changes, are you sure you would like to continue?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm == 0) {
                    System.exit(0);
                }
            } else {
                System.exit(0);
            }
        }
    }

    public class MapMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            int x = mev.getX();
            int y = mev.getY();
            Position clickedPosition = new Position(x, y);
            for (Position p : placesByPosition.keySet()) {
                if (p.equals(clickedPosition)) {
                    Place place = placesByPosition.get(p);
                    if (mev.getButton() == MouseEvent.BUTTON1) {
                        lastSelectedPosition = p;
                        place.setSelected(!place.getSelected());
                    } else if (mev.getButton() == MouseEvent.BUTTON3) {
                        place.setShowInfo(!place.getShowInfo());
                    }
                }
            }
            mp.validate();
            mp.repaint();
        }
    }

    class ShowCategoriesListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lve) {
            String category = categoryList.getSelectedValue();
            for (Map.Entry<String, HashSet<Place>> me : placesByCategory.entrySet()) {
                if (me.getKey().equalsIgnoreCase(category)) {
                    HashSet<Place> set = me.getValue();
                    for (Place p : set) {
                        p.setHidden(false);
                    }
                }
            }
            repaint();
        }
    }

    class HideCategoriesListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String category = categoryList.getSelectedValue();
            for (Map.Entry<String, HashSet<Place>> me : placesByCategory.entrySet()) {
                if (me.getKey().equalsIgnoreCase(category)) {
                    HashSet<Place> set = me.getValue();
                    for (Place p : set) {
                        p.setHidden(true);
                    }
                }
            }
            repaint();
        }
    }

    class NewPlaceListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            mp.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            mp.addMouseListener(mouseAddListener);
        }
    }

    public class MouseAddListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            int x = mev.getX();
            int y = mev.getY();
            mp.validate();
            mp.repaint();
            mp.removeMouseListener(mouseAddListener);
            mp.setCursor(Cursor.getDefaultCursor());

            String chosenTypeOfPlace = (String) chooseTypeOfPlace.getSelectedItem();
            if (chosenTypeOfPlace.equalsIgnoreCase("Named place")) {
                NamedPlaceForm n = new NamedPlaceForm();
                int confirm = JOptionPane.showConfirmDialog(PlaceRegistry.this, n, "Create new named place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = n.getName();
                Position p = new Position(x, y);
                String category = categoryList.getSelectedValue();
                NamedPlace namedPlace = new NamedPlace(name, p, category);
                if (placesByName.containsKey(name)) {
                    HashSet<Place> set = placesByName.get(name);
                    set.add(namedPlace);
                } else {
                    HashSet<Place> newSet = new HashSet<>();
                    newSet.add(namedPlace);
                    placesByName.put(name, newSet);
                }
                placesByPosition.put(p, namedPlace);
                if (category != null) {
                    if (placesByCategory.containsKey(category)) {
                        HashSet<Place> set = placesByCategory.get(category);
                        set.add(namedPlace);
                    } else {
                        HashSet<Place> set = new HashSet<>();
                        set.add(namedPlace);
                        placesByCategory.put(category, set);
                    }
                }
                unsavedChanges = true;
                repaint();

            } else if (chosenTypeOfPlace.equalsIgnoreCase("Described place")) {
                DescribedPlaceForm d = new DescribedPlaceForm();
                int confirm = JOptionPane.showConfirmDialog(PlaceRegistry.this, d, "Create new described place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (confirm != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = d.getName();
                String description = d.getDescription();
                Position p = new Position(x, y);
                String category = categoryList.getSelectedValue();
                DescribedPlace describedPlace = new DescribedPlace(name, p, description, category);
                if (placesByName.keySet().contains(name)) {
                    HashSet<Place> set = placesByName.get(name);
                    set.add(describedPlace);
                } else {
                    HashSet<Place> newSet = new HashSet<>();
                    newSet.add(describedPlace);
                    placesByName.put(name, newSet);
                }
                placesByPosition.put(p, describedPlace);
                if (category != null) {
                    if (placesByCategory.containsKey(category)) {
                        HashSet<Place> set = placesByCategory.get(category);
                        set.add(describedPlace);
                    } else {
                        HashSet<Place> set = new HashSet<>();
                        set.add(describedPlace);
                        placesByCategory.put(category, set);
                    }
                }
                unsavedChanges = true;
                repaint();
            }
        }
    }

    public class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            Collection<Place> placesToIterate = placesByPosition.values();
            Iterator<Place> i = placesToIterate.iterator();
            while (i.hasNext()) {
                Place place = i.next();
                if (place.getSelected() == true) {
                    for (Map.Entry<String, HashSet<Place>> me : placesByName.entrySet()) {
                        HashSet<Place> placeSet = me.getValue();
                        for (Place p : placeSet) {
                            if (p.equals(place)) {
                                placeSet.remove(p);
                                if (placeSet.isEmpty()) {
                                    placesByName.remove(me.getKey());
                                }
                            }
                        }
                    }
                    for (Map.Entry<String, HashSet<Place>> me : placesByCategory.entrySet()) {
                        HashSet<Place> set = me.getValue();
                        for (Place p : set) {
                            if (p.equals(place)) {
                                set.remove(p);
                            }
                        }
                    }
                    i.remove();
                }
            }
            unsavedChanges = true;
            repaint();
        }
    }

    public class HideListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            for (Place p : placesByPosition.values()) {
                if (p.getSelected() == true) {
                    p.setHidden(true);
                    p.setSelected(false);
                    p.setShowInfo(false);
                }
            }
            repaint();
        }
    }

    public class WhatIsHereListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            for (Position p : placesByPosition.keySet()) {
                if (p.equals(lastSelectedPosition)) {
                    Place place = placesByPosition.get(p);
                    place.setHidden(false);
                }
            }
            repaint();
        }
    }

    public class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String searchInput = searchField.getText();
            HashSet<Place> placeSet = placesByName.get(searchInput);
            if (!placesByName.containsKey(searchInput)) {
                return;
            }
            for (Place p : placeSet) {
                p.setSelected(true);
                p.setHidden(false);
            }
            repaint();
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

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(map.getImage(), 0, 0, this);
            for (Position p : placesByPosition.keySet()) {
                Place place = placesByPosition.get(p);
                if (place.getHidden() == false) {
                    String category = place.getCategory();
                    Color triangleColor;
                    if (category == null) {
                        triangleColor = Color.BLACK;
                    } else if (category.equalsIgnoreCase("bus")) {
                        triangleColor = Color.RED;
                    } else if (category.equalsIgnoreCase("train")) {
                        triangleColor = Color.GREEN;
                    } else if (category.equalsIgnoreCase("subway")) {
                        triangleColor = Color.BLUE;
                    } else {
                        triangleColor = Color.BLACK;
                    }
                    g.setColor(triangleColor);
                    Polygon t = new Polygon(new int[]{p.getX(), p.getX() - 15, p.getX() + 15}, new int[]{p.getY(), p.getY() - 25, p.getY() - 25}, 3);
                    g.fillPolygon(t);
                    if (place.getSelected() == true) {
                        g.setColor(Color.MAGENTA);
                        g.drawRect(p.getX() - 15, p.getY() - 25, 30, 25);
                        g.drawRect(p.getX() - 14, p.getY() - 24, 28, 23);
                    }
                    if (place.getShowInfo() == true) {
                        g.setColor(Color.WHITE);
                        g.fillRect(p.getX(), p.getY(), 150, 50);
                        g.setColor(triangleColor);
                        g.drawString(place.toString(), p.getX() + 5, p.getY() + 15);
                    }
                }
            }
        }
    }


    public class OpenMapListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            JFileChooser jfc = new JFileChooser(".");
            int confirm = jfc.showOpenDialog(PlaceRegistry.this);
            if (confirm != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = jfc.getSelectedFile();
            String fileName = file.getAbsolutePath();
            mp = new MapPanel(fileName);
            JScrollPane scroll = new JScrollPane(mp);
            add(scroll, BorderLayout.CENTER);
            mp.addMouseListener(mapMouseListener);
            pack();
            validate();
            repaint();
        }
    }

    public class SaveListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            JFileChooser jfc = new JFileChooser(".");
            int confirm = jfc.showSaveDialog(PlaceRegistry.this);
            if (confirm != jfc.APPROVE_OPTION) {
                return;
            }
            try {
                String fileName = jfc.getSelectedFile().getName();
                FileWriter saveFile = new FileWriter(fileName);
                PrintWriter out = new PrintWriter(saveFile);
                for (String s : placesByName.keySet()) {
                    HashSet<Place> set = placesByName.get(s);
                    for (Place p : set) {
                        Position position = p.getPosition();
                        int x = position.getX();
                        int y = position.getY();
                        if (p instanceof NamedPlace && p.getCategory() != null)
                            out.println(((NamedPlace) p).getType() + "," + p.getCategory() + "," + x + "," + y + "," + p.getName());
                        else if (p instanceof NamedPlace && p.getCategory() == null)
                            out.println(((NamedPlace) p).getType() + "," + "None" + "," + x + "," + y + "," + p.getName());
                        else if (p instanceof DescribedPlace && p.getCategory() != null)
                            out.println(((DescribedPlace) p).getType() + "," + p.getCategory() + "," + x + "," + y + "," + p.getName() + "," + ((DescribedPlace) p).getDescription());
                        else if (p instanceof DescribedPlace && p.getCategory() == null)
                            out.println(((DescribedPlace) p).getType() + "," + "None" + "," + x + "," + y + "," + p.getName() + "," + ((DescribedPlace) p).getDescription());

                        saveFile.close();
                        unsavedChanges = false;
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(PlaceRegistry.this, "Error");
            }
        }
    }

    public class OpenListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            if (unsavedChanges == true) {
                int confirm = JOptionPane.showOptionDialog(
                        null, "You have unsaved changes, are you sure you would like to continue?",
                        "Exit Confirmation", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, null, null);
                if (confirm != 0) {
                    return;
                }
            }
            placesByName.clear();
            placesByPosition.clear();
            placesByCategory.clear();

            JFileChooser jfc = new JFileChooser(".");
            int confirm = jfc.showOpenDialog(PlaceRegistry.this);
            if (confirm != JFileChooser.APPROVE_OPTION) {
                return;
            }
            File file = jfc.getSelectedFile();
            String fileName = file.getAbsolutePath();
            try {
                FileReader in = new FileReader(fileName);
                BufferedReader br = new BufferedReader(in);
                String line;
                while ((line = br.readLine()) != null) {
                    String[] tokens = line.split(",");
                    if (tokens[0].equalsIgnoreCase("described")) {
                        String category = tokens[1];
                        if (category.equalsIgnoreCase("None")) {
                            category = null;
                        }
                        int x = Integer.parseInt(tokens[2]);
                        int y = Integer.parseInt(tokens[3]);
                        String name = tokens[4];
                        String description = tokens[5];

                        Position position = new Position(x, y);
                        DescribedPlace describedPlace = new DescribedPlace(name, position, description, category);
                        placesByPosition.put(position, describedPlace);
                        if (placesByName.containsKey(name)) {
                            HashSet<Place> set = placesByName.get(name);
                            set.add(describedPlace);
                        } else {
                            HashSet<Place> newSet = new HashSet<>();
                            newSet.add(describedPlace);
                            placesByName.put(name, newSet);
                        }
                        if (category != null) {
                            if (placesByCategory.containsKey(category)) {
                                HashSet<Place> set = placesByCategory.get(category);
                                set.add(describedPlace);
                            } else {
                                HashSet<Place> set = new HashSet<>();
                                set.add(describedPlace);
                                placesByCategory.put(category, set);
                            }
                        }
                    } else if (tokens[0].equalsIgnoreCase("named")) {
                        String category = tokens[1];
                        if (category.equalsIgnoreCase("None")) {
                            category = null;
                        }
                        int x = Integer.parseInt(tokens[2]);
                        int y = Integer.parseInt(tokens[3]);
                        String name = tokens[4];

                        Position position = new Position(x, y);
                        NamedPlace namedPlace = new NamedPlace(name, position, category);

                        placesByPosition.put(position, namedPlace);

                        if (placesByName.containsKey(name)) {
                            HashSet<Place> set = placesByName.get(name);
                            set.add(namedPlace);
                        } else {
                            HashSet<Place> newSet = new HashSet<>();
                            newSet.add(namedPlace);
                            placesByName.put(name, newSet);
                        }
                        if (category != null) {
                            if (placesByCategory.containsKey(category)) {
                                HashSet<Place> set = placesByCategory.get(category);
                                set.add(namedPlace);
                            } else {
                                HashSet<Place> set = new HashSet<>();
                                set.add(namedPlace);
                                placesByCategory.put(category, set);
                            }
                        }
                    }
                }
                br.close();
                in.close();
            } catch (FileNotFoundException e) {
                JOptionPane.showMessageDialog(PlaceRegistry.this, "Error");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(PlaceRegistry.this, "Error");
            }
            validate();
            repaint();
        }
    }
}


