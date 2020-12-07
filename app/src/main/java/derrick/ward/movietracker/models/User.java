package derrick.ward.movietracker.models;

import com.google.firebase.database.ServerValue;

/*
* Contains Information on a User
*/
public class User {
    public String displayName;
    public String email;
    public String phoneNumber;
    public Object timestamp;

    public User(String displayName, String email, String phoneNumber) {
        this.displayName = displayName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.timestamp= ServerValue.TIMESTAMP;
    }

    public User() {

    }

    public Object getTimestamp(){
        return timestamp;
    }


}
