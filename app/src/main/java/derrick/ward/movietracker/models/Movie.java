package derrick.ward.movietracker.models;

import com.google.firebase.database.ServerValue;

public class Movie {
    public Object timestamp;
    public String name;
    public String description;
    public String length;
    public String year;
    public double rating;
    public String director;
    public String stars;
    public String url;
    public String imageName;

    public Movie() {
        this.timestamp= ServerValue.TIMESTAMP;
    }
}
