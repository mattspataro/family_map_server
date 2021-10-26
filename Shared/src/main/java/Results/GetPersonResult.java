package Results;

import Model.Person;

/**
 * Represents JSON data sent from the GetPersonService back to the server
 */
public class GetPersonResult extends Result{
    private String personID;
    private String associatedUsername;
    private String firstName;
    private String lastName;
    private Person.Gender gender;

    //optional
    private String fatherID;
    private String motherID;
    private String spouseID;

    /**
     * Constructs a new GetPersonResult object with the given person
     * @param person a person
     */
    public GetPersonResult(Person person) {
        this.setSuccess(true);

        personID = person.getPersonID();
        associatedUsername = person.getAssociatedUsername();
        firstName = person.getFirstName();
        lastName = person.getLastName();
        gender = person.getGender();

        fatherID = person.getFatherID();
        motherID = person.getMotherID();
        spouseID = person.getSpouseID();
    }

    public String getPersonID() {
        return personID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Person.Gender getGender() {
        return gender;
    }

    public String getFatherID() {
        return fatherID;
    }

    public String getMotherID() {
        return motherID;
    }

    public String getSpouseID() {
        return spouseID;
    }
}
