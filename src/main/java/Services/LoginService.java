package Services;

import DataAccess.AuthTokenDAO;
import DataAccess.DataAccessException;
import DataAccess.UserDAO;
import Model.User;
import Requests.LoginRequest;
import Results.ErrorResult;
import Results.LoginResult;
import Results.Result;

/**
 * Services login requests
 */
public class LoginService extends Service{
    /**
     * Logs in the user and returns an auth token.
     * Errors: Request property missing or has invalid value, Internal server error
     * @param request a LoginRequest object
     * @return if success: a LoginResult object, if failure: a ErrorResult Object
     */
    public Result login(LoginRequest request){
        try{
            UserDAO uDao = new UserDAO(this.getConnection());

            if(!isValidRequest(request)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            User loginUser = uDao.login(request.getUserName(),request.getPassword());

            if(loginUser != null){
                //insert the auth token
                String authToken;
                AuthTokenDAO tDao = new AuthTokenDAO(this.getConnection());
                authToken = tDao.generateAndInsertFor(request.getUserName());

                return new LoginResult(authToken,loginUser.getUsername(),loginUser.getPersonID());
            }else{
                return new ErrorResult("error: invalid username or password");
            }

        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        }finally{
            this.closeConnection();
        }
    }
    private boolean isValidRequest(LoginRequest request){
        return  request.getUserName() != null &&
                request.getPassword() != null;
    }
}
