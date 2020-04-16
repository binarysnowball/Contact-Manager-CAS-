import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class CSVObject {

    private String[] headers;
    private ArrayList<ArrayList<String>> values;
    private ArrayList<Integer> mapColumns;
    private ArrayList<Integer> mapRows;
    private int headerCount;
    private int lineCount;

    CSVObject(Path path) {
        //record the headers of the CSV file and their values
        try {
            //number of lines to read inside file
            lineCount = (int) Files.lines(path).count();
            Scanner lineReader = new Scanner(path);
            //the first line of a CSV file contains the headers, separated by commas
            headers = lineReader.nextLine().split(",");
            headerCount = headers.length;

            //removes whitespace from beginning and end
            for (int i = 0; i < headerCount; i++) {
                headers[i] = headers[i].trim();
            }

            //reads the values under the headers
            values = new ArrayList<>();
            for (int i = 0; i < lineCount - 1; i++) {
                values.add(new ArrayList<>(Arrays.asList(lineReader.nextLine().split(","))));
            }

        } catch (IOException e) {
            //this shouldn't happen, because bad paths were checked for before, but java requires this.
            e.printStackTrace();
            System.out.println("BAD PATH");
            System.exit(0);
        }

        //creates the initial map for columns; a map for every single column is not needed, because rows must change position together.
        mapColumns = new ArrayList<>();
        for (int i = 0; i < lineCount - 1; i++) {
            mapColumns.add(i);
        }

        //creates the initial map for the position of columns.
        mapRows = new ArrayList<>();
        for (int i = 0; i < headerCount; i++) {
            mapRows.add(i);
        }
    }

    public int getHeaderCount(){
        return headerCount;
    }

    public int getLineCount(){
        return lineCount - 1;
    }

    public String[] getHeaders(){
        String[] mappedHeaders = new String[headerCount];
        for (int i = 0; i < headerCount; i++) {
            mappedHeaders[i] = headers[mapRows.get(i)];
        }

        return mappedHeaders;
    }

    //replaces the link to the original values at a certain part of the map with another link.
    public void reorderRow(int row, int newLink) {
        mapColumns.set(row, newLink);
    }

    //changes the order of columns in the map
    public void reorderColumn(int previousColumn, int newColumn){
        int mappedColumn = mapRows.remove(previousColumn);
        if(previousColumn > newColumn) {
            mapRows.add(newColumn, mappedColumn);
        } else if(previousColumn < newColumn){
            mapRows.add(newColumn, mappedColumn);
        }

    }

    //gets the value of an element using the link at a certain part of the map
    public String getElement(int row, int column){
        return values.get(mapColumns.get(row)).get(mapRows.get(column));
    }

    //sets the value of an element using the link at a certain part of the map
    public void setElement(int row, int column, String s){
        values.get(mapColumns.get(row)).set(mapRows.get(column), s);
    }

    //gets the entire map of a column
    public ArrayList<Integer> getMapColumn(int low, int high) {
        ArrayList<Integer> shortColumn = new ArrayList<>();
        for (int i = low; i <= high; i++) {
            shortColumn.add(mapColumns.get(i));
        }

        return shortColumn;
    }

    //gets the values of a column using the map
    public ArrayList<String> getValueColumn(int column, int low, int high){
        ArrayList<String> shortColumn = new ArrayList<>();
        for (int i = low; i <= high; i++) {
            shortColumn.add(values.get(i).get(mapRows.get(column)));
        }

        return shortColumn;
    }

    //creates a string to write to a file when saving
    @Override
    public String toString() {
        StringBuilder finalString = new StringBuilder();
        for (int i = 0; i < headerCount - 1; i++) {
            finalString.append(headers[mapRows.get(i)]).append(",");
        }
        finalString.append(headers[mapRows.get(headerCount - 1)]).append("\n");
        for (int i = 0; i < lineCount - 1; i++) {
            ArrayList<String> row = values.get(mapColumns.get(i));
            for (int j = 0; j < headerCount - 1; j++) {
                finalString.append(row.get(mapRows.get(j))).append(",");
            }
            finalString.append(row.get(mapRows.get(headerCount - 1))).append("\n");
        }
        return finalString.toString();
    }
}

