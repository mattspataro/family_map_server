package DataAccess;

import Model.Person;
import Model.User;

import java.sql.*;

/**
 * A User "Data Access Object" or DAO for communicating between the database and the program
 */
public class UserDAO {

    private final Connection conn;

    /**
     * Constructs a new UserDAO object with the given connection
     * @param conn a connection
     */
    public UserDAO(Connection conn){
        this.conn = conn;
    }

    /**
     * Inserts the given user into the database
     * @param user a User
     */
    public void insert(User user) throws DataAccessException {
        String sql = "INSERT INTO Users(Username, Password, Email, FirstName, LastName, Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender().toString());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DataAccessException(e.getMessage());
        }
    }

    /**
     * Deletes the entire User table in the database
     */
    public void deleteAll() throws DataAccessException {
        try {
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM Users";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing User table");
        }
    }

    /**
     * Deletes the user with the given username
     * @param username a string
     * @return the number of rows that were deleted
     */
    public int delete(String username) throws DataAccessException {
        try {
            String sql = "DELETE FROM Users WHERE Username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("SQL Error encountered while deleting from User table");
        }
    }

    /**
     * Returns the user with the given username and password
     * @param username a string for username
     * @param password a string for password
     * @return the user with the given username and password
     */
    public User login(String username, String password) throws DataAccessException {

        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        Person.Gender.valueOf(rs.getString("Gender")), rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public User get(String username) throws DataAccessException {

        User user;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        Person.Gender.valueOf(rs.getString("Gender")), rs.getString("PersonID"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }
}
