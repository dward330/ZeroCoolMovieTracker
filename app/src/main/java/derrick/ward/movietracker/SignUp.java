package derrick.ward.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import derrick.ward.movietracker.models.User;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
    }

    /*
    * Registers a new User
    */
    public void registerNewUser(View view) {
        String emailEntered = ((EditText)findViewById(R.id.signUpEmail)).getText().toString();
        String passwordEntered = ((EditText)findViewById(R.id.signUpPassword)).getText().toString();
        String displayNameEntered = ((EditText)findViewById(R.id.signUpDisplayName)).getText().toString();
        String phoneNumberEntered = ((EditText)findViewById(R.id.signUpPhoneNumber)).getText().toString();

        if (emailEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (displayNameEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter a Display Name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (phoneNumberEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter a Phone Number", Toast.LENGTH_SHORT).show();
            return;
        }

        User newUser = new User(displayNameEntered, emailEntered, phoneNumberEntered);

        this.firebaseAuth.createUserWithEmailAndPassword(emailEntered, passwordEntered)
                .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        currentFirebaseUser = authResult.getUser();
                        currentFirebaseUser.sendEmailVerification().addOnSuccessListener(SignUp.this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(SignUp.this, "Sign up Successful! Please verify your email before trying to login. Check your email for process.", Toast.LENGTH_SHORT).show();

                                saveNewUserToDB(newUser);

                                // Bring user to login activity
                                startActivity( new Intent(SignUp.this, Login.class));
                                finish(); // Make sure back button does not bring user back to this activity
                            }
                        }).addOnFailureListener(SignUp.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    /*
    * Saves new User to Database
    */
    private void saveNewUserToDB(User newUser) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersTable = database.getReference("Users");

        usersTable.child(this.currentFirebaseUser.getUid())
                  .setValue(newUser);
    }
}
