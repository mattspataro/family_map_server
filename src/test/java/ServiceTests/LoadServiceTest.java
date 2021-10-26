package ServiceTests;

import DataAccess.*;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.ErrorResult;
import Results.LoadResult;
import Results.Result;
import Services.LoadService;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;

public class LoadServiceTest {

    private LoadService ls;
    private LoadRequest lr;
    private Database db;
    private String username;

    @BeforeEach
    public void setUp() {
        lr = generateRequest();
        ls = new LoadService();
        username = lr.getUsers().get(0).getUsername();
    }

    public LoadRequest generateRequest(){

        ArrayList<User> userList = new ArrayList<User>();
        ArrayList<Person> personList = new ArrayList<Person>();
        ArrayList<Event> eventList = new ArrayList<Event>();

        User u = new User("same","u","u","u","u", Person.Gender.m,"p0");
        userList.add(u);

        for(int i = 0; i < 10; i++){

            Person p = new Person("p" + i,"same","p","p",Person.Gender.m);
            Event e = new Event("e"+ i,"same","p" + i,0.0f,0.0f,"e","e","e",0000);

            personList.add(p);
            eventList.add(e);
        }

        return new LoadRequest(userList,personList,eventList);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db = new Database();
        try{
            UserDAO uDao = new UserDAO(db.getConnection());
            PersonDAO pDAO = new PersonDAO(db.getConnection());
            EventDAO eDAO = new EventDAO(db.getConnection());

            uDao.delete(username);
            pDAO.deleteWithUsername(username);
            eDAO.deleteWithUsername(username);

        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass() throws IOException, DataAccessException {
        //load the given data
        Result result = ls.load(lr);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof LoadResult);
        Assertions.assertEquals( "Successfully added 1 users, 10 persons, and 10 events to the database",
                                          result.getMessage());
    }

    @Test
    public void fail(){
        //try to add data that has two of the same thing
        ArrayList<User> userList = new ArrayList<User>();
        User u = new User("same","u","u","u","u", Person.Gender.m,"p0");
        userList.add(u);
        userList.add(u);

        LoadRequest badRequest = new LoadRequest(userList, lr.getPersons(),lr.getEvents());

        Result result = ls.load(badRequest);

        Assertions.assertNotNull(result);
        Assertions.assertTrue(result instanceof ErrorResult);
    }
}
