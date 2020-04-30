import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.io.IOException;
import javax.swing.border.EmptyBorder;


class CSVGrid extends JScrollPane {
    private ArrayList <Contact> contacts;
    //private LinkedList<Contact> contacts;
    private ArrayList <Integer> map;
    private ArrayList <Integer> mapColumns;
    private HighlightableRowsPanel content;
    private JPanel top;
    private HighlightableRowsPanel side;
    private int lineCount;
    private int headerCount;

    private Path path;


    private ArrayList <JLabel> lineNumbers;
    private ArrayList <JTextField> fields;
    private ArrayList <JCheckBox> checkboxes;

    //use path to get contacts when CSVGrid is created
    public CSVGrid(Path path) {

        this.path = path;
        contacts = new ArrayList <> ();
        headerCount = Contact.CONTACT_FIELDS.length;
        try {
            //number of lines to read inside file
            lineCount = (int) Files.lines(path).count();
            Scanner lineReader = new Scanner(path);
            //the first line of a CSV file contains the headers, separated by commas
            lineReader.nextLine();

            //reads the values under the headers
            for (int i = 0; i < lineCount - 1; i++) {
                String[] contactInfo = lineReader.nextLine().split(",");
                contacts.add(new Contact(contactInfo));
            }

        } catch (IOException e) {
            //this shouldn't happen, because bad paths were checked for before, but java requires this.
            e.printStackTrace();
            System.out.println("BAD PATH");
            System.exit(0);
        }

        //creates the initial map for columns
        map = new ArrayList <> ();
        for (int i = 0; i < lineCount - 1; i++) {
            map.add(i);
        }

        //creates the initial map for the position of columns.
        mapColumns = new ArrayList <> ();
        for (int i = 0; i < headerCount; i++) {
            mapColumns.add(i);
        }

        //creates the panel that contains the JTextFields to be scrolled
        content = new HighlightableRowsPanel(this, 25);
        //layout for panel
        GridBagLayout contentLayout = new GridBagLayout();
        content.setLayout(contentLayout);


        //the headers of the grid (won't disappear when scrolling down)
        top = new JPanel();
        GridBagLayout topLayout = new GridBagLayout();
        top.setLayout(topLayout);
        //JLabels for headers
        JLabel[] headers = new JLabel[headerCount];

        //the numbers of the grid (won't disappear when scrolling side to side)
        side = new HighlightableRowsPanel(this, 25);
        GridBagLayout sideLayout = new GridBagLayout();
        side.setLayout(sideLayout);
        //JLabels for line numbers
        lineNumbers = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        content.setBorder(BorderFactory.createLineBorder(Color.red, 2));
        top.setBorder(new EmptyBorder(0, 0, 0, 0));
        side.setBorder(new EmptyBorder(0, 0, 0, 0));

        //instantiates the line numbers and adds them to the side
        for (int i = 0; i < map.size(); i++) {
            lineNumbers.add(new JLabel(Integer.toString(i + 1)));
            lineNumbers.get(i).setMinimumSize(new Dimension(0, 25));
            lineNumbers.get(i).setPreferredSize(new Dimension(lineNumbers.get(i).getPreferredSize().width, 25));

            constraints.weightx = 0;
            constraints.weighty = 1;
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.anchor = GridBagConstraints.PAGE_START;

            side.add(lineNumbers.get(i), constraints);
        }

        //instantiates the headers and adds them to the top
        for (int i = 0; i < headerCount; i++) {
            headers[i] = new JLabel();
            headers[i].setMinimumSize(new Dimension(160, 25));
            headers[i].setPreferredSize(new Dimension(160, 25));

            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = i;
            constraints.gridy = 0;

            headers[i].setText(Contact.CONTACT_FIELDS[i]);
            top.add(headers[i], constraints);
        }

        //JTextFields for fields
        fields = new ArrayList < > (map.size() * headerCount);
        //checkboxes
        checkboxes = new ArrayList < > (map.size());
        for (int i = 0; i < map.size(); i++) {
            final int row = i;
            for (int j = 0; j < headerCount; j++) {
                final int column = j;
                JTextField field = new JTextField();
                //listener that edits the values inside of the CSVObject when the JTextField is edited
                FocusRemovedListener f = (e) -> {
                    JTextField text = (JTextField) e.getComponent();
                    String[] info = getContact(row).asArray();
                    info[column] = text.getText();

                    getContact(row).setAll(info);
                };

                //field.setEditable(false);
                field.addFocusListener(f);

                field.setMinimumSize(new Dimension(160, 25));
                field.setPreferredSize(new Dimension(160, 25));

                constraints.fill = GridBagConstraints.HORIZONTAL;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.gridx = j + 1;
                constraints.gridy = i;
                constraints.anchor = GridBagConstraints.PAGE_START;

                field.setText(getContact(i).asArray()[j]);

                content.add(field, constraints);
                fields.add(field);

            }

            JCheckBox checkbox = new JCheckBox();
            checkbox.setMinimumSize(new Dimension(25, 25));
            checkbox.setPreferredSize(new Dimension(25, 25));

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = 0;
            constraints.gridy = i;
            constraints.anchor = GridBagConstraints.PAGE_START;

            checkbox.addActionListener(a -> {
                if (checkbox.isSelected()) {
                    content.highlightRow(row);
                    side.highlightRow(row);
                    lineNumbers.get(row).setForeground(new Color(255, 255, 255));
                } else {
                    content.unhighlightRow(row);
                    side.unhighlightRow(row);
                    lineNumbers.get(row).setForeground(new Color(0, 0, 0));
                }
            });

            content.add(checkbox, constraints);
            checkboxes.add(checkbox);

        }

        //attaches the top, side, and content to the JScrollPane
        this.setViewportView(content);
        //this.setColumnHeaderView(top);
        this.setRowHeaderView(side);
        System.out.println(lineNumbers.size());
    }

