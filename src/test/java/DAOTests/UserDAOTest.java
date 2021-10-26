package DAOTests;

import DataAccess.*;

import Model.Person;
import Model.User;
import org.junit.jupiter.api.*;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class UserDAOTest {
    private Database db;
    private User sampleUser;
    private User sampleUser2;
    private User sampleUser3;
    private Connection conn;
    private UserDAO uDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        sampleUser = new User("bibleBasher33", "english", "willdale@gmail.com",
                "William", "Tyndale", Person.Gender.valueOf("m"), "101011");
        sampleUser2 = new User("martinLuther", "95thesis", "a1@gmail.com",
                "Martin", "Luther", Person.Gender.valueOf("m"), "000");
        sampleUser3 = new User("cliffNotes", "password", "a2@gmail.com",
                "John", "Wycliff", Person.Gender.valueOf("m"), "010");
        conn = db.openConnection();
        db.clearTables();
        uDao = new UserDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    //insert test cases -------------------------------------------------------------------------------

    @Test
    public void insertPass() throws DataAccessException {
        //same User object after it's inserted?
        try {
            uDao.insert(sampleUser); //put something in
            User compareTest = uDao.login(sampleUser.getUsername(), sampleUser.getPassword()); //take it back out
            assertNotNull(compareTest); //is it not null?
            assertEquals(sampleUser, compareTest); //is it the same as when we put it in?
        }catch(DataAccessException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void insertFail() throws DataAccessException {
        //doesn't let you enter the same User twice?
        uDao.insert(sampleUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(sampleUser)); //try to put the same thing in twice
    }

    //login test cases ------------------------------------------------------------------------------------

    @Test
    public void loginPass() throws DataAccessException {

        //gets the User you just inserted
        uDao.insert(sampleUser);
        User compareTest = uDao.login(sampleUser.getUsername(), sampleUser.getPassword());
        assertNotNull(compareTest);
        assertEquals(sampleUser, compareTest);
    }
    @Test
    public void loginFail() throws DataAccessException {

        //try to get something when there's nothing in the table
        User returnedUser = uDao.login(sampleUser.getUsername(), sampleUser.getPassword());
        assertNull(returnedUser);

        uDao.insert(sampleUser);

        //confirm that you can get it with the correct username and password
        returnedUser = uDao.login(sampleUser.getUsername(), sampleUser.getPassword());
        assertNotNull(returnedUser);

        //correct username, incorrect password
        returnedUser = uDao.login(sampleUser.getUsername(),"fakepassword");
        assertNull(returnedUser);

        //correct password, incorrect username
        returnedUser = uDao.login("fakeUsername", sampleUser.getPassword());
        assertNull(returnedUser);
    }
    //get test cases ------------------------------------------------------------------------------------

    @Test
    public void getPass() throws DataAccessException {
        //gets the User you just inserted
        uDao.insert(sampleUser);
        User compareTest = uDao.get(sampleUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(sampleUser, compareTest);
    }

    @Test
    public void getFail() throws DataAccessException {
        //try to get something when there's nothing in the table
        User returnedUser = uDao.get(sampleUser.getUsername());
        assertNull(returnedUser);
    }

    //clear test cases ------------------------------------------------------------------------------------
    @Test
    public void clearPass1() throws DataAccessException {
        uDao.insert(sampleUser);
        uDao.insert(sampleUser2);
        uDao.insert(sampleUser3);
        uDao.deleteAll();

        assertNull(uDao.login(sampleUser.getUsername(), sampleUser.getPassword()));
        assertNull(uDao.login(sampleUser2.getUsername(), sampleUser2.getPassword()));
        assertNull(uDao.login(sampleUser3.getUsername(), sampleUser3.getPassword()));
    }

    @Test
    public void clearPass2() throws DataAccessException {
        //delete all when there's nothing in the table
        uDao.deleteAll();
        assertNull(uDao.login(sampleUser.getUsername(), sampleUser.getPassword()));
    }

    //delete test cases ------------------------------------------------------------------------------------
    @Test
    public void deletePass() throws DataAccessException {
        //enter something
        uDao.insert(sampleUser);

        //delete it
        uDao.delete(sampleUser.getUsername());

        //assertException when you try and get it
        assertNull(uDao.login(sampleUser.getUsername(),sampleUser.getPassword()));
    }

    @Test
    public void deleteFail() throws DataAccessException {
        //try to delete something that doesn't exist
        assertEquals(0, uDao.delete(sampleUser.getUsername()));
    }

}
