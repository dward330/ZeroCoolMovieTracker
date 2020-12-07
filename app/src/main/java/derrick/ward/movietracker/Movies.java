package derrick.ward.movietracker;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;

public class Movies extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movies);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
    }
}
