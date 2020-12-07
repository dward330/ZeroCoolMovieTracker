package derrick.ward.movietracker.models;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import derrick.ward.movietracker.R;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {
    private Context context;
    private List<Movie> movies = new ArrayList<Movie>();
    FirebaseDatabase database;
    private DatabaseReference movieDatabaseTable;
    private ChildEventListener movieChangeDbRefListener;
    private RecyclerView recyclerView;

    public MoviesRecyclerAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.database = FirebaseDatabase.getInstance(); // Get a reference to the firebase database
        this.movieDatabaseTable = database.getReference("Movies");
        this.listenForMovieDataChanges();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = parent.getContext();

        // Create a new instance of this layout as a View
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card_details, parent,false);

        final MoviesRecyclerAdapter.MovieViewHolder viewHolder = new MoviesRecyclerAdapter.MovieViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        final Movie movie = this.movies.get(position);

        // If this there is already a movie database table reference && If there is a movie change event listener
        if(holder.movieDbRef !=null && holder.movieInfoChangeListener !=null)
        {
            // Remove current movie change event listener from the movie database table reference
            holder.movieDbRef.removeEventListener(holder.movieInfoChangeListener);
        }

        holder.movieTitle.setText(movie.name);
        holder.movieLength.setText(movie.length);
        holder.movieYear.setText(movie.year);
        holder.movieDirector.setText(movie.director);
        holder.movieStars.setText(movie.stars);
        holder.movieRating.setRating((float)movie.rating);
        holder.movieDescription.setText(movie.description);


        // Download movie Poster Image
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(movie.url);
        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.moviePoster); // Load image into supplied ImageView Element
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(context, "Error Downloading Movie Poster! "+ e.getMessage(), Toast.LENGTH_SHORT).show();
        });

        /* Get a reference to the movie in our movies database */
        holder.movieDbRef = this.movieDatabaseTable.child(movie.name);
        // When user's info is first published to client, let display the correct name
        holder.movieDbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                holder.movieTitle.setText(dataSnapshot.child("name").getValue().toString());
                holder.movieLength.setText("Length: "+dataSnapshot.child("length").getValue().toString());
                holder.movieYear.setText("Year: "+dataSnapshot.child("year").getValue().toString());
                holder.movieDirector.setText("Director: "+dataSnapshot.child("director").getValue().toString());
                holder.movieStars.setText("Actors: "+dataSnapshot.child("stars").getValue().toString());
                holder.movieRating.setRating(Float.parseFloat(dataSnapshot.child("rating").getValue().toString()));
                holder.movieDescription.setText(dataSnapshot.child("description").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return this.movies.size();
    }

    private void listenForMovieDataChanges() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); // Get a reference to the firebase database

        this.movieChangeDbRefListener = database.getReference("Movies").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Movie movieData = getMovieDataFromHashMap((HashMap<String, Object>)dataSnapshot.getValue());

                if (movieData != null) {
                    movies.add(movieData);

                    Collections.sort(movies, new Comparator<Movie>() {
                        @Override
                        public int compare(Movie m1, Movie m2) {
                            return m1.name.compareTo(m2.name);
                        }
                    });

                    MoviesRecyclerAdapter.this.notifyDataSetChanged(); // Trigger adapter to reprocess all movies in the conversation
                    MoviesRecyclerAdapter.this.recyclerView.scrollToPosition(movies.size()-1); // Tell adapter to scroll down to the last movie
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                Movie movieChanged = getMovieDataFromHashMap((HashMap<String, Object>)dataSnapshot.getValue());

                boolean positionFound = false;
                int position = 0;
                for (Movie movie : movies) {
                    if (movie.name.toUpperCase().equals(movieChanged.name.toUpperCase())) {
                        positionFound = true;
                        break;
                    }
                    position++;
                }

                if (positionFound == true) {
                    movies.set(position, movieChanged);
                    MoviesRecyclerAdapter.this.notifyItemChanged(position);

                    MoviesRecyclerAdapter.this.recyclerView.scrollToPosition(position);

                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Movie movieRemoved = getMovieDataFromHashMap((HashMap<String, Object>)snapshot.getValue());

                boolean positionFound = false;
                int position = 0;
                for (Movie movie : movies) {
                    if (movie.name.toUpperCase().equals(movieRemoved.name.toUpperCase())) {
                        positionFound = true;
                        break;
                    }
                    position++;
                }

                if (positionFound == true) {
                    movies.remove(position);
                    MoviesRecyclerAdapter.this.notifyItemRemoved(position);

                    if (position > 0) {
                        MoviesRecyclerAdapter.this.recyclerView.scrollToPosition(position - 1);
                    } else {
                        MoviesRecyclerAdapter.this.recyclerView.scrollToPosition(0);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

        // Stop listening for movie database table changes
    public void removeListener(){
        if(this.movieDatabaseTable !=null && this.movieChangeDbRefListener!=null)
            this.movieDatabaseTable.removeEventListener(this.movieChangeDbRefListener);
    }

    // Class used to communicate with UI Elements used for each instance of an entry
    public static class MovieViewHolder extends RecyclerView.ViewHolder{
        public TextView movieTitle;
        public TextView movieLength;
        public TextView movieYear;
        public TextView movieDirector;
        public TextView movieStars;
        public RatingBar movieRating;
        public TextView movieDescription;
        public ImageView moviePoster;

        public DatabaseReference movieDbRef; // Holds a reference to a specific movie in movies database table
        public ValueEventListener movieInfoChangeListener; // Holds a reference to listener to invoke when this movie is changed in movies database table


        public MovieViewHolder(View v){
            super(v);

            // Bind Layout UI Elements to properties in View Holder Instance
            this.movieTitle = v.findViewById(R.id.movieTitle);
            this.movieLength = v.findViewById(R.id.movieLength);
            this.movieYear = v.findViewById(R.id.movieYear);
            this.movieDirector = v.findViewById(R.id.movieDirector);
            this.movieStars = v.findViewById(R.id.movieStars);
            this.movieRating = v.findViewById(R.id.movieRating);
            this.movieDescription = v.findViewById(R.id.movieDescription);
            this.moviePoster = v.findViewById(R.id.moviePoster);
        }
    }
}
