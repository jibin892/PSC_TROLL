package techsayas.in.psctrolls.psctroll;

public class User {

    String userId;
    String email;
    Boolean userHasInterest;
    String eventId;

    public String getUserId() {
        return userId;
    }
    public String getEmail() {
        return email;
    }

    public Boolean getUserHasInterest() {
        return userHasInterest;
    }

    public String getEventId() {
        return eventId;
    }

    public User(String userId, String eventId, String email, Boolean userHasInterest) {
        this.userId = userId;
        this.email = email;
        this.userHasInterest = userHasInterest;
        this.eventId = eventId;
    }}