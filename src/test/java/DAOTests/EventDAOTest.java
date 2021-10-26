package DAOTests;

import DataAccess.*;
import Model.AuthToken;
import Model.Event;
import Model.Person;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EventDAOTest {
    private Database db;
    private Event bestEvent;
    private Event anotherEvent;
    private Event yetAnotherEvent;
    private Connection conn;
    private EventDAO eDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        bestEvent = new Event("Biking_123A", "pemdas13", "00100",
                10.3f, 10.3f, "Japan", "Ushiku",
                "Biking_Around", 2016);
        anotherEvent = new Event("13890", "different", "00110",
                44.4f, 42.8f, "USA", "Seattle",
                "backpacking", 2000);
        yetAnotherEvent = new Event("23823", "pemdas13", "00101",
                0.0f, 0.0f, "England", "London",
                "sky diving", 1995);
        conn = db.openConnection();
        db.clearTables();
        eDao = new EventDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    @Test
    public void insertPass() throws DataAccessException {
        eDao.insert(bestEvent);
        Event compareTest = eDao.get(bestEvent.getEventID(), bestEvent.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestEvent, compareTest);
    }

    @Test
    public void insertFail() throws DataAccessException {
        eDao.insert(bestEvent);
        Assertions.assertThrows(DataAccessException.class, ()-> eDao.insert(bestEvent));
    }

    //get tests ---------------------------------------------------------------------------------------------
    @Test
    public void getPass() throws DataAccessException {
        eDao.insert(bestEvent);
        assertNotNull(bestEvent.getEventID());
        assertEquals(bestEvent, eDao.get(bestEvent.getEventID(),bestEvent.getUsername()));

    }
    @Test
    public void getFail() throws DataAccessException {
        assertNull(eDao.get(bestEvent.getEventID(),bestEvent.getUsername()));
    }

    //getAll tests ---------------------------------------------------------------------------------------------
    @Test
    public void getAllPass() throws DataAccessException {

        //generate authToken that connects to people
        AuthToken token = new AuthToken("001","pemdas13");
        //insert events
        eDao.insert(bestEvent);
        eDao.insert(anotherEvent); // different username so not included!
        eDao.insert(yetAnotherEvent);

        assertEquals(new ArrayList<Event>(List.of(bestEvent, yetAnotherEvent)),eDao.getAll(token));
    }
    @Test
    public void getAllFail() throws DataAccessException {
        //get when there's nothing in the table
        assertEquals(new ArrayList<Person>(), eDao.getAll(new AuthToken("001","fakeUsername")));
    }

    //delete test cases ------------------------------------------------------------------------------------
    @Test
    public void deletePass() throws DataAccessException {
        //enter something
        eDao.insert(bestEvent);

        //delete it
        eDao.deleteWithUsername(bestEvent.getUsername());

        //assert null when you try and get it
        assertNull(eDao.get(bestEvent.getPersonID(),bestEvent.getUsername()));
    }
    @Test
    public void deleteFail() throws DataAccessException {
        assertDoesNotThrow(() -> eDao.deleteWithUsername(bestEvent.getUsername()));
    }
}
