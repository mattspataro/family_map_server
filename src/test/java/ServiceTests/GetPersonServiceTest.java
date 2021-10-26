package ServiceTests;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import DataAccess.PersonDAO;
import Model.Person;
import Results.ErrorResult;
import Results.GetPersonResult;
import Results.Result;
import Services.GetPersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GetPersonServiceTest {
    private GetPersonService gps;
    private Person samplePerson;
    private Database db;
    private String username;
    private String personID;
    private String authTokenID;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        gps = new GetPersonService();
        samplePerson = new Person("santaclaus0","hollyJolly","santa","claus",Person.Gender.m);
        username = samplePerson.getAssociatedUsername();
        personID = samplePerson.getPersonID();
        authTokenID = "";
        db = new Database();

    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        try{
            PersonDAO pDAO = new PersonDAO(db.getConnection());
            pDAO.deleteWithUsername(username);

            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            tDAO.delete(authTokenID);
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass() throws DataAccessException {
        try{
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            authTokenID = tDAO.generateAndInsertFor(username);

            PersonDAO pDAO = new PersonDAO(db.getConnection());
            pDAO.insert(samplePerson);
            db.closeConnection(true);

            Result r = gps.getPerson(personID,authTokenID);
            System.out.println();
            assertTrue(r instanceof GetPersonResult);
            GetPersonResult gpr = (GetPersonResult)r;
            assertEquals(samplePerson.getAssociatedUsername(), gpr.getAssociatedUsername());
            assertEquals(samplePerson.getPersonID(), gpr.getPersonID());
            assertEquals(samplePerson.getFirstName(), gpr.getFirstName());
            assertEquals(samplePerson.getLastName(), gpr.getLastName());
            assertEquals(samplePerson.getGender(), gpr.getGender());

        }catch(DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
        }
    }

    @Test
    public void fail() throws DataAccessException {
        //Requested person does not belong to this user
        try{
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            authTokenID = tDAO.generateAndInsertFor(username);

            PersonDAO pDAO = new PersonDAO(db.getConnection());
            pDAO.insert(samplePerson);
            db.closeConnection(true);

            Result r = gps.getPerson(personID,"FAKE AUTH_TOKEN_ID!!!");

            assertTrue(r instanceof ErrorResult);
            assertTrue(r.getMessage().contains("error"));

        }catch(DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
        }
    }
}
