import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.ArrayList;


public class CSVGrid extends JScrollPane {
    private CSVObject csv;
    private JLabel[] headers;
    private JTextField[][] values;
    private JPanel content;
    private JPanel top;
    private JPanel side;

    //the display area is blank before loadCSV is called. No constructor is needed.

    //loads a new CSV file
    public void loadCSV(CSVObject csv) {
        //creates CSVObject
        csv = new CSVObject(path);

        //creates the panel that contains the JTextFields to be scrolled
        content = new JPanel();
        //layout for panel
        GridBagLayout contentLayout = new GridBagLayout();
        content.setLayout(contentLayout);

        //the headers of the grid (won't disappear when scrolling down)
        top = new JPanel();
        GridBagLayout topLayout = new GridBagLayout();
        top.setLayout(topLayout);
        //JLabels for headers
        headers = new JLabel[csv.getHeaderCount()];

        //the numbers of the grid (won't disappear when scrolling side to side)
        side = new JPanel();
        GridBagLayout sideLayout = new GridBagLayout();
        side.setLayout(sideLayout);
        //JLabels for line numbers
        JLabel[] lineNumbers = new JLabel[csv.getLineCount()];

        GridBagConstraints constraints = new GridBagConstraints();


        //instantiates the line numbers and adds them to the side
        for (int i = 0; i < csv.getLineCount(); i++) {
            lineNumbers[i] = new JLabel(Integer.toString(i + 1));
            lineNumbers[i].setMinimumSize(new Dimension(0, 20));
            lineNumbers[i].setPreferredSize(new Dimension(lineNumbers[i].getPreferredSize().width, 20));

            constraints.weightx = 0;
            constraints.weighty = 1;
            constraints.gridx = 0;
            constraints.gridy = i;

            side.add(lineNumbers[i], constraints);
        }

        //instantiates the headers and adds them to the top
        for (int i = 0; i < csv.getHeaderCount(); i++) {
            headers[i] = new JLabel();
            headers[i].setMinimumSize(new Dimension(150, 20));
            headers[i].setPreferredSize(new Dimension(150, 20));

            constraints.weightx = 1;
            constraints.weighty = 0;
            constraints.gridx = i;
            constraints.gridy = 0;

            top.add(headers[i], constraints);
        }

        //creates the JTextFields for the values and adds them to the display area
        values = new JTextField[csv.getLineCount()][csv.getHeaderCount()];

        for (int i = 0; i < csv.getLineCount(); i++) {
            for (int j = 0; j < csv.getHeaderCount(); j++) {
                final int row = i;
                final int column = j;
                values[i][j] = new JTextField();
                //listener that edits the values inside of the CSVObject when the JTextField is edited
                FocusRemovedListener f = (e) -> {
                    JTextField text = (JTextField) e.getComponent();
                    csv.setElement(row, column, text.getText());
                };

                values[i][j].addFocusListener(f);

                values[i][j].setMinimumSize(new Dimension(150, 20));
                values[i][j].setPreferredSize(new Dimension(150, 20));

                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 1;
                constraints.weighty = 1;
                constraints.gridx = j;
                constraints.gridy = i;

                content.add(values[i][j], constraints);
            }

        }

        refreshValues();

        //attaches the top, side, and content to the JScrollPane
        this.setViewportView(content);
        this.setColumnHeaderView(top);
        this.setRowHeaderView(side);
    }

    //sets the text inside of the JTextFields and JLabels
    private void refreshValues() {
        for (int i = 0; i < csv.getHeaderCount(); i++) {
            headers[i].setText(csv.getHeaders()[i]);
        }

        for (int i = 0; i < csv.getLineCount(); i++) {
            for (int j = 0; j < csv.getHeaderCount(); j++) {
                values[i][j].setText(csv.getElement(i, j));
            }
        }
    }


    public String[] getHeaders() {
        return csv.getHeaders();
    }

    public int getLineCount() {
        return csv.getLineCount();
    }

    //sorts a specified column using a range of rows
    public void sort(int column, int low, int high) {
        ArrayList<Integer> newMap = Main.quickSortV3(csv.getValueColumn(column, low, high), csv.getMapColumn(low, high));
        //sets the map with new values
        for (int i = low; i <= high; i++) {
            csv.reorderRow(i, newMap.get(i));
        }

        refreshValues();
    }

    //shifts a column
    public void shift(int previousColumn, int newColumn) {
        csv.reorderColumn(previousColumn, newColumn);
        refreshValues();
    }

    //saves the CSVObject
    public void save(String path) {
        try {
            PrintWriter writer = new PrintWriter(new File(path), "UTF-8");
            writer.print(csv.toString());
            writer.close();
        } catch (FileNotFoundException f) {
            System.out.println("error");
            f.printStackTrace();
        } catch (UnsupportedEncodingException u) {
            System.out.println("other");
        }
    }

}
