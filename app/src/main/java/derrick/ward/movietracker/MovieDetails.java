package derrick.ward.movietracker;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import derrick.ward.movietracker.models.Movie;

public class MovieDetails extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference movieDatabaseTable = database.getReference("Movies");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication

        ImageView moviePoster = findViewById(R.id.detailViewMoviePoster);
        TextView movieTitle = findViewById(R.id.detailViewMovieTitle);
        TextView movieYear = findViewById(R.id.detailViewMovieYear);
        TextView movieLength = findViewById(R.id.detailViewMovieLength);
        TextView movieDirector = findViewById(R.id.detailViewMovieDirector);
        RatingBar movieRating = findViewById(R.id.detailViewMovieRating);
        TextView movieRatingNumber = findViewById(R.id.detailViewMovieRatingNumber);
        TextView movieDescription = findViewById(R.id.detailViewMovieDescription);
        EditText movieStars = findViewById(R.id.detailViewMovieStars);

        Intent intent = getIntent();
        if (intent != null) {
            String movieName = intent.getStringExtra("MovieName");

            DatabaseReference movieDetailsDatabaseReference = movieDatabaseTable.child(movieName);
            movieDetailsDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        Movie movie = getMovieDataFromHashMap((HashMap<String, Object>) snapshot.getValue());

                        // Download movie Poster Image
                        FirebaseStorage storage = FirebaseStorage.getInstance();
                        StorageReference storageReference = storage.getReferenceFromUrl(movie.url);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(moviePoster); // Load image into supplied ImageView Element
                            }
                        }).addOnFailureListener(e -> {
                            Toast.makeText(MovieDetails.this, "Error Downloading Movie Poster! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                        movieTitle.setText(movie.name);
                        movieYear.setText("("+movie.year+")");
                        movieLength.setText(movie.length);
                        movieDirector.setText(movie.director);
                        movieStars.setText(movie.stars);
                        movieRating.setRating((float)movie.rating);
                        movieRatingNumber.setText(String.valueOf(movie.rating));
                        movieDescription.setText(movie.description);


                    } else {
                        Toast.makeText(MovieDetails.this, "No Database information found for "+ movieName, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    // Map HashMap into Movie Model
    private Movie getMovieDataFromHashMap(HashMap<String, Object> movieData) {
        Movie movie = new Movie();

        movie.name = movieData.get("name").toString();
        movie.description = movieData.get("description").toString();
        movie.length = movieData.get("length").toString();
        movie.year = movieData.get("year").toString();
        movie.rating = Double.parseDouble(movieData.get("rating").toString());
        movie.director = movieData.get("director").toString();
        movie.stars = movieData.get("stars").toString();
        movie.url = movieData.get("url").toString();
        movie.imageName = movieData.get("imageName").toString();

        return movie;
    }
}
