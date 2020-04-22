class Contact {
    //new contact information variables
    private String lastName, firstName, phoneNumber, emailAddress;

    final public static String[] CONTACT_FIELDS = {"First Name", "Last Name", "Email", "Phone"};
    
    /**********
     *name: getLastName --> GetNotesUser
     *description: Each method returns the related variable
     *input/output: no input, out is the related variable
     ***************************/
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    /**********
     *name: ContactHelper
     *description: attaches the class's variables to the method inputs
     *input/output: input is all the current contact's information
     ***************************/
    public Contact(String lastName, String firstName, String phoneNumber, String emailAddress) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }
   
    /**********
     *name: toString
     *description: prints the contact's information in a clean way
     *input/output: no input, output is a string (the information of the current contact)
     ***************************/
    public String toString() {
        return "Name: " + firstName + " " + lastName + "\n   Phone: " + phoneNumber + ", Email: " + emailAddress;
    }
    /**********
     *name: setAll
     *description: sets all the method inputs to the class's variables
     *input/output: no input, no output (void)
     ***************************/
    public void setAll(String last, String first, String phone, String email) {
        lastName = last;
        firstName = first;
        phoneNumber = phone;
        emailAddress = email;
    }
    /**********
     *name: contains
     *description: returns whether the inputed string matches that of an element of the array that stores the current contact's information
     *input/output: input is string s, output is a boolean value
     ***************************/
    public boolean contains(String s){
        return s.equals(lastName) || s.equals(firstName) || s.equals(phoneNumber) || s.equals(emailAddress);
    }

    public String[] asArray(){
        String[] contactInfo = new String[4];
        contactInfo[0] = lastName;
        contactInfo[1] = firstName;
        contactInfo[2] = phoneNumber;
        contactInfo[3] = emailAddress;

        return contactInfo;
    }
    


}
