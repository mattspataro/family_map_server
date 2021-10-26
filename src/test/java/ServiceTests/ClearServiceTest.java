package ServiceTests;

import DataAccess.*;
import Model.AuthToken;
import Model.Person;
import Model.User;
import Results.ClearResult;
import Results.Result;
import Services.ClearService;
import Services.FillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTest {

    private ClearService cs;
    private User sampleUser;
    private FillService fillService;
    private String username;
    private Database db;
    private String authTokenID;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        cs = new ClearService();
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

    @Test
    public void pass() throws IOException, DataAccessException {
        //clear all tables
        Result r = cs.clear();
        assertTrue(r instanceof ClearResult);

        try {
            //check for anything in the tables
            UserDAO uDAO = new UserDAO(db.getConnection());
            assertNull(uDAO.get(username));

            PersonDAO pDAO = new PersonDAO(db.getConnection());
            assertEquals(new ArrayList<>(),pDAO.getAll(new AuthToken(authTokenID,username)));

            EventDAO eDAO = new EventDAO(db.getConnection());
            assertEquals(new ArrayList<>(),eDAO.getAll(new AuthToken(authTokenID,username)));

            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            assertNull(tDAO.get(authTokenID));

        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass2(){
        //clear twice, so that you clear when nothing's in the tables
        Result r = cs.clear();
        assertTrue(r instanceof ClearResult);

        //check if there's an error when you clear with nothing in the tables
        r = cs.clear();
        assertTrue(r instanceof ClearResult);

    }
}
