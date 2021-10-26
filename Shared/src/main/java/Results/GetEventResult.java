package Results;

import Model.Event;

/**
 * Represents JSON data sent from the GetEventService back to the server
 */
public class GetEventResult extends Result{
    private String eventID;
    private String associatedUsername;
    private String personID;
    private float latitude;
    private float longitude;
    private String country;
    private String city;
    private String eventType;
    private int year;

    /**
     * Constructs a new GetEventResult object with the given event
     * @param event an event
     */
    public GetEventResult(Event event){
        this.setSuccess(true);
        eventID = event.getEventID();
        associatedUsername = event.getUsername();
        personID = event.getPersonID();
        latitude = event.getLatitude();
        longitude = event.getLongitude();
        country = event.getCountry();
        city = event.getCity();
        eventType = event.getEventType();
        year = event.getYear();
    }

    public String getAssociatedUsername() {
        return associatedUsername;
    }

    public String getPersonID() {
        return personID;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getEventType() {
        return eventType;
    }

    public int getYear() {
        return year;
    }

    public String getEventID() {
        return eventID;
    }
}
