package derrick.ward.movietracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }

    /*
    * Navigates to Sign Up Activity/Screen
    */
    public void navigateToSignUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }
}
