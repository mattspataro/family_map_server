package DataAccess;

import Model.AuthToken;

import java.sql.*;

/**
 * An AuthToken "Data Access Object" or DAO for communicating between the database and the program
 */
public class AuthTokenDAO {
    private Connection conn;

    public AuthTokenDAO(Connection conn){
        this.conn = conn;
    }
    /**
     * Inserts the given authorization token into the database
     * @param associatedUsername the username to generate and enter an AuthToken for
     */
    public String generateAndInsertFor(String associatedUsername) throws DataAccessException {

        try {
            String sql = "INSERT INTO AuthTokens(AssociatedUsername) " +
                         "VALUES(?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, associatedUsername);

            if (statement.executeUpdate() == 1) {
                Statement keyStatement = conn.createStatement();
                ResultSet keyResultSet;
                keyResultSet = keyStatement.executeQuery("select last_insert_rowid()");
                keyResultSet.next();
                int authTokenID = keyResultSet.getInt(1);
                return Integer.toString(authTokenID);
            } else {
                throw new DataAccessException("Couldn't find auto-incremented key");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new DataAccessException("Error while trying to insert into the AuthTokens table");
        }
    }

    public AuthToken get(String ID) throws DataAccessException {
        //gets the AuthToken with the specified ID
        // (because that's the primary key and there can be multiple tokens with the same username)

        AuthToken token;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE ID = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ID);

            rs = stmt.executeQuery();
            if (rs.next()) {
                token = new AuthToken(rs.getString("ID"),rs.getString("AssociatedUsername"));
                return token;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding auth-token");
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

    /**
     * Deletes the specified authorization token from the database
     * @param ID a unique authTokenID to delete
     */
    public void delete(String ID) throws DataAccessException {
        try {
            String sql = "DELETE FROM AuthTokens " +
                         "WHERE ID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,ID);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while trying to delete an authToken");
        }
    }
}
