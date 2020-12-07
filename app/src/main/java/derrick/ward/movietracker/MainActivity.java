package derrick.ward.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
        this.currentFirebaseUser = this.firebaseAuth.getCurrentUser();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new CountDownTimer(3000, 1000) {

            @Override
            public void onTick(long milliSecondsUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (currentFirebaseUser == null) {
                    Toast.makeText(MainActivity.this, "No signed in user detected", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                } else {
                    if (currentFirebaseUser.isEmailVerified()) {
                        Toast.makeText(MainActivity.this, "Signed in User Detected", Toast.LENGTH_SHORT).show();

                        startActivity( new Intent(MainActivity.this, Movies.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Please verify your email before trying to login. Check your email for process.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Login.class));
                        finish();
                    }
                }
            }
        }.start();
    }
}