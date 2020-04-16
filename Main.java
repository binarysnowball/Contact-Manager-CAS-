import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;
import javax.swing.event.MenuListener;
import java.util.*;
import java.io.*; 
public class Main {
    static Scanner reader = new Scanner(System.in);

    static int currentOption = -1;
    //new integer variable, will be used to store the number of contacts the user has
 
    //new global array (length of 100), will store all the user's contacts
    static Contact[] allContactsArray = new Contact[100];

    static String[] arraySorting = new String[100];

	public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("error loading styler");
        }
        /*     
        //taking in the contacts from the file
        File save = new File("allcontactsvf.csv");
        if (save.exists()) {
            //new buffered reader
            BufferedReader reader = new BufferedReader(new FileReader(save));
            //new string variable, will use the current line being read in the file
            String line;
            //while loop, repeats until the line read is null
            while ((line = reader.readLine()) != null) {
                //each element is separated by a comma, so the comma is used to identify each cell
                //string array stores the contact's information
                String[] parameters = line.split(",");
                //allContactsArray array stores the value and calls the class ContactHelper
                //the class' input is set as all the elements of the array parameters
                allContactsArray[contactCounter++] = new Contact(parameters[0], parameters[1], parameters[2], parameters[3]);
            }
            //file reader is closed
            reader.close();

        }
        //grid.loadCSV(allContactsArray, contactCounter);

        //System.out.print(allContactsArray[0]);
        */   
        //Creating the Frame 
	    JFrame frame = new JFrame("Contact Manager");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(300, 300);
          

	    //Creating the MenuBar and adding components
	    JMenuBar mb = new JMenuBar();
	    JMenu file = new JMenu("FILE");
	    JMenu view = new JMenu("VIEW");
	    JMenu sort = new JMenu("SORT");
	    JMenu search = new JMenu("SEARCH");
	    mb.add(file);
	    mb.add(view);
	    mb.add(sort);
	    mb.add(search);
	    JMenuItem newContact = new JMenuItem("New Contact");
	    JMenuItem editContact = new JMenuItem("Edit Contact");
	    file.add(newContact);
	    file.add(editContact);
	    
        //Creating the panel at bottom and adding components
	    JPanel panel = new JPanel(); // the panel is not visible in output
	    JLabel label = new JLabel("Search Term:");
	    JTextField tf = new JTextField(10); // accepts upto 10 characters
	    JButton send = new JButton("Send");
	     
	    panel.add(label); // Components Added using Flow Layout
	    panel.add(tf);
	    panel.add(send);
	    panel.setVisible(true);

	    // Text Area at the Center
        JPanel[] contentPanels = new JPanel[5];
	    
	    //Adding Components to the frame.
	    frame.getContentPane().add(BorderLayout.SOUTH, panel);
	    frame.getContentPane().add(BorderLayout.NORTH, mb);
	    frame.setVisible(true);

        //-------------------NEW CONTACT GUI START--------------------------//
        contentPanels[0] = new JPanel();
	    contentPanels[0].setBackground(new java.awt.Color(145, 255, 244));
 	        
	    contentPanels[0].setLayout(new GridBagLayout());  
		String text = tf.getText().toString();
		GridBagConstraints c = new GridBagConstraints();
		JLabel title = new JLabel("Enter a new contact:");
		JLabel Identifier = new JLabel("First Name:");
		JTextField IdentifierData = new JTextField(10); // accepts upto 10 characters
		        	
		JLabel Identifier1 = new JLabel("Last Name:");
		JTextField IdentifierData1 = new JTextField(10); // accepts upto 10 characters
		        	
		JLabel Identifier2 = new JLabel("Phone Number:");
		JTextField IdentifierData2 = new JTextField(10);
		        	
		JLabel Identifier3 = new JLabel("Email:");
		JTextField IdentifierData3 = new JTextField(10);
	
  	    JButton confirm = new JButton("Confirm");
        
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		contentPanels[0].add(title, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		contentPanels[0].add(Identifier, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		contentPanels[0].add(IdentifierData, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		contentPanels[0].add(Identifier1, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 2;
		contentPanels[0].add(IdentifierData1, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 3;
		contentPanels[0].add(Identifier2, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 3;
		contentPanels[0].add(IdentifierData2, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		contentPanels[0].add(Identifier3, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 4;
		contentPanels[0].add(IdentifierData3, c);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 5;
		contentPanels[0].add(confirm, c);
   

        confirm.addActionListener(a ->{
            String firstNameText = IdentifierData.getText();
            String lastNameText = IdentifierData1.getText(); 
            String phoneNumberText = IdentifierData2.getText();
            String emailAdressText = IdentifierData3.getText();

            boolean isValid = true;
            //check if input is validate

            if(isValid){
                //allContactsArray.add(new Contact(lastNameText, firstNameText, phoneNumberText, emailAdressText));
            }
        });       	
		contentPanels[0].validate();
 
        newContact.addActionListener(a->{
            switchContent(0, frame, contentPanels);
        });

        
        //-------------------NEW CONTACT GUI END----------------------------//
	    
        //-------------------VIEW GUI START---------------------------------//
        contentPanels[2] = new JPanel();
        
        JLabel tester = new JLabel("Viewing contacts:");
        contentPanels[2].add(tester);
        
        
        
        
        MenuClickListener m = (e) -> {
            switchContent(2, frame, contentPanels); 
        };
        view.addMenuListener(m);
        //-------------------VIEW GUI END-----------------------------------//
    
    }

    static void switchContent(int option, JFrame frame, JPanel[] contentPanels){
        if(currentOption != option){
            if(currentOption != -1){
                frame.getContentPane().remove(contentPanels[currentOption]);
            }

            frame.getContentPane().add(contentPanels[option]);
            frame.revalidate();
            frame.repaint();
            currentOption = option; 
        } 
    }
}    
