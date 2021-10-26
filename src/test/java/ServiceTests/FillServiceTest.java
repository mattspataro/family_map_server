package ServiceTests;
import DataAccess.*;
import Model.Person;
import Model.User;
import Services.FillService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class FillServiceTest {
    private FillService fillService;
    private User sampleUser;
    private String username;
    private Database db;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {

        sampleUser = new User("mattspataro98","password","fakeEmail@yahoo.com","Matt","Spataro", Person.Gender.m,"TBD");
        fillService = new FillService();
        username = "mattspataro98";
        db = new Database();

        try{
            UserDAO uDao = new UserDAO(db.getConnection());
            uDao.insert(sampleUser);
        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
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
    public void fillPass() throws IOException, DataAccessException {
        //add i generations
        //check to see if the correct number of people and events were added

        for(int i = 0; i < 8; i++){
            String msg = "Successfully added " + numPeople(i) + " persons and " + numEvents(i) + " events to the database";
            assertEquals(msg, fillService.fill(username,i).getMessage());
        }

    }

    private int numPeople(int gen){
        int num = 0;
        for(int i = 0; i <= gen; i++){
            num += Math.pow(2,i);
        }
        return num;
    }
    private int numEvents(int gen){
        return numPeople(gen)*3 - 2;
    }

    @Test
    public void fillFail(){
        //try to fill for a user that hasn't been registered
        assertTrue(fillService.fill("fakeUsername",4).getMessage().contains("error"));
    }
}
