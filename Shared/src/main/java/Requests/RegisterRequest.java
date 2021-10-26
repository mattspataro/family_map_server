package Requests;

import Model.Person;

/**
 * Represents JSON data sent from the server to the RegisterService, is a User
 */
public class RegisterRequest extends Request{

    private String userName; //get
    private String password; //get and set
    private String email; //get and set
    private String firstName; //get and set
    private String lastName; //get and set
    private Person.Gender gender; //get
    private String personID; //get

    public RegisterRequest(String username, String password, String email, String firstName, String lastName, Person.Gender gender){
        this(username,password,email,firstName,lastName,gender,"");
    }
    /**
     * Constructs a new user.
     * @param username a non-empty string for username
     * @param password a non-empty string for password
     * @param email a non-empty string for email
     * @param firstName a non-empty string for first name
     * @param lastName a non-empty string for last name
     *
     * @param gender either Male or Female from the Gender enum
     * @param personID a non-empty string for the corresponding person ID
     */
    public RegisterRequest(String username, String password, String email, String firstName, String lastName, Person.Gender gender, String personID) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.personID = personID;
    }

    public String getUsername() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Person.Gender getGender() {
        return gender;
    }

    public String getPersonID() {
        return personID;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setUsername(String username) {
        this.userName = username;
    }

    public void setGender(Person.Gender gender) {
        this.gender = gender;
    }
}
