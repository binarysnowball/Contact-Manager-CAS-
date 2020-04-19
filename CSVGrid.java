

class CSVGrid extends JScrollPane {
    ArrayList<Contact> contacts;
    private JPanel content;
    private JPanel top;
    private JPanel side;

    public CSVGrid(Path path){
        try {
            //number of lines to read inside file
            lineCount = (int) Files.lines(path).count();
            Scanner lineReader = new Scanner(path);
            //the first line of a CSV file contains the headers, separated by commas
            lineReader.nextLine();
            
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
    }
}