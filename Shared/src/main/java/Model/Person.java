package Model;


/**
 * A person, living or dead, that is not necessarily a user, but is recorded in a family. Every user is a person, but very few persons are users.
 */
public class Person {
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private Gender gender;

    //optional
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Constructs a person without the optional information.
     * @param personID a unique, non-empty string for the person ID
     * @param associatedUsername a non-empty string for the username of the associated user
     * @param firstName a non-empty string for the first name
     * @param lastName a non-empty string for the first name
     * @param gender a gender enum, either Male or Female
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, Gender gender) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    /**
     * Constructs a person that includes the optional information.
     * @param personID a non-empty string for the person ID
     * @param associatedUsername a non-empty string for the username of the associated user
     * @param firstName a non-empty string for the first name
     * @param lastName a non-empty string for the first name
     * @param gender a gender enum, either Male or Female
     * @param fatherID OPTIONAL (can be null) string containing the id of the father
     * @param motherID OPTIONAL (can be null) string containing the id of the mother
     * @param spouseID OPTIONAL (can be null) string containing the id of the spouse
     */
    public Person(String personID, String associatedUsername, String firstName, String lastName, Gender gender, String fatherID, String motherID, String spouseID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getPersonID() {
        return personID;
    }

    public Gender getGender() {
        return gender;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public void setAssociatedUsername(String associatedUsername) {
        this.associatedUsername = associatedUsername;
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

    public String getFatherID() {
        return fatherID;
    }

    public void setFatherID(String fatherID) {
        this.fatherID = fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public void setMotherID(String motherID) {
        this.motherID = motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }

    public void setSpouseID(String spouseID) {
        this.spouseID = spouseID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Person) {
            Person oPerson = (Person) o;
            boolean isEqual;
            isEqual = oPerson.getPersonID().equals(personID) &&
                      oPerson.getAssociatedUsername().equals(associatedUsername) &&
                      oPerson.getFirstName().equals(firstName) &&
                      oPerson.getLastName().equals(lastName) &&
                      oPerson.getGender().equals(gender);

            if(oPerson.getFatherID() != null && fatherID != null){
                isEqual = isEqual && oPerson.getFatherID().equals(fatherID);
            }
            if(oPerson.getMotherID() != null && motherID != null){
                isEqual = isEqual && oPerson.getMotherID().equals(motherID);
            }
            if(oPerson.getSpouseID() != null && spouseID != null){
                isEqual = isEqual && oPerson.getSpouseID().equals(spouseID);
            }
            return isEqual;
        } else {
            return false;
        }
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString(){
        return personID + "\n" +
                associatedUsername + "\n" +
                firstName + "\n" +
                lastName + "\n" +
                gender + "\n" +
                fatherID + "\n" +
                motherID + "\n" +
                spouseID + "\n";
    }

    /**
     * The Gender enum helps restrict the data to either "m" for Male or "f" for Female
     */
    public enum Gender {
        m, f
    }
    
}
