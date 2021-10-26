package ServiceTests;

import DataAccess.*;
import Model.Person;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.ErrorResult;
import Results.LoginResult;
import Results.RegisterResult;
import Results.Result;
import Services.LoginService;
import Services.RegisterService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class LoginServiceTest {
    private LoginService ls;
    private LoginRequest lr;
    private RegisterRequest sampleRequest;
    private String username;
    private String password;
    private Database db;
    private String authToken;
    private String authToken2;

    @BeforeEach
    public void setUp() {
        ls = new LoginService();
        sampleRequest = new RegisterRequest("mattspataro98","password","fakeEmail@yahoo.com","Matt","Spataro", Person.Gender.m);
        username = sampleRequest.getUsername();
        password = sampleRequest.getPassword();

        //register a user using a request object
        RegisterService rs = new RegisterService();
        Result r = rs.register((RegisterRequest) sampleRequest);

        //check if it returned a success result
        if(r instanceof RegisterResult){
            RegisterResult rr = (RegisterResult)r;
            authToken = rr.getAuthToken();
        }
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db = new Database();
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

            if(authToken2 != null){
                tDAO.delete(authToken2);
            }

        }catch(DataAccessException ex){
            ex.printStackTrace();
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass() throws IOException, DataAccessException {
        //login a user using a request object
        lr = new LoginRequest(username,password);
        Result r = ls.login(lr);

        Assertions.assertTrue(r instanceof LoginResult);
        LoginResult loginResult = (LoginResult)r;
        authToken2 = loginResult.getAuthToken();
        Assertions.assertEquals(sampleRequest.getUsername(), loginResult.getUserName());
    }

    @Test
    public void fail(){
        //try to login with invalid info
        lr = new LoginRequest(username,"FAKE PASSWORD");
        Result r = ls.login(lr);
        Assertions.assertTrue(r instanceof ErrorResult);
    }
}
