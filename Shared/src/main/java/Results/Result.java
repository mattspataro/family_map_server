package Results;

/**
 * An abstract class from which all result objects inherit from
 */
public abstract class Result {
    /**
     * Notifies whether the request was a success
     */
    private boolean success;
    /**
     * Summarizes the response
     */
    private String message;

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }
}
