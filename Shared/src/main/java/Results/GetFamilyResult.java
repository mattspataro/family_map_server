package Results;

import Model.Person;
import java.util.ArrayList;

/**
 * Represents JSON data sent from the GetFamilyService back to the server
 */
public class GetFamilyResult extends Result{
    private ArrayList<Person> data = new ArrayList<Person>();

    /**
     * Constructs a new GetFamilyResult object with the given list of persons
     * @param data list of persons
     */
    public GetFamilyResult(ArrayList<Person> data){
        this.data = data;
        this.setSuccess(true);
    }

    public ArrayList<Person> getData() {
        return data;
    }
}
