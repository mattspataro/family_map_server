package Results;

/**
 * Represents JSON data sent from the FillService back to the server
 */
public class FillResult extends Result{
    /**
     * Constructs a new FillResult object with the given number of person and events filled
     * @param persons number of persons
     * @param events number of persons
     */
    public FillResult(int persons, int events) {
        this.setMessage("Successfully added " + persons + " persons and " + events + " events to the database");
        this.setSuccess(true);
    }
}
