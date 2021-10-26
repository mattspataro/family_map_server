package Results;

/**
 * Represents JSON data sent from the LoginService back to the server
 */
public class LoginResult extends Result{
    private String authToken;
    private String userName;
    private String personID;

    /**
     * Constructs a new LoginResult object with the given authToken, username, and personID
     * @param authToken string authToken
     * @param userName string username
     * @param personID string person ID
     */
    public LoginResult(String authToken, String userName, String personID) {
        this.authToken = authToken;
        this.userName = userName;
        this.personID = personID;
        this.setSuccess(true);
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

}
