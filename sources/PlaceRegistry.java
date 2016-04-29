import java.awt.event.*;
import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;
/*
 * Created by Melke on 16/04/16.
 */

public class PlaceRegistry extends JFrame {

    public HashMap<String, Place> placesByName = new HashMap<>();
    public HashMap<Position, Place> placesByPosition = new HashMap<>();
    String[] typesOfPlaces = {"Described place", "Named place"};
    String[] typesOfCategories = {"Bus", "Subway", "Train", ""};
    JList<String> categoryList = new JList<>(typesOfCategories);
    Place lastSelectedPlace;
    private JComboBox<String> chooseTypeOfPlace = new JComboBox<>(typesOfPlaces);
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
        JScrollPane listScroll = new JScrollPane(categoryList);
        Dimension d = categoryList.getPreferredSize();
        d.width = 120;
        listScroll.setPreferredSize(d);
        right.add(listScroll);
        JButton hideCategoriesButton = new JButton("Hide Categories");
        right.add(hideCategoriesButton);
        hideCategoriesButton.addActionListener(new HideCategoriesListener());

        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public class MapMouseListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent mev) {
            int x =  mev.getX();
            int y = mev.getY();
            for (Position p : placesByPosition.keySet()){
                boolean intersects = p.intersectsWith(x,y);
                if (intersects == true) {
                    Place place = placesByPosition.get(p);
                    lastSelectedPlace = place;
                }
            }
            mp.validate();
            mp.repaint();
        }
    }

    class HideCategoriesListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String category = categoryList.getSelectedValue();
            for(String s : placesByName.keySet()) {
                Place p = placesByName.get(s);
                if (category.equalsIgnoreCase(p.getCategory())) {
                    p.hideHidden();
                }
            }
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
                int answer = JOptionPane.showConfirmDialog(PlaceRegistry.this, n, "Create new named place",
                        JOptionPane.OK_CANCEL_OPTION);
                if (answer != JOptionPane.OK_OPTION) {
                    return;
                }
                String name = n.getName();
                Position p = new Position(x, y);
                String category = categoryList.getSelectedValue();
                NamedPlace namedPlace = new NamedPlace(name, p, category);
                placesByName.put(name ,namedPlace);
                placesByPosition.put(p, namedPlace);
                repaint();

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
                String category = categoryList.getSelectedValue();
                DescribedPlace describedPlace = new DescribedPlace(name, p, description, category);
                placesByName.put(name, describedPlace);
                placesByPosition.put(p, describedPlace);
                repaint();
            }
        }
    }

    public class RemoveListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            placesByName.remove(lastSelectedPlace);
            placesByPosition.remove(lastSelectedPlace);
            repaint();
        }
    }

    public class HideListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            lastSelectedPlace.hideHidden();
            repaint();
        }
    }

    public class WhatIsHereListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            lastSelectedPlace.showHidden();
            repaint();
        }
    }

    public class SearchListener implements ActionListener {
        public void actionPerformed(ActionEvent ave) {
            String searchInput = searchField.getText();
            Place place = placesByName.get(searchInput);
            lastSelectedPlace = place;
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
                if (place.getHidden() == true){
                    return;
                }
                String category = place.getCategory();
                Color triangleColor;
                if(category == null) {
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
                if (place == lastSelectedPlace) {
                    g.setColor(Color.WHITE);
                    g.fillRect(p.getX(), p.getY(), 150, 50);
                    g.setColor(Color.BLACK);
                    g.drawString(place.toString(), p.getX() + 5, p.getY() + 15);
                }
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
                mp.addMouseListener(mapMouseListener);
                pack();
                validate();
                repaint();
            }
        }

        public class SaveListener implements ActionListener {
            public void actionPerformed(ActionEvent ave) {
                try{
                    FileWriter saveFile = new FileWriter("place.reg");
                         PrintWriter out = new PrintWriter(saveFile);
                         for(String s : placesByName.keySet()) {
                             Place p = placesByName.get(s);
                             Position position = p.getPosition();
                             int x = position.getX();
                             int y = position.getY();
                             if (p instanceof NamedPlace && p.getCategory() != null)
                                out.println(((NamedPlace) p).getType() + "," + p.getName() + "," + x + "," + y + "," + p.getCategory());
                             else if (p instanceof NamedPlace && p.getCategory() == null)
                                 out.println(((NamedPlace) p).getType() + "," + p.getName() + ","  + "," + x + "," + y + ",");
                             else if (p instanceof DescribedPlace && p.getCategory() != null)
                                 out.println(((DescribedPlace) p).getType() + "," + p.getName() + "," + ((DescribedPlace) p).getDescription()+ ","  + "," + x + "," + y + ","  + p.getCategory());
                             else if (p instanceof DescribedPlace && p.getCategory() == null)
                                 out.println(((DescribedPlace) p).getType() + "," + p.getName() + "," + ((DescribedPlace) p).getDescription()+ ","  + "," + x + "," + y + ",");
                             saveFile.close();
                         }
                 }catch(IOException e){
                    JOptionPane.showMessageDialog(PlaceRegistry.this,"Error");
            }
            }
        }
        public class OpenListener implements ActionListener {
            public void actionPerformed(ActionEvent ave) {
                try{
                    FileReader in = new FileReader("place.reg");
                    BufferedReader br = new BufferedReader(in);
                    String line;
                    while ((line=br.readLine()) != null) {
                        String[] tokens = line.split(",");
                        if (tokens[0].equalsIgnoreCase("named")) {
                            String name = tokens[1];
                            int x = Integer.parseInt(tokens[2]);
                            int y = Integer.parseInt(tokens[3]);
                            String description = tokens[3];
                            String category = tokens[4];

                        Position position = new Position(x,y);
                        DescribedPlace p = new DescribedPlace(name, position, description, category);
                        placesByName.put(name, p);
                        placesByPosition.put(position, p);
                        }
                        else {
                            String name = tokens[1];
                            int x = Integer.parseInt(tokens[2]);
                            int y = Integer.parseInt(tokens[3]);
                            String category = tokens[3];

                            Position position = new Position(x,y);
                            NamedPlace p = new NamedPlace(name, position, category);
                            placesByName.put(name, p);
                            placesByPosition.put(position, p);
                        }
                    }
                    br.close();
                    in.close();
                }catch(FileNotFoundException e){
                    JOptionPane.showMessageDialog(PlaceRegistry.this,"Error");
                }catch(IOException e){
                    JOptionPane.showMessageDialog(PlaceRegistry.this,"Error");
                }
            }
        }
    }

