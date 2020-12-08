package derrick.ward.movietracker.models;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import derrick.ward.movietracker.MovieDetails;
import derrick.ward.movietracker.R;

public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> implements Filterable,  PopupMenu.OnMenuItemClickListener {
    private Context context;
    private List<Movie> movies = new ArrayList<Movie>();
    FirebaseDatabase database;
    private DatabaseReference movieDatabaseTable;
    private Query moviesWithRating;
    private ChildEventListener movieChangeDbRefListener;
    private RecyclerView recyclerView;
    private Movie currentSelectedMovie;

    public MoviesRecyclerAdapter(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.database = FirebaseDatabase.getInstance(); // Get a reference to the firebase database
        this.movieDatabaseTable = database.getReference("Movies");
        this.listenForMovieDataChanges(this.movieDatabaseTable, null);
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
                holder.movieDescription.setText(dataSnapshot.child("description").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Set Card On click
        holder.rootContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMovieDetails(movie.name);
            }
        });

        // Set Options On Click
        holder.movieOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelectedMovie = movie;
                PopupMenu popupMenu = new PopupMenu(context, view);
                popupMenu.setOnMenuItemClickListener(MoviesRecyclerAdapter.this);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.movie_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (this.movies == null) {
            return 0;
        }

        return this.movies.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                List<Movie> filteredMovies = new ArrayList<Movie>();

                if (charString.trim().isEmpty()) {
                    // Set Back to Movies Table Reference
                    listenForMovieDataChanges(movieDatabaseTable, null);
                } else {
                    boolean filterIsANumber = true;
                    double rating=0;

                    try {
                        rating = Double.parseDouble(charString.trim());
                    } catch (Exception e) {
                        filterIsANumber = false;
                    }

                    if (!filterIsANumber) {
                        Toast.makeText(context, "Filter only takes a number (ex. 1 or 5.5)", Toast.LENGTH_SHORT).show();
                        // Set Back to Movies Table Reference
                        listenForMovieDataChanges(movieDatabaseTable, null);
                    } else {
                        // Build Query with filter to listen to
                        moviesWithRating = movieDatabaseTable.orderByChild("rating").startAt(rating);
                        listenForMovieDataChanges(null, moviesWithRating);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredMovies;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movies = (List<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.delete:
                if (currentSelectedMovie != null) {
                    movieDatabaseTable.child(currentSelectedMovie.name).runTransaction(getDeleteMovieTransactionHandler());
                }
                return true;
            case R.id.duplicate:
                if (currentSelectedMovie != null) {
                    movieDatabaseTable.child(currentSelectedMovie.name+"_new").runTransaction(getCreateDuplicateMovieTransactionHandler(currentSelectedMovie));
                }
                return true;
            default:
                return false;
        }
    }

    /* Gets Transaction Handler for Deleting a Movie */
    private Transaction.Handler getDeleteMovieTransactionHandler() {
        return new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Movie movie = getMovieDataFromHashMap((HashMap<String, Object>) currentData.getValue());

                movie = null;

                currentData.setValue(movie);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                currentSelectedMovie = null;
            }
        };
    }

    /* Gets Transaction Handler for Duplicating a Movie */
    private Transaction.Handler getCreateDuplicateMovieTransactionHandler(Movie movie) {
        return new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Movie newMovie = new Movie();
                newMovie.name = movie.name+"_new";
                newMovie.description = movie.description;
                newMovie.length = movie.length;
                newMovie.year = movie.year;
                newMovie.rating = movie.rating;
                newMovie.director = movie.director;
                newMovie.stars = movie.stars;
                newMovie.url = movie.url;
                newMovie.imageName = movie.imageName;

                currentData.setValue(newMovie);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                currentSelectedMovie = null;
            }
        };
    }

    /*
    * Navigate and Show Movie Details
    */
    private void showMovieDetails(String movieName) {
        Intent movieDetailsIntent = new Intent(context, MovieDetails.class);
        movieDetailsIntent.putExtra("MovieName", movieName);
        context.startActivity(movieDetailsIntent);
    }

    /* Listens for Database Changes */
    private void listenForMovieDataChanges(DatabaseReference databaseReference, Query query) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance(); // Get a reference to the firebase database

        // Clear Movies
        movies.clear();

        // Remove Previous Database Table Listeners if they Exist
        if (movieDatabaseTable != null && this.movieChangeDbRefListener != null) {
            movieDatabaseTable.removeEventListener(this.movieChangeDbRefListener);
        }

        // Remove Previous Query Listeners if they Exist
        if (moviesWithRating != null && this.movieChangeDbRefListener != null) {
            moviesWithRating.removeEventListener(this.movieChangeDbRefListener);
        }

        if (databaseReference != null) {
            this.movieChangeDbRefListener = movieDatabaseTable.addChildEventListener(getChildEventListenerForMovies());
        } else if (query != null) {
            this.movieChangeDbRefListener = moviesWithRating.addChildEventListener(getChildEventListenerForMovies());
        }
    }

    /* Listen for Movies CRUD Operations and updates View */
    private ChildEventListener getChildEventListenerForMovies() {
        return new ChildEventListener() {
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
        };
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
        public TextView movieDescription;
        public ImageView moviePoster;
        public CardView rootContainer;
        public ImageView movieOptions;

        public DatabaseReference movieDbRef; // Holds a reference to a specific movie in movies database table
        public ValueEventListener movieInfoChangeListener; // Holds a reference to listener to invoke when this movie is changed in movies database table


        public MovieViewHolder(View v){
            super(v);

            // Bind Layout UI Elements to properties in View Holder Instance
            this.movieTitle = v.findViewById(R.id.movieTitle);
            this.movieDescription = v.findViewById(R.id.movieDescription);
            this.moviePoster = v.findViewById(R.id.moviePoster);
            this.rootContainer = v.findViewById(R.id.rootContainer);
            this.movieOptions = v.findViewById(R.id.options);
        }
    }
}
