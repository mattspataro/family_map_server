package Services;

import DataAccess.DataAccessException;
import DataAccess.PersonDAO;
import Model.AuthToken;
import Model.Person;
import Results.ErrorResult;
import Results.GetPersonResult;
import Results.Result;

/**
 * Services a single person request
 */
public class GetPersonService extends Service {
    /**
     * Returns the single Person object with the specified ID.
     * Errors: -Invalid auth token, Invalid personID parameter,
     * -Requested person does not belong to this user,
     * -Internal server error
     * @param personID the ID for the person to get
     * @return if success: a GetPersonResult object, if failure: a ErrorResult Object
     */
    public  Result getPerson(String personID, String authTokenID){
        try{

            if(!isValidRequest(personID)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            //check if the auth token is valid, if not throw an exception
            AuthToken authToken = this.getAuthToken(authTokenID);

            PersonDAO pDao = new PersonDAO(this.getConnection());
            Person requestedPerson = pDao.get(personID,authToken.getAssociatedUsername());
            if(requestedPerson != null){
                return new GetPersonResult(requestedPerson);
            }else{
                return new ErrorResult("error: requested person does not belong to this user");
            }
        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        }catch(InvalidAuthTokenException ex){
            return new ErrorResult("error: invalid auth token");
        }finally{
            this.closeConnection();
        }
    }

    private boolean isValidRequest(String personID) {
        return personID != null;
    }
}
