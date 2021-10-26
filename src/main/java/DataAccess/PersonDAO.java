package DataAccess;

import Model.AuthToken;
import Model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * A Person "Data Access Object" or DAO for communicating between the database and the program
 */
public class PersonDAO {

    private Connection conn;

    /**
     * Constructs a new PersonDAO with the given connection
     * @param conn
     */
    public PersonDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Inserts the given person into the database
     * @param person a Person
     */
    public void insert(Person person) throws DataAccessException {

        String sql = "INSERT INTO Persons (PersonID, AssociatedUsername, FirstName, LastName, Gender, FatherID, MotherID, SpouseID) VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender().toString());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the Person table");
        }
    }

    /**
     * Deletes the entire Person table in the database
     */
    public void deleteAll() throws DataAccessException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing the Person table");
        }
    }

    /**
     * Returns the single Person object with the specified ID
     * @param personID a string
     * @return the person with the given ID
     */
    public Person get(String personID, String username) throws DataAccessException {

        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ? AND AssociatedUsername = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, personID);
            stmt.setString(2, username);

            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("PersonID"),
                                    rs.getString("AssociatedUsername"),
                                    rs.getString("FirstName"),
                                    rs.getString("LastName"),
                                    Person.Gender.valueOf(rs.getString("Gender")),
                                    rs.getString("FatherID"),
                                    rs.getString("MotherID"),
                                    rs.getString("SpouseID"));
                return person;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token
     * @param authToken an authToken that designates what user to get the family from
     * @return ALL family members of the current user
     */
    public ArrayList<Person> getAll(AuthToken authToken) throws DataAccessException {

        ArrayList<Person> family = new ArrayList<Person>();
        ResultSet resultSet = null;
        String sql = "SELECT *" +
                     "FROM Persons " +
                     "WHERE Persons.AssociatedUsername = ?";
        try {

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken.getAssociatedUsername());

            resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                Person person = new Person(resultSet.getString("PersonID"),
                                resultSet.getString("AssociatedUsername"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("LastName"),
                                Person.Gender.valueOf(resultSet.getString("Gender")),
                                resultSet.getString("FatherID"),
                                resultSet.getString("MotherID"),
                                resultSet.getString("SpouseID"));
                family.add(person);
            }
            return family;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        } finally {
            if(resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public int deleteWithUsername(String username) throws DataAccessException {
        try {
            String sql = "DELETE FROM Persons WHERE AssociatedUsername = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while deleting from Persons table");
        }
    }

}
