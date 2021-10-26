package Services;

import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.ErrorResult;
import Results.LoadResult;
import Results.Result;

import java.util.ArrayList;

/**
 * Services load requests
 */
public class LoadService extends Service {
    /**
     * Clears all data from the database (just like the /clear API), and then loads the posted user, person, and event data into the database.
     * Errors: Invalid request data (missing values, invalid values, etc.), Internal server error
     * @return if success: a LoadResult object, if failure: a ErrorResult Object
     */

    public Result load(LoadRequest request){
        try{

            if(!isValidRequest(request)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            //clear all data from the database
            this.getDatabase().clearTables();
            return loadWithoutClear(request);

        }catch(DataAccessException ex){
            this.setCommit(false);
            ex.printStackTrace();
            return new ErrorResult("error: internal server error");
        }
    }
    public Result loadWithoutClear(LoadRequest request){
        try{
            //load all the new data
            int numUsers = loadUsers(request.getUsers());
            int numPersons = loadPersons(request.getPersons());
            int numEvents = loadEvents(request.getEvents());

            return new LoadResult(numUsers, numPersons,numEvents);
        }catch(DataAccessException ex){
            this.setCommit(false);
            ex.printStackTrace();
            return new ErrorResult("error: internal server error");
        }finally{
            this.closeConnection();
        }
    }

    private boolean isValidRequest(LoadRequest request) {
        return  request.getUsers()   != null &&
                request.getPersons() != null &&
                request.getEvents()  != null;
    }

    private int loadUsers(ArrayList<User> users)throws DataAccessException{

        UserDAO uDao = new UserDAO(this.getConnection());
        for(User user : users) {
            uDao.insert(user);
        }
        return users.size();

    }
    private int loadPersons(ArrayList<Person> persons) throws DataAccessException {

        PersonDAO pDao = new PersonDAO(this.getConnection());
        for (Person person : persons) {
            pDao.insert(person);
        }
        return persons.size();

    }
    private int loadEvents(ArrayList<Event> events) throws DataAccessException  {

        EventDAO eDao = new EventDAO(this.getConnection());
        for(Event event : events) {
            eDao.insert(event);
        }
        return events.size();
    }
}
