package Services;

import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import Model.AuthToken;
import Model.Event;
import Results.ErrorResult;
import Results.GetAllEventsResult;
import Results.Result;

import java.util.ArrayList;

/**
 * Services all events requests
 */
public class GetAllEventsService extends Service {
    /** Gets all the events from all people related to the user
     * Errors: Invalid auth token, Internal server error
     * Success Response Body: The response body returns a JSON object with a “data” attribute that contains an array of Event objects.
     * @return if success: a ClearResult object, if failure: a ErrorResult Object
     */
    public Result getAllEvents(String authTokenID){
        try{
            AuthToken authToken = this.getAuthToken(authTokenID);
            EventDAO eDao = new EventDAO(this.getConnection());
            ArrayList<Event> familyHistory = eDao.getAll(authToken);
            return new GetAllEventsResult(familyHistory);
        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        }catch(InvalidAuthTokenException ex){
            return new ErrorResult("error: invalid auth token");
        }finally{
            this.closeConnection();
        }
    }
}
