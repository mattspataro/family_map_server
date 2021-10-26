package Requests;

import Model.Event;
import Model.Person;
import Model.User;
import java.util.ArrayList;

/**
 * Represents JSON data sent from the server to the LoadService
 */
public class LoadRequest extends Request{

    private final ArrayList<User> users;
    private final ArrayList<Person> persons;
    private final ArrayList<Event> events;

    /**
     * Constructs a new LoadRequest object with the data to be loaded into the database
     * @param users list of users
     * @param persons list of persons
     * @param events list of events
     */
    public LoadRequest(ArrayList<User> users, ArrayList<Person> persons, ArrayList<Event> events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public ArrayList<User> getUsers() {
        return users;
    }
    public ArrayList<Person> getPersons() {
        return persons;
    }
    public ArrayList<Event> getEvents() {
        return events;
    }
}
