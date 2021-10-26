package Results;

/**
 * Represents JSON error data sent from a service back to the server
 */
public class ErrorResult extends Result{
    /**
     * Constructs a new ErrorResult object with the given message
     * @param message string message
     */
    public ErrorResult(String message){
        this.setMessage(message);
        this.setSuccess(false);
    }
}
