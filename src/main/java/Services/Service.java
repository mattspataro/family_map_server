package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.Database;
import Model.AuthToken;

import java.sql.Connection;

public abstract class Service {
    
    private final Database database = new Database();
    private boolean commit = true;

    public Database getDatabase(){
        return database;
    }
    public Connection getConnection() throws DataAccessException {
        return database.getConnection();
    }
    public AuthToken getAuthToken(String authTokenID) throws InvalidAuthTokenException{
        try{
            //get AuthToken
            AuthTokenDAO tDao = new AuthTokenDAO(database.getConnection());
            AuthToken authToken = tDao.get(authTokenID);

            //if authToken doesn't exist, throw an exception, otherwise return the authToken object
            if(authToken == null){
                throw new InvalidAuthTokenException();
            }else{
                return authToken;
            }
        } catch (DataAccessException ex){
            throw new InvalidAuthTokenException();
        }
    }
    public void setCommit(boolean commit){
        this.commit = commit;
    }
    public void closeConnection(){
        try{
            database.closeConnection(commit);
        }catch(DataAccessException ex){
            System.out.println("ERROR: UNABLE TO COMMIT CHANGES");
        }
    }

}
