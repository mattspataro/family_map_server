package Model;

/**
 * An authorization token used to validate that a user is signed in.
 */
public class AuthToken {
    private final String ID;
    private final String associatedUsername;

    /**
     * Constructs a new AuthToken
     * @param ID a non-empty string containing the actual unique token
     * @param associatedUsername a non-empty string for the username of the associated user that's signed in
     */
    public AuthToken(String ID, String associatedUsername) {
        this.ID = ID;
        this.associatedUsername = associatedUsername;
    }


    /**
     * returns the object's identifying string
     * @return the actual authorization token string
     */
    @Override
    public String toString() {
        return ID;
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    @Override
    public boolean equals(Object o){
        if(o instanceof AuthToken){
            AuthToken oToken = (AuthToken)o;
            return oToken.toString().equals(ID) && oToken.getAssociatedUsername().equals(associatedUsername);
        }else{
            return false;
        }
    }
}
