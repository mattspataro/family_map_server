package ServiceTests;

import DataAccess.*;
import Model.Event;
import Results.ErrorResult;
import Results.GetEventResult;
import Results.Result;
import Services.GetEventService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GetEventServiceTest {

    private GetEventService ges;
    private Event sampleEvent;
    private Database db;
    private String username;
    private String eventID;
    private String authTokenID;

    @BeforeEach
    public void setUp() throws DataAccessException, IOException {
        ges = new GetEventService();
        sampleEvent = new Event("christams2020",
                                "hollyJolly",
                                "santaclaus",
                                0.0f,
                                0.0f,
                                "up yonder",
                                "north pole",
                                "christmas",
                                2020);
        username = sampleEvent.getUsername();
        eventID = sampleEvent.getEventID();
        authTokenID = "";
        db = new Database();
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        try{
            EventDAO eDAO = new EventDAO(db.getConnection());
            eDAO.deleteWithUsername(username);

            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            tDAO.delete(authTokenID);
        }finally{
            db.closeConnection(true);
        }
    }

    @Test
    public void pass() throws IOException, DataAccessException {
        //get the requested event
        try{
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            authTokenID = tDAO.generateAndInsertFor(username);

            EventDAO eDAO = new EventDAO(db.getConnection());
            eDAO.insert(sampleEvent);
            db.closeConnection(true);

            Result r = ges.getEvent(eventID,authTokenID);
            System.out.println();
            assertTrue(r instanceof GetEventResult);
            GetEventResult ger = (GetEventResult)r;
            assertEquals(sampleEvent.getYear(),ger.getYear());
            assertEquals(sampleEvent.getEventID(),ger.getEventID());
            assertEquals(sampleEvent.getEventType(),ger.getEventType());
            assertEquals(sampleEvent.getPersonID(),ger.getPersonID());
            assertEquals(sampleEvent.getCity(),ger.getCity());
            assertEquals(sampleEvent.getCountry(),ger.getCountry());
            assertEquals(sampleEvent.getLatitude(),ger.getLatitude());
            assertEquals(sampleEvent.getLongitude(),ger.getLongitude());
            assertEquals(sampleEvent.getUsername(),ger.getAssociatedUsername());

        }catch(DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
        }
    }

    @Test
    public void fail() throws DataAccessException {
        //requested event does not belong to this user
        try{
            AuthTokenDAO tDAO = new AuthTokenDAO(db.getConnection());
            authTokenID = tDAO.generateAndInsertFor(username);

            EventDAO eDAO = new EventDAO(db.getConnection());
            eDAO.insert(sampleEvent);
            db.closeConnection(true);

            Result r = ges.getEvent(eventID,"FAKE AUTH_TOKEN_ID!!!");

            assertTrue(r instanceof ErrorResult);
            assertTrue(r.getMessage().contains("error"));

        }catch(DataAccessException ex){
            ex.printStackTrace();
            db.closeConnection(false);
        }
    }
}
