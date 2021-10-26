package DAOTests;

import DataAccess.*;

import Model.AuthToken;
import Model.Person;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//We will use this to test that our insert method is working and failing in the right ways
public class PersonDAOTest {
    private Database db;
    private Person samplePerson;
    private Person samplePerson2;
    private Person samplePerson3;
    private Connection conn;
    private PersonDAO pDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        samplePerson = new Person("001","bibleBasher33", "Chris P.","Bacon", Person.Gender.valueOf("m"));
        samplePerson2 = new Person("002","bibleBasher33", "Porky","Pig", Person.Gender.m);
        samplePerson3 = new Person("003","bibleBasher33", "Jimmy","Dean", Person.Gender.m);
        conn = db.openConnection();
        db.clearTables();
        pDao = new PersonDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    //insert test cases -------------------------------------------------------------------------------

    @Test
    public void insertPass() throws DataAccessException {
        //same Person object after it's inserted?
        try {
            pDao.insert(samplePerson); //put something in
            Person compareTest = pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername()); //take it back out
            assertNotNull(compareTest); //is it not null?
            assertEquals(samplePerson, compareTest); //is it the same as when we put it in?
        }catch(DataAccessException ex){
            ex.printStackTrace();
        }
    }

    @Test
    public void insertFail() throws DataAccessException {
        //doesn't let you enter the same Person twice?
        pDao.insert(samplePerson);
        assertThrows(DataAccessException.class, ()-> pDao.insert(samplePerson)); //try to put the same thing in twice
    }

    //get test cases ------------------------------------------------------------------------------------

    @Test
    public void getPass() throws DataAccessException {

        //gets the Person you just inserted?
        pDao.insert(samplePerson);
        Person compareTest = pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername());
        assertNotNull(compareTest);
        assertEquals(samplePerson, compareTest);
    }
    @Test
    public void getFail() throws DataAccessException {

        //try to get something when there's nothing in the table
        Person returnedUser = pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername());
        assertNull(returnedUser);

        pDao.insert(samplePerson);
    }

    //clear test cases ------------------------------------------------------------------------------------
    @Test
    public void clearPass1() throws DataAccessException {
        //add three things, delete all, and check to see if any survived
        pDao.insert(samplePerson);
        pDao.insert(samplePerson2);
        pDao.insert(samplePerson3);
        pDao.deleteAll();

        assertNull(pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername()));
        assertNull(pDao.get(samplePerson2.getPersonID(),samplePerson2.getAssociatedUsername()));
        assertNull(pDao.get(samplePerson3.getPersonID(), samplePerson3.getAssociatedUsername()));
    }

    @Test
    public void clearPass2() throws DataAccessException {
        //delete-all when there's nothing in the table
        pDao.deleteAll();
        assertNull(pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername()));

    }

    //getAll test cases ------------------------------------------------------------------------------------
    @Test
    public void getAllPass() throws DataAccessException {
        //insert an authToken associated with the username
        AuthTokenDAO aDao = new AuthTokenDAO(conn);
        AuthToken sampleToken = new AuthToken("001","bibleBasher33");
        aDao.generateAndInsertFor("test");

        //insert 3 people
        pDao.insert(samplePerson);
        pDao.insert(samplePerson2);
        pDao.insert(samplePerson3);

        assertEquals(new ArrayList<>(List.of(samplePerson,samplePerson2,samplePerson3)), pDao.getAll(sampleToken));
    }
    @Test
    public void getAllFail() throws DataAccessException {
        //get when there's nothing in the table
        assertEquals(new ArrayList<Person>(), pDao.getAll(new AuthToken("001","fakeUsername")));
    }

    //delete test cases ------------------------------------------------------------------------------------
    @Test
    public void deletePass() throws DataAccessException {
        //enter something
        pDao.insert(samplePerson);

        //delete it
        pDao.deleteWithUsername(samplePerson.getAssociatedUsername());

        //assert null when you try and get it
        assertNull(pDao.get(samplePerson.getPersonID(),samplePerson.getAssociatedUsername()));

    }
    @Test
    public void deleteFail() throws DataAccessException {
        //delete something that's not there
        assertDoesNotThrow(() -> pDao.deleteWithUsername(samplePerson.getAssociatedUsername()));
    }

}
