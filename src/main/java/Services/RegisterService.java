package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Model.User;
import Requests.RegisterRequest;
import Results.ErrorResult;
import Results.RegisterResult;
import Results.Result;

import java.io.IOException;

/**
 * Services register requests.
 */
public class RegisterService extends Service {
    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     * @param request a RegisterRequest object
     * @return if success: a RegisterResult object, if failure: a ErrorResult Object
     */

    public Result register(RegisterRequest request){
        try{
            //set-up the connection
            UserDAO uDao = new UserDAO(this.getConnection());

            if(!isValidRequest(request)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            //try to register the new user with the server
            User newUser = new User(request.getUsername(),
                                    request.getPassword(),
                                    request.getEmail(),
                                    request.getFirstName(),
                                    request.getLastName(),
                                    request.getGender(),
                                    request.getPersonID());
            newUser.setPersonID("");
            uDao.insert(newUser);

            this.closeConnection();

            //fill 4 generations of user data
            FillService fillService = new FillService();
            fillService.fill(newUser.getUsername());

            //generate an AuthToken object for the user (sign them in)
            String authToken;
            AuthTokenDAO tDao = new AuthTokenDAO(this.getConnection());
            authToken = tDao.generateAndInsertFor(newUser.getUsername());

            //update the new user object with the object in the database with a personID
            uDao = new UserDAO(this.getConnection());
            newUser = uDao.get(newUser.getUsername());

            //return the result
            return new RegisterResult(authToken,newUser.getUsername(),newUser.getPersonID());

        }catch(DataAccessException ex) {
            this.setCommit(false);
            if (ex.getMessage().contains("UNIQUE constraint failed")) {
                return new ErrorResult("error: username already taken by another user");
            } else {
                return new ErrorResult("error: internal server error");
            }
        }catch(IOException ex){
            this.setCommit(false);
            return new ErrorResult("error: unable to load data for generating random family for the user");
        }finally{
            this.closeConnection();
        }

    }

    private boolean isValidRequest(RegisterRequest request){
        return  request.getUsername() != null &&
                request.getPassword() != null &&
                request.getEmail()    != null &&
                request.getFirstName()!= null &&
                request.getLastName() != null &&
                request.getGender()   != null;
    }
}
