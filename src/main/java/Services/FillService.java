package Services;

import Data.ListOfLocations;
import Data.ListOfNames;
import Data.Location;
import DataAccess.DataAccessException;
import DataAccess.EventDAO;
import DataAccess.PersonDAO;
import DataAccess.UserDAO;
import Model.Event;
import Model.Person;
import Model.User;
import Requests.LoadRequest;
import Results.ErrorResult;
import Results.FillResult;
import Results.Result;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;

import java.util.Objects;
import java.util.Random;

/**
 * Services fill requests
 */
public class FillService extends Service{

    private ArrayList<User> users;
    private ArrayList<Person> persons;
    private ArrayList<Event> events;
    private String associatedUsername;

    private final Gson gson = new Gson();
    private ArrayList<String> maleNames = new ArrayList<>();
    private ArrayList<String> femaleNames = new ArrayList<>();
    private ArrayList<String> lastNames = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();
    private final Random RANDOM = new Random();


    public FillService() throws IOException {
        loadData();
    }

    /**
     * Overloaded fill method that omits the optional "generations" parameter
     * @param userName string username
     * @return if success: a FillResult object, if failure: a ErrorResult Object
     */
    public Result fill(String userName){
        return this.fill(userName,4);
    }
    /**
     * Populates the server's database with generated data for the specified username.
     * The required "username" parameter must be a user already "registered with the server".
     * If there is any data in the database already associated with the given username, it is deleted.
     * The optional "generations" parameter lets the caller specify the number of generations of ancestors to be generated, and must be a non-negative integer.
     * (the default is 4, which results in 31 new persons each with associated events).
     * Errors: Invalid username or generations parameter, Internal server error
     * @param username string username
     * @param generations optional number of generations (method overloaded to omit this)
     * @return if success: a FillResult object, if failure: a ErrorResult Object
     */
    public Result fill(String username, int generations){

        try {

            if(!isValidRequest(username, generations)){
                return new ErrorResult("error: request property missing or has invalid value");
            }

            //reset data
            associatedUsername = username;
            users = new ArrayList<>();
            persons = new ArrayList<>();
            events = new ArrayList<>();

            //get user and modify personID
            User user = getUser();
            user.setPersonID(user.getFirstName() + user.getLastName() + associatedUsername + persons.size());
            users.add(user);

            //generate person
            Person userPerson = generatePerson(user);
            addBirthEvent(userPerson,null);

            //generate family
            Person mother = generateXGenerationsForY(generations, userPerson, Person.Gender.f);
            Person father = generateXGenerationsForY(generations, userPerson, Person.Gender.m);
            setXAsSpouseOfY(mother,father);

            //clear old data
            clearOldData();

            //load new data
            LoadService loadService = new LoadService();
            loadService.loadWithoutClear(new LoadRequest(users, persons, events));

            //return the result
            return new FillResult(persons.size(), events.size());

        }catch(DataAccessException ex){
            return new ErrorResult("error: internal server error");
        } catch(NoRegisteredUserException ex){
            return new ErrorResult("error: attempted to fill a user that has not been registered with the database");
        }catch(RuntimeException ex){
            ex.printStackTrace();
            return new ErrorResult("error: internal server runtime error");
        }
    }

    private boolean isValidRequest(String username, int generations) {
        return  username    != null &&
                generations > -1;
    }

    private Person generateXGenerationsForY(int generations, Person child, Person.Gender gender){

        //base case
        if(generations <= 0){
            return null;
        }

        //generate a parent
        Person parent = generateRandomPerson(child.getLastName(),gender);
        setXAsParentOfY(parent, child);

        //add parent events
        addBirthEvent(parent,child);
        addMarriageEvent(parent,child);
        addDeathEvent(parent,child);

        //generate the parent's parents
        Person mother = generateXGenerationsForY(generations-1, parent, Person.Gender.f);
        Person father = generateXGenerationsForY(generations-1, parent, Person.Gender.m);
        setXAsSpouseOfY(mother,father);

        return parent;
    }

    // EVENTS ----------------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private void addBirthEvent(Person parent, Person child){

        //BIRTH
        // Parents should be born a reasonable number of years before their children (at least 13 years),
        // Also, females should not give birth at over 50 years old.

