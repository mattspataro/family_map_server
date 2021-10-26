package Services;

import DataAccess.DataAccessException;
import Results.ClearResult;
import Results.ErrorResult;
import Results.Result;

/**
 * Services clear requests
 */
public class ClearService extends Service{
    /**
     * Deletes ALL data from the database, including user accounts, auth tokens, and generated person and event data.
     * Errors: Internal server error
     *@return if success: a ClearResult object, if failure: a ErrorResult Object
     */
    public Result clear(){
        try{
            this.getDatabase().clearTables();
            return new ClearResult();
        }catch(DataAccessException ex){
            this.setCommit(false);
            return new ErrorResult("error: Internal server error");
        }finally{
            this.closeConnection();
        }
    }
}
