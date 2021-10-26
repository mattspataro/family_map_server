package ServiceTests;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.UserDAO;
import Model.Person;
import Model.User;
import Results.ErrorResult;
import Results.GetAllEventsResult;
import Results.Result;
import Services.ClearService;
import Services.FillService;
import Services.GetAllEventsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

//WARNING: THIS TEST CLEARS THE ENTIRE DATABASE AT THE END
public class GetAllEventsServiceTest {

    private GetAllEventsService gaes;
    private User sampleUser;
    private FillService fillService;
    private String username;
    private Database db;
    private String authTokenID;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        gaes = new GetAllEventsService();
        sampleUser = new User("mattspataro98","password","fakeEmail@yahoo.com","Matt","Spataro", Person.Gender.m,"TBD");
        fillService = new FillService();
        username = "mattspataro98";
        db = new Database();
        authTokenID = "";

        try{
            UserDAO uDao = new UserDAO(db.getConnection());
            uDao.insert(sampleUser);
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            authTokenID = tDAO.generateAndInsertFor(username);

        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
        fillService.fill(username,4);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //clear all tables
        ClearService cs = new ClearService();
        cs.clear();
    }

    @Test
    public void pass() throws IOException, DataAccessException {
        //get all the events
        Result r = gaes.getAllEvents(authTokenID);
        assertTrue(r instanceof GetAllEventsResult);
        GetAllEventsResult gr = (GetAllEventsResult) r;

        //there are 91 events generated when we do a fill of 4 generations
        //confirm that that's the case here
        assertEquals(91,gr.getData().size());
    }

    @Test
    public void fail(){
        //invalid auth token
        Result r = gaes.getAllEvents("FAKE AUTH_TOKEN_ID!!!");
        assertTrue(r instanceof ErrorResult);
        assertTrue(r.getMessage().contains("error"));
    }
}
