package ServiceTests;

import DataAccess.*;
import Model.Person;
import Requests.RegisterRequest;
import Results.ErrorResult;
import Results.RegisterResult;
import Results.Result;
import Services.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegisterServiceTest {
    private Database db;
    private RegisterRequest sampleRequest;
    private String username;
    private String authToken;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        //create a user object
        db = new Database();
        sampleRequest = new RegisterRequest("mattspataro98","password","fakeEmail@yahoo.com","Matt","Spataro", Person.Gender.m);
        username = sampleRequest.getUsername();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        //remove the user and everything with the associatedUsername
        try{
            UserDAO uDao = new UserDAO(db.getConnection());
            PersonDAO pDAO = new PersonDAO(db.getConnection());
            EventDAO eDAO = new EventDAO(db.getConnection());
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());

            uDao.delete(username);
            pDAO.deleteWithUsername(username);
            eDAO.deleteWithUsername(username);
            tDAO.delete(authToken);

        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass() throws IOException, DataAccessException {
        //register a user using a request object
        RegisterService rs = new RegisterService();
        Result r = rs.register(sampleRequest);

        //check if it returned a success result
        Assertions.assertTrue(r instanceof RegisterResult);
        RegisterResult rr = (RegisterResult)r;
        authToken = rr.getAuthToken();
    }

    @Test
    public void fail(){
        //try to register the same user twice
        RegisterService rs = new RegisterService();
        Result r = rs.register(sampleRequest);

        //check if the first one returned a success result
        Assertions.assertTrue(r instanceof RegisterResult);
        RegisterResult rr = (RegisterResult)r;
        authToken = rr.getAuthToken();

        //now check if the second returns an error result
        r = rs.register(sampleRequest);
        Assertions.assertTrue(r instanceof ErrorResult);
        Assertions.assertTrue(r.getMessage().contains("error"));
    }
}
