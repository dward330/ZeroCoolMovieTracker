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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import derrick.ward.movietracker.models.MovieData;
import derrick.ward.movietracker.models.MoviesRecyclerAdapter;

public class Movies extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference movieDatabaseTable = database.getReference("Movies");
    private MoviesRecyclerAdapter moviesRecyclerAdapter;

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

                    RecyclerView recyclerView=findViewById(R.id.movies_recylcer_view);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(Movies.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    layoutManager.scrollToPosition(0);
                    recyclerView.setLayoutManager(layoutManager);
                    moviesRecyclerAdapter=new MoviesRecyclerAdapter(recyclerView);
                    recyclerView.setAdapter(moviesRecyclerAdapter);
                } else {
                    RecyclerView recyclerView=findViewById(R.id.movies_recylcer_view);
                    LinearLayoutManager layoutManager=new LinearLayoutManager(Movies.this);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    layoutManager.scrollToPosition(0);
                    recyclerView.setLayoutManager(layoutManager);
                    moviesRecyclerAdapter=new MoviesRecyclerAdapter(recyclerView);
                    recyclerView.setAdapter(moviesRecyclerAdapter);
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