    /*public int[] searchContacts(){

    }
    */
     /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public int getLineCount() {
        return lineCount - 1;
    }

    //gets the contact at a row using the link at a certain part of the map
     /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public Contact getContact(int row) {
        return contacts.get(map.get(row));
    }

    //edit contact
    /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void addContact(Contact c) {
        GridBagConstraints constraints = new GridBagConstraints();
        lineCount++;
        contacts.add(c);
        map.add(lineCount - 2);

        final int row = map.size() - 1;

        for (int j = 0; j < headerCount; j++) {
            final int column = j;
            JTextField field = new JTextField();

            //listener that edits the values inside of the CSVObject when the JTextField is edited
            FocusRemovedListener f = (e) -> {
                JTextField text = (JTextField) e.getComponent();
                String[] info = getContact(row).asArray();
                info[column] = text.getText();

                getContact(row).setAll(info);
            };

            //field.setEditable(false);
            field.addFocusListener(f);

            field.setMinimumSize(new Dimension(160, 25));
            field.setPreferredSize(new Dimension(160, 25));

            constraints.fill = GridBagConstraints.HORIZONTAL;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = j + 1;
            constraints.gridy = row;
            constraints.anchor = GridBagConstraints.PAGE_START;

            field.setText(c.getField(j));

            content.add(field, constraints);
            fields.add(field);
        }

        JLabel lineNumber = new JLabel(Integer.toString(row + 1));
        lineNumber.setMinimumSize(new Dimension(0, 25));
        lineNumber.setPreferredSize(new Dimension(lineNumber.getPreferredSize().width, 25));

        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.PAGE_START;

        side.add(lineNumber, constraints);
        lineNumbers.add(lineNumber);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setMinimumSize(new Dimension(25, 25));
        checkbox.setPreferredSize(new Dimension(25, 25));

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.anchor = GridBagConstraints.PAGE_START;

        checkbox.addActionListener(a -> {
            if (checkbox.isSelected()) {
                content.highlightRow(row);
                side.highlightRow(row);
                lineNumbers.get(row).setForeground(new Color(255, 255, 255));
            } else {
                content.unhighlightRow(row);
                side.unhighlightRow(row);
                lineNumbers.get(row).setForeground(new Color(0, 0, 0));
            }
        });

        content.add(checkbox, constraints);
        checkboxes.add(checkbox);


        this.validate();
        this.repaint();
        System.out.println(lineNumbers.size());
    }

