package derrick.ward.movietracker;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import derrick.ward.movietracker.models.Movie;
import derrick.ward.movietracker.models.MoviesRecyclerAdapter;

public class NewMovieSubmission extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference movieDatabaseTable = database.getReference("Movies");
    private static final int REQUEST_FOR_LOCATION = 123 ;
    private static final int REQUEST_FOR_CAMERA=0011;
    private static final int OPEN_FILE=0012;
    private Uri imageUri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_movie_submission);

        //Add Photo Options
        ImageView moviePoster = findViewById(R.id.submissionMoviePoster);
        moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(NewMovieSubmission.this, view);
                popupMenu.setOnMenuItemClickListener(NewMovieSubmission.this);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.photo_menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.selectPhotoFromGallary:
                selectPhoto();
                return true;
            case R.id.takePhoto:
                takePhoto();
                return true;
            default:
                return false;
        }
    }

    /* Saves Movie Review*/
    public void submitMovieReview(View view) {
        String movieTitleEntered = ((EditText)findViewById(R.id.submissionMovieTitle)).getText().toString();
        String movieYearEntered = ((EditText)findViewById(R.id.submissionMovieYear)).getText().toString();
        String movieLengthEntered = ((EditText)findViewById(R.id.submissionMovieLength)).getText().toString();
        String movieDirectorEntered = ((EditText)findViewById(R.id.submissionMovieDirector)).getText().toString();
        String movieCastEntered = ((EditText)findViewById(R.id.submissionMovieCast)).getText().toString();
        String movieRatingEntered = ((EditText)findViewById(R.id.submissionMovieRating)).getText().toString();
        String movieDescriptionEntered = ((EditText)findViewById(R.id.submissionMovieDescription)).getText().toString();

        if (imageUri == null) {
            Toast.makeText(this, "Please Tap on the Image to Select or Take a Photo", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieTitleEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieYearEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Year", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieLengthEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Length (ex. 125 min)", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieDirectorEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Director", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieCastEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Cast", Toast.LENGTH_SHORT).show();
            return;
        }

        if (movieRatingEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Rating (1-10, ex 8.5)", Toast.LENGTH_SHORT).show();
            return;
        } else {
            boolean ratingIsANumber = true;

            try {
                double rating = Double.parseDouble(movieRatingEntered.trim());
            } catch (Exception e) {
                ratingIsANumber = false;
            }

            if (!ratingIsANumber) {
                Toast.makeText(this, "Please Enter Movie's Rating as a number! (1-10, ex 8.5)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (movieDescriptionEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Movie's Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Movie newMovie = new Movie();
        newMovie.name = movieTitleEntered.trim();
        newMovie.year = movieYearEntered.trim();
        newMovie.length = movieLengthEntered.trim();
        newMovie.director = movieDirectorEntered.trim();
        newMovie.stars = movieCastEntered.trim();
        newMovie.rating = Double.parseDouble(movieRatingEntered.trim());
        newMovie.description = movieDescriptionEntered.trim();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Generate Image Filename
        final String fileNameInStorage = UUID.randomUUID().toString();
        // Generate Image Storage Location
        String path="movieArt/"+ fileNameInStorage+".jpg";
        final StorageReference imageFirebaseStorageRef = storage.getReference(path);
        imageFirebaseStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Update Image Meta Data in Movie Model
                newMovie.imageName = fileNameInStorage+".jpg";
                newMovie.url = "gs://movietracker-3e36a.appspot.com/movieArt"+"/"+newMovie.imageName;

                // Saves Movie
                movieDatabaseTable.child(newMovie.name).runTransaction(getCreateMovieTransactionHandler(newMovie));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_FOR_LOCATION && ((grantResults.length>0 && grantResults[0]!= PackageManager.PERMISSION_GRANTED) || (grantResults.length>1 && grantResults[1]!=PackageManager.PERMISSION_GRANTED))){
            Toast.makeText(this, "We need to access your location", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_CAMERA && resultCode == RESULT_OK) {
            if(imageUri==null)
            {
                Toast.makeText(this, "Error taking photo.", Toast.LENGTH_SHORT).show();
                return;
            }

            ImageView imageView=findViewById(R.id.submissionMoviePoster);
            imageView.setImageURI(imageUri);

            return;
        }

        // User Choice a Photo from the gallery
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();

            ImageView imageView=findViewById(R.id.submissionMoviePoster);
            imageView.setImageURI(imageUri);
        }
    }

    /* Launches Camera App */
    private void takePhoto(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Movie Post Photo");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        this.imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        Intent chooser=Intent.createChooser(intent,"Select a Camera App.");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(chooser, REQUEST_FOR_CAMERA);}
    }

    /* Launches Photo Gallery so user make select photo */
    private void selectPhoto(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser=Intent.createChooser(pickPhoto,"Select a Photo Gallery App.");
        if (pickPhoto.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(pickPhoto, 1);}
    }

    /* Generates a Transaction Handler for creating supplied Movie */
    private Transaction.Handler getCreateMovieTransactionHandler(Movie movie) {
        return new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {

                currentData.setValue(movie);

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
                imageUri = null;

                startActivity(new Intent(NewMovieSubmission.this, Movies.class));
                finish(); // Negate Back Button from bringing the user back here
            }
        };
    }
}