        final int MIN_AGE = 13;
        final int MAX_AGE = 50;
        final int BASE_YEAR = 1995;

        String eventType = "birth";
        String eventID = parent.getPersonID() + eventType;
        Location eventLocation = locations.get(RANDOM.nextInt(locations.size()));
        int year;

        //if there is a child, use their birth to calculate yours
        //otherwise calculate from the base year
        if(child != null){
            //generate mother's births based on children
            //generate father's births based on wives
            if(parent.getGender() == Person.Gender.f){
                Event childBirth = getBirthEvent(child.getPersonID());
                assert childBirth != null;
                year = childBirth.getYear() - (MIN_AGE + RANDOM.nextInt(MAX_AGE-MIN_AGE+1));
            }else{
                int wifeBirth = Objects.requireNonNull(getBirthEvent(child.getMotherID())).getYear();
                year = wifeBirth - RANDOM.nextInt(5);
            }
        }else{
            year = BASE_YEAR + RANDOM.nextInt(5);
        }

        //create event
        Event birth = new Event(eventID,
                associatedUsername,
                parent.getPersonID(),
                eventLocation.getLatitude(),
                eventLocation.getLongitude(),
                eventLocation.getCountry(),
                eventLocation.getCity(),
                eventType,
                year);
        events.add(birth);
    }

    private void addMarriageEvent(Person parent, Person child){

        //MARRIAGE
        // get married at a reasonable age (13+)
        // Each person in a couple has their own marriage event,
        // but their two marriage events need to have the same year and location.
        // +18-30 from birth event (where possible)

        try {
            final int MIN_AGE = 13;
            String eventType = "marriage";
            String eventID = parent.getPersonID() + eventType;
            Event marriage;

            if (parent.getGender() == Person.Gender.m) {

                //mother's are added first, so we can use her marriage event for the father
                Event spouseMarriageEvent = getMarriageEvent(child.getMotherID());

                //create event
                assert spouseMarriageEvent != null;
                marriage = new Event(eventID,
                        associatedUsername,
                        parent.getPersonID(),
                        spouseMarriageEvent.getLatitude(),
                        spouseMarriageEvent.getLongitude(),
                        spouseMarriageEvent.getCountry(),
                        spouseMarriageEvent.getCity(),
                        eventType,
                        spouseMarriageEvent.getYear());
            } else {
                Location eventLocation = locations.get(RANDOM.nextInt(locations.size()));

                Event childBirth = getBirthEvent(child.getPersonID());
                Event myBirth = getBirthEvent(parent.getPersonID());
                assert myBirth != null;
                assert childBirth != null;
                int ageAtChildBirth = childBirth.getYear() - myBirth.getYear();
                int upperBound = Math.min(ageAtChildBirth - MIN_AGE, 30 - MIN_AGE);
                int year = myBirth.getYear() + (MIN_AGE + RANDOM.nextInt(upperBound + 1));

                //create event
                marriage = new Event(eventID,
                        associatedUsername,
                        parent.getPersonID(),
                        eventLocation.getLatitude(),
                        eventLocation.getLongitude(),
                        eventLocation.getCountry(),
                        eventLocation.getCity(),
                        eventType,
                        year);
            }
            events.add(marriage);
        }catch(RuntimeException ex){

            ex.printStackTrace();
        }
    }

    private void addDeathEvent(Person parent, Person child) {
        //DEATH
        // and not die before their child is born.
        // death events need to be last.
        // No one should die at over 120 years old.
        // +years to child's death-120

        final int MAX_AGE = 120;
        String eventType = "death";
        String eventID = parent.getPersonID() + eventType;
        Location eventLocation = locations.get(RANDOM.nextInt(locations.size()));
        Event childBirth = getBirthEvent(child.getPersonID());
        Event myBirth = getBirthEvent(parent.getPersonID());
        assert childBirth != null;
        assert myBirth != null;
        int ageAtChildBirth = childBirth.getYear() - myBirth.getYear();
        int year = myBirth.getYear() + (ageAtChildBirth + 1 + RANDOM.nextInt(MAX_AGE-ageAtChildBirth));

        Event death = new Event(eventID,
                associatedUsername,
                parent.getPersonID(),
                eventLocation.getLatitude(),
                eventLocation.getLongitude(),
                eventLocation.getCountry(),
                eventLocation.getCity(),
                eventType,
                year);
        events.add(death);
    }

    private Event getBirthEvent(String personID){
        for(Event event : events){
            if(event.getPersonID().equals(personID) && event.getEventType().equals("birth")){
                return event;
            }
        }
        return null;
    }
    private Event getMarriageEvent(String personID){
        for(Event event : events){
            if(event.getPersonID().equals(personID) && event.getEventType().equals("marriage")){
                return event;
            }
        }
        return null;
    }

    // PERSON HELP METHODS ---------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private Person generateRandomPerson(String surname, Person.Gender gender){
        ArrayList<String> firstNames;
        String lastName;

        //determine gender names
        if(gender == Person.Gender.f){
            firstNames = femaleNames;
            lastName = lastNames.get(RANDOM.nextInt(lastNames.size()));
        }else{
            firstNames = maleNames;
            lastName = surname;
        }

        //determine the first name and personID
        String firstName = firstNames.get(RANDOM.nextInt(firstNames.size()));
        String personID = firstName + lastName + associatedUsername + persons.size();

        //create a person, add it to the list, and return it
        Person person = new Person(personID,associatedUsername,firstName,lastName,gender);
        persons.add(person);
        return person;
    }

    private void setXAsParentOfY(Person self, Person child){
        if(self.getGender() == Person.Gender.f){
            child.setMotherID(self.getPersonID());
        }else{
            child.setFatherID(self.getPersonID());
        }
    }
    private void setXAsSpouseOfY(Person mother,Person father){
        if(mother != null && father != null){
            mother.setSpouseID(father.getPersonID());
            father.setSpouseID(mother.getPersonID());
        }
    }

    private User getUser() throws DataAccessException, NoRegisteredUserException {

        try{
            UserDAO uDao = new UserDAO(this.getConnection());
            User user = uDao.get(associatedUsername);

            if(user != null){
                return user;
            }else{
                this.setCommit(false);
                throw new NoRegisteredUserException();
            }
        }finally{
            this.closeConnection();
        }
    }
    private Person generatePerson(User user){
        Person person = new Person(user.getPersonID(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender());
        persons.add(person);
        return person;
    }

    // LOAD AND DELETE DATA --------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------------------

    private void loadData() throws IOException {

        //clear all data
        maleNames.clear();
        femaleNames.clear();
        lastNames.clear();
        locations.clear();

        //load all data
        loadLocations();
        loadFemaleNames();
        loadLastNames();
        loadMaleNames();
    }

    private void loadMaleNames() throws IOException {
        try(InputStream inputStream = FillService.class.getResourceAsStream("/mnames.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            maleNames = gson.fromJson(bufferedReader, ListOfNames.class).getData();
        }
    }
    private void loadFemaleNames() throws IOException {
        try(InputStream inputStream = FillService.class.getResourceAsStream("/fnames.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            femaleNames = gson.fromJson(bufferedReader, ListOfNames.class).getData();
        }
    }
    private void loadLastNames() throws IOException {
        try(InputStream inputStream = FillService.class.getResourceAsStream("/snames.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            lastNames = gson.fromJson(bufferedReader, ListOfNames.class).getData();
        }
    }
    private void loadLocations() throws IOException {
        try(InputStream inputStream = FillService.class.getResourceAsStream("/locations.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            locations = gson.fromJson(bufferedReader, ListOfLocations.class).getData();
        }
    }

    private void clearOldData() throws DataAccessException {
        try{
            clearOldEvents();
            clearOldPersons();
            clearOldUser();
        }catch(DataAccessException ex){
            ex.printStackTrace();
            throw ex;
        }finally{
            this.closeConnection();
        }

    }
    private void clearOldUser() throws DataAccessException {
        UserDAO uDao = new UserDAO(this.getConnection());
        uDao.delete(associatedUsername);
    }
    private void clearOldPersons() throws DataAccessException {
        PersonDAO pDao = new PersonDAO(this.getConnection());
        pDao.deleteWithUsername(associatedUsername);
    }
    private void clearOldEvents() throws DataAccessException {
        EventDAO uDao = new EventDAO(this.getConnection());
        uDao.deleteWithUsername(associatedUsername);
    }
}
