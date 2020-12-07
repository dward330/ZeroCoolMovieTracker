package derrick.ward.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        this.firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
    }

    /*
    * Tries to login the user
    */
    public void login(View view) {
        String emailEntered = ((EditText)findViewById(R.id.loginEmail)).getText().toString();
        String passwordEntered = ((EditText)findViewById(R.id.loginPassword)).getText().toString();

        if (emailEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (passwordEntered.trim().equals("")) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }

        this.firebaseAuth.signInWithEmailAndPassword(emailEntered, passwordEntered)
                        .addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                currentFirebaseUser = authResult.getUser();

                                if (currentFirebaseUser.isEmailVerified()) {
                                    Toast.makeText(Login.this, "Login Successful.", Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(Login.this, Movies.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Please verify your email first. Check your email for process.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).addOnFailureListener(this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    /*
    * Navigates to Sign Up Activity/Screen
    */
    public void navigateToSignUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }

    /*
    * Resets the password of the email entered
    */
    public void resetPassword(View view) {
        String emailEntered = ((EditText)findViewById(R.id.loginEmail)).getText().toString();

        if (emailEntered.trim().equals("")) {
            Toast.makeText(this, "Please enter email you want password reset for!", Toast.LENGTH_SHORT).show();
            return;
        }

        this.firebaseAuth.sendPasswordResetEmail(emailEntered)
                         .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                             @Override
                             public void onSuccess(Void aVoid) {
                                 Toast.makeText(Login.this, "An email to reset password has been sent!", Toast.LENGTH_SHORT).show();
                             }
                         }).addOnFailureListener(this, e -> {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                         });
    }

    /*
    * Resend email verification for the email entered
    */
    public void resendEmailVerification(View view) {
        String emailEntered = ((EditText)findViewById(R.id.loginEmail)).getText().toString();

        if (emailEntered.trim().equals("")) {
            Toast.makeText(this, "Please enter email you want password reset for!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (this.currentFirebaseUser == null) {
            Toast.makeText(this, "Please login first to re-send verification email.", Toast.LENGTH_SHORT).show();
            return;
        }

        this.currentFirebaseUser.sendEmailVerification()
                        .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "An email with verification steps has been sent", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(this, e -> {
                            Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
    }
}
