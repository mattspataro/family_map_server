package DAOTests;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class AuthTokenDAOTest {

    private Database db;
    private String sampleUsername;
    private Connection conn;
    private AuthTokenDAO tDao;

    @BeforeEach
    public void setUp() throws DataAccessException {
        db = new Database();
        //add tokens
        sampleUsername = "pemdas13";
        conn = db.openConnection();
        db.clearTables();
        tDao = new AuthTokenDAO(conn);
    }

    @AfterEach
    public void tearDown() throws DataAccessException {
        db.closeConnection(false);
    }

    //insert test cases -------------------------------------------------------------------------------
    @Test
    public void insertPass() throws DataAccessException {
        //add and check if it's the same when you take it out
        String authID = tDao.generateAndInsertFor(sampleUsername);
        assertEquals(authID, tDao.get(authID).toString());
    }
    @Test
    public void insertPass2() throws DataAccessException {
        //doesn't throw an exception when you try to add for the same username twice
        tDao.generateAndInsertFor(sampleUsername);
        Assertions.assertDoesNotThrow(() -> tDao.generateAndInsertFor(sampleUsername));
    }
    //get test cases -------------------------------------------------------------------------------
    @Test
    public void getPass() throws DataAccessException {
        //add and check if it's the same when you take it out
        String authID = tDao.generateAndInsertFor(sampleUsername);
        assertEquals(authID, tDao.get(authID).toString());
    }
    @Test
    public void getFail() throws DataAccessException {
        assertNull(tDao.get("INCORRECT AUTH_TOKEN_ID!!"));
    }
    //delete test cases -------------------------------------------------------------------------------
    @Test
    public void deletePass() throws DataAccessException {
        String authID = tDao.generateAndInsertFor(sampleUsername);
        tDao.delete(authID);
        assertNull(tDao.get(authID));
    }
    @Test
    public void deleteFail() throws DataAccessException {
        //try to delete something that's not there
        tDao.delete("INCORRECT AUTH_TOKEN_ID!!");
        assertNull(tDao.get("INCORRECT AUTH_TOKEN_ID!!"));
    }
}
