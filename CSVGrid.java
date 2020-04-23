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
import java.util.Scanner;
import java.io.IOException;
import javax.swing.border.EmptyBorder;


class CSVGrid extends JScrollPane {
    private ArrayList<Contact> contacts;
    //private LinkedList<Contact> contacts;
    private ArrayList<Integer> map;
    private ArrayList<Integer> mapColumns;
    private HighlightableRowsPanel content;
    private JPanel top;
    private HighlightableRowsPanel side;
    private int lineCount;
    private int headerCount;

    private Path path;


    private ArrayList<JLabel> lineNumbers;
    private ArrayList<JTextField> fields;
    private ArrayList<JCheckBox> checkboxes;

    //use path to get contacts when CSVGrid is created
    public CSVGrid(Path path){

        this.path = path;
        contacts = new ArrayList<>();
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
        map = new ArrayList<>();
        for (int i = 0; i < lineCount - 1; i++) {
            map.add(i);
        }

        //creates the initial map for the position of columns.
        mapColumns = new ArrayList<>();
        for (int i = 0; i < headerCount; i++) {
            mapColumns.add(i);
        }

        //creates the panel that contains the JTextFields to be scrolled
        content = new HighlightableRowsPanel(25);
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
        side = new HighlightableRowsPanel(25);
        GridBagLayout sideLayout = new GridBagLayout();
        side.setLayout(sideLayout);
        //JLabels for line numbers
        lineNumbers = new ArrayList<>();

        GridBagConstraints constraints = new GridBagConstraints();

        content.setBorder(new EmptyBorder(0, 0, 0, 0));
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

            side.add(lineNumbers.get(i), constraints);
        }

        //instantiates the headers and adds them to the top
        for (int i = 0; i < headerCount; i++) {
            headers[i] = new JLabel();
            headers[i].setMinimumSize(new Dimension(160, 25));
            headers[i].setPreferredSize(new Dimension(160, 25));

            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.gridx = i;
            constraints.gridy = 0;

            headers[i].setText(Contact.CONTACT_FIELDS[i]);
            top.add(headers[i], constraints);
        }

        //JTextFields for fields
        fields = new ArrayList<>(map.size() * headerCount);
        //checkboxes
        checkboxes = new ArrayList<>(map.size());
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

                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.gridx = j + 1;
                constraints.gridy = i;

                field.setText(getContact(i).asArray()[j]);

                content.add(field, constraints);
                fields.add(field);

            }

            JCheckBox checkbox = new JCheckBox();
            checkbox.setMinimumSize(new Dimension(25, 25));
            checkbox.setPreferredSize(new Dimension(25, 25));

            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = 0;
            constraints.gridy = i;

            checkbox.addActionListener(a ->{
                if(checkbox.isSelected()){
                    content.highlightRow(row);
                    side.highlightRow(row);
                    lineNumbers.get(row).setForeground(new Color(255,255,255));
                } else {
                    content.unhighlightRow(row);
                    side.unhighlightRow(row);
                    lineNumbers.get(row).setForeground(new Color(0,0,0));
                }
            });

            content.add(checkbox, constraints);
            checkboxes.add(checkbox);

        }

        //attaches the top, side, and content to the JScrollPane
        this.setViewportView(content);
        //this.setColumnHeaderView(top);
        this.setRowHeaderView(side);
    }

    /*public int[] searchContacts(){

    }
    */
    public int getLineCount(){
        return lineCount - 1;
    }

    //gets the contact at a row using the link at a certain part of the map
    public Contact getContact(int row){
        return contacts.get(map.get(row));
    }

    //edit contact

    public void addContact(Contact c){
        GridBagConstraints constraints = new GridBagConstraints();
        lineCount++;
        contacts.add(c);
        map.add(lineCount - 2);

        int i = lineCount - 2;
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

            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.weighty = 1;
            constraints.gridx = j + 1;
            constraints.gridy = i;

            field.setText(c.getField(j));

            content.add(field, constraints);
            fields.add(field);
        }

        JLabel lineNumber = new JLabel(Integer.toString(map.size() - 1 + 1));
        lineNumber.setMinimumSize(new Dimension(0, 25));
        lineNumber.setPreferredSize(new Dimension(lineNumber.getPreferredSize().width, 25));

        constraints.weightx = 0;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = map.size() - 1;

        side.add(lineNumber, constraints);
        lineNumbers.add(lineNumber);

        JCheckBox checkbox = new JCheckBox();
        checkbox.setMinimumSize(new Dimension(25, 25));
        checkbox.setPreferredSize(new Dimension(25, 25));

        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.gridx = 0;
        constraints.gridy = i;

        checkbox.addActionListener(a ->{
            if(checkbox.isSelected()){
                content.highlightRow(row);
                side.highlightRow(row);
                lineNumbers.get(row).setForeground(new Color(255,255,255));
            } else {
                content.unhighlightRow(row);
                side.unhighlightRow(row);
                lineNumbers.get(row).setForeground(new Color(0,0,0));
            }
        });

        content.add(checkbox, constraints);
        checkboxes.add(checkbox);


        this.validate();
        this.repaint();
    }


    public void removeContact(int row){
        map.remove(row);
    }

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
}