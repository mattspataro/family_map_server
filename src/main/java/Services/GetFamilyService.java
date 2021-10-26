package Services;

import DataAccess.DataAccessException;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Results.ErrorResult;
import Results.GetFamilyResult;
import Results.Result;

import java.util.ArrayList;

/**
 * Services all people requests
 */
public class GetFamilyService extends Service{
    /**
     * Returns ALL family members of the current user. The current user is determined from the provided auth token.
     * Errors: Invalid auth token, Internal server error
     * Success Response Body: The response body returns a JSON object with a “data” attribute that contains an array of Person objects.
     * @return if success: a GetFamilyResult object, if failure: a ErrorResult Object
     */
    public Result getFamily(String authTokenID){
        try{
            AuthToken authToken = this.getAuthToken(authTokenID);
            PersonDAO pDao = new PersonDAO(this.getConnection());
            ArrayList<Person> requestedPeople = pDao.getAll(authToken);
            return new GetFamilyResult(requestedPeople);

        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        }catch(InvalidAuthTokenException ex){
            return new ErrorResult("error: invalid auth token");
        }finally{
            this.closeConnection();
        }
    }
}
