package Requests;

/**
 * Represents JSON data sent from the server to the LoginService
 */
public class LoginRequest extends Request{

    private String userName; //get
    private String password; //get

    /**
     * Constructs a new LoginRequest object with the given username and password
     * @param username string username
     * @param password string password
     */
    public LoginRequest(String username, String password) {
        this.userName = username;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }


}
