package Services;

import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import Model.AuthToken;
import Model.Event;
import Results.ErrorResult;
import Results.GetEventResult;
import Results.Result;

/**
 * Services single event requests
 */
public class GetEventService extends Service{
    /**
     * Returns the single Event object with the specified ID.
     * Errors: Invalid auth token, Invalid eventID parameter, Requested event does not belong to this user, Internal server error
     * @param eventID the ID for the event to get
     * @return if success: a GetEventResult object, if failure: a ErrorResult Object
     */
    public Result getEvent(String eventID, String authTokenID){
        try{

            if(!isValidRequest(eventID)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            AuthToken authToken = getAuthToken(authTokenID);
            EventDAO eDao = new EventDAO(this.getConnection());
            Event requestedEvent = eDao.get(eventID,authToken.getAssociatedUsername());

            if(requestedEvent == null){
                return new ErrorResult("error: requested event does not belong to this user");
            }else{
                return new GetEventResult(requestedEvent);
            }
        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        }catch(InvalidAuthTokenException ex){
            return new ErrorResult("error: invalid auth token");
        }finally{
            this.closeConnection();
        }
    }

    private boolean isValidRequest(String eventID) {
        return eventID != null;
    }
}
