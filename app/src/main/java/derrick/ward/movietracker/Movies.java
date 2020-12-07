package derrick.ward.movietracker;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import derrick.ward.movietracker.models.MovieData;

public class Movies extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference movieDatabaseTable = database.getReference("Movies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        this.seedMovieDatabaseTableIfMissing();
    }

    /*
    * Will seed the Movie Database Table with information if it is missing
    */
    private void seedMovieDatabaseTableIfMissing() {
        movieDatabaseTable.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {

                    // Seed the Database with Data
                    movieDatabaseTable.runTransaction(getTransactionToSeedDatabaseWithMovieInfo());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /*
    * Generates a Transaction Handler to seed the Movies Database with Movie Info
    */
    private Transaction.Handler getTransactionToSeedDatabaseWithMovieInfo() {
        return new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Object data = new MovieData().getMoviesList();

                HashMap<String, HashMap<String, String>> movies = new HashMap<String, HashMap<String, String>>();
                HashMap<String, String> sampleData = new HashMap<>();
                sampleData.put("movieName", "Name");
                sampleData.put("movieDirector", "Director");
                sampleData.put("Rating", String.valueOf(3.4));

                movies.put("Movie 1", sampleData);

                // Set movies' information
                currentData.setValue(data);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                String x = "successful";
            }
        };
    }
}
