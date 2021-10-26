package Results;

/**
 * Represents JSON data sent from ClearService back to the server
 */
public class ClearResult extends Result{
    /**
     * Constructs a new ClearResult object
     */
    public ClearResult() {
        this.setMessage("Clear succeeded");
        this.setSuccess(true);
    }
}
