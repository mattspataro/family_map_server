package DataAccess;

import Model.AuthToken;
import Model.Event;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDAO {
    private final Connection conn;

    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    public void insert(Event event) throws DataAccessException {
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, AssociatedUsername, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getUsername());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public Event get(String eventID, String username) throws DataAccessException {
        Event event;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ? AND AssociatedUsername = ?;";
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, eventID);
            stmt.setString(2,username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                event = new Event(rs.getString("EventID"),
                                  rs.getString("AssociatedUsername"),
                                  rs.getString("PersonID"),
                                  rs.getFloat("Latitude"),
                                  rs.getFloat("Longitude"),
                                  rs.getString("Country"),
                                  rs.getString("City"),
                                  rs.getString("EventType"),
                                  rs.getInt("Year"));
                return event;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
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
     * Returns ALL events of all family members of the current user. The current user is determined from the provided auth token
     * @param authToken an authToken that designates what user to get the events from
     * @return ALL family events of the current user
     */
    public ArrayList<Event> getAll(AuthToken authToken) throws DataAccessException {

        ArrayList<Event> familyEvents = new ArrayList<Event>();
        ResultSet resultSet = null;
        String sql = "SELECT *" +
                     "FROM Events " +
                     "WHERE Events.AssociatedUsername = ?;";
        try {

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken.getAssociatedUsername());

            resultSet = stmt.executeQuery();
            while(resultSet.next()) {
                Event newEvent = new Event(resultSet.getString("EventID"),
                        resultSet.getString("AssociatedUsername"),
                        resultSet.getString("PersonID"),
                        resultSet.getFloat("Latitude"),
                        resultSet.getFloat("Longitude"),
                        resultSet.getString("Country"),
                        resultSet.getString("City"),
                        resultSet.getString("EventType"),
                        resultSet.getInt("Year"));
                familyEvents.add(newEvent);
            }
            return familyEvents;
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
            String sql = "DELETE FROM Events WHERE AssociatedUsername = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while deleting from Event table");
        }
    }
}
