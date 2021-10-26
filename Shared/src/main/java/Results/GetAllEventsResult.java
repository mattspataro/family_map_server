package Results;

import Model.Event;
import java.util.ArrayList;

/**
 * Represents JSON data sent from the GetAllEventsService back to the server
 */
public class GetAllEventsResult extends Result{

    private ArrayList<Event> data = new ArrayList<Event>();

    /**
     * Constructs a new GetAllEventsResult object with the given list of events
     * @param data list of events
     */
    public GetAllEventsResult(ArrayList<Event> data){
        this.data = data;
        this.setSuccess(true);
    }

    public ArrayList<Event> getData() {
        return data;
    }
}
