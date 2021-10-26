package Results;

/**
 * Represents JSON data sent from the LoadService back to the server
 */
public class LoadResult extends Result{
    /**
     * Constructs a new LoadResult object with the given number of users, persons, and events that were loaded into the database
     * @param users number of users
     * @param persons number of persons
     * @param events number of events
     */
    public LoadResult(int users, int persons, int events) {
        this.setMessage("Successfully added " + users + " users, " + persons + " persons, and " + events + " events to the database");
        this.setSuccess(true);
    }
}