    /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void removeContact(int row) {
        map.remove(row);
    }
    /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void save() {
        try {
            PrintWriter writer = new PrintWriter(path.toFile(), "UTF-8");
            writer.print(toString());
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println("error");
            f.printStackTrace();
        } catch (UnsupportedEncodingException u) {
            System.out.println("other");
        }
    }
    /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public String toString() {
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < headerCount - 1; i++) {
            finalString.append(Contact.CONTACT_FIELDS[i]).append(",");
        }
        finalString.append(Contact.CONTACT_FIELDS[headerCount - 1]).append("\n");
        for (int i = 0; i < map.size(); i++) {
            String[] info = contacts.get(map.get(i)).asArray();
            for (int j = 0; j < headerCount - 1; j++) {
                finalString.append(info[j]).append(",");
            }
            finalString.append(info[headerCount - 1]).append("\n");
        }
        return finalString.toString();
    }
     /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void searches (String searchTerm){
        System.out.println("yes");
        //arraylist will store the contact number that contains the search searchTerm
        
        ArrayList<Integer> locations = new ArrayList<>();
        //used to write to arraylist
        int posLast = 0;
        boolean found = false;
        for (int i = 0; i<getLineCount()-1;i++){
          //takes in a row from the grid in the form of a string array
            String[] current = getContact(i).asArray();
            for (int a = 0; a < headerCount;a++){
              //if an element of the array is the same as the search term, boolean found is set to true
                if (current[a].equals(searchTerm)){
                    found = true;
                    System.out.println("found");
                }
            }
            if (found) {
              //writes to the element at posLast and increments (so it writes to the next element)
                locations.add(posLast, i);
                posLast++;
                
            }
            //resets the boolean in preparation for the next row
            found = false;
        }
        
        for(int row: new ArrayList<Integer>(content.rows)){
            checkboxes.get(row).doClick();
        }
       
        for(int row: locations){
            checkboxes.get(row).doClick();
        }
    }
     /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void sort2(String field, int type) {
        System.out.print("Sort! Field: " + field + " & Direction: ");
        if (type == 1){
            System.out.print("A-Z");
        }
        else{
            System.out.print("Z-A");
        }
        System.out.println();
        int numbLine = getLineCount();
        String[] selectedField = new String[numbLine];

        int[] positions = new int[numbLine];
        //populates positions array

        for (int z = 0; z < numbLine; z++) {
            positions[z] = z + 1;
        }

        //populates the selectedFields array depended on what is choice the combobox is on
        for (int s = 0; s < numbLine; s++) {
            String[] current = getContact(s).asArray();
            if (field.equals("First Name")) {
                selectedField[s] = current[0];
            } else if (field.equals("Last Name")) {
                selectedField[s] = current[1];
            } else if (field.equals("Email")) {
                selectedField[s] = current[2];
            } else {
                selectedField[s] = current[3];
            }
        }
        
        for (int i = 0; i < numbLine; i++) {
            //new loop starts at zero, increments positively by one and ends when the loop integer is one less than contactCounter
            for (int j = i + 1; j < numbLine; j++) {

                //if the elements compared are not in alphabetical order in terms of A to Z
                int compared = selectedField[i].compareTo(selectedField[j]);
                if (compared > 0 && type == 1) {
                    //new variable temp is set to the element of the array allContactsArray at index j
                    
                    int tempInt = positions[i];
                    //element at index j is set to the next element
                    positions[i] = positions[j];
                    //element at index j+1 is set to tempS
                    positions[j] = tempInt;

                    //string tempS is set to the element of the array arraySorting at index k
                    String tempS = selectedField[i];
                    //element at index j is set to the next element
                    selectedField[i] = selectedField[j];
                    //element at index j+1 is set to tempS
                    selectedField[j] = tempS;

                }

                //if the elements compared are not in alphabetical order in terms of Z to A
                else if (compared < 0 && type == 0) {
                    //new variable temp is set to the element of the array allContactsArray at index j
                    int tempInt = positions[i];
                    //element at index j is set to the next element
                    positions[i] = positions[j];
                    //element at index j+1 is set to tempS
                    positions[j] = tempInt;

                    //string tempS is set to the element of the array arraySorting at index k
                    String tempS = selectedField[i];
                    //element at index j is set to the next element
                    selectedField[i] = selectedField[j];
                    //element at index j+1 is set to tempS
                    selectedField[j] = tempS;

                }
            }
        }

            for (int q = 0; q < numbLine; q++) {

                System.out.print(positions[q] + " ");
            }
        System.out.print(" Fields: ");
        for (int e = 0; e < numbLine; e++) {

            System.out.print(selectedField[e] + " ");
        }
    }
     /**********
     *name: 
     *description:
     *input/output:no input, no output (void)
     ***************************/
    public void sort(String field, int type) {
        System.out.print("Sort! Field: " + field + " & Direction: ");
        if (type == 1){
            System.out.print("A-Z");
        }
        else{
            System.out.print("Z-A");
        }
        System.out.println();
        int numbLine = map.size();
        //String[] selectedField = new String[numbLine];

        //int[] positions = new int[numbLine];
        //populates positions array

        //populates the selectedFields array depended on what is choice the combobox is on
        int fieldType = -1;
        for (int s = 0; s < numbLine; s++) {
            String[] current = getContact(s).asArray();
            if (field.equals("First Name")) {
                fieldType = 0;
                //selectedField[s] = current[0];
            } else if (field.equals("Last Name")) {
                fieldType = 1;
                //selectedField[s] = current[1];
            } else if (field.equals("Email")) {
                fieldType = 2;
                //selectedField[s] = current[2];
            } else {
                fieldType = 3;
                //selectedField[s] = current[3];
            }
        }
        
        for (int i = 0; i < numbLine; i++) {
            //new loop starts at zero, increments positively by one and ends when the loop integer is one less than contactCounter
            for (int j = i + 1; j < numbLine; j++) {

                //if the elements compared are not in alphabetical order in terms of A to Z
                int compared = contacts.get(map.get(i)).getField(fieldType).compareTo(contacts.get(map.get(j)).getField(fieldType));
                if (compared > 0 && type == 1) {
                    Collections.swap(map, i, j);
                }

                //if the elements compared are not in alphabetical order in terms of Z to A
                else if (compared < 0 && type == 0) {
                    Collections.swap(map, i, j);
                }
            }
        }

            for (int q = 0; q < numbLine; q++) {

                //System.out.print(map.get(q) + " ");
            }
        //System.out.print(" Fields: ");
        for (int e = 0; e < numbLine; e++) {

            //System.out.print(contacts.get(map.get(e)).getField(fieldType) + " ");
        }

        for(int e = 0; e < numbLine; e++){
            String[] contactInfo = contacts.get(map.get(e)).asArray();
            for(int f = 0; f < headerCount; f++){
                fields.get(e * headerCount + f).setText(contactInfo[f]);
            }
        }

    }
    /**********
     *name: deleteSelectedContacts
     *description: deleted rows that have been selected
     *input/output:no input, no output (void)
     ***************************/
    public void deleteSelectedContacts(){
        ArrayList<Integer> rowsToDelete = new ArrayList<>(content.rows);
        Collections.sort(rowsToDelete, Collections.reverseOrder());

        for(int row : rowsToDelete){
            checkboxes.get(row).doClick();
            map.remove(row);
            side.remove(lineNumbers.remove(map.size()));
            content.remove(checkboxes.remove(map.size()));
            
            for(int j = 0; j < headerCount; j++){
                content.remove(fields.remove(fields.size() - 1));
            }
        }
        for(int i = 0; i < map.size(); i++){
            for(int j = 0; j < headerCount; j++){
                fields.get(i * headerCount + j).setText(contacts.get(map.get(i)).getField(j));
            }
        }
        side.repaint();
        content.repaint();
        System.out.println(lineNumbers.size());
    }

    public double getRowPosition(int row){
        return lineNumbers.get(map.get(row)).getLocation().getY();
    }
    
}