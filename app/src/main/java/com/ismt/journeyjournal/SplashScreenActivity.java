package com.ismt.journeyjournal;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import android.window.SplashScreen;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //below line of code is to create shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);
        String userFullName = sharedPreferences.getString("user", "");
        new Handler().postDelayed(()->{

            //below line of code is to check if the user is logged in from their google credentials or, not
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(SplashScreenActivity.this);

            //if user is logged in through their google credentials
            if (acct != null){
                String personEmail = acct.getEmail();
                Toast.makeText(SplashScreenActivity.this, personEmail,Toast.LENGTH_SHORT).show();
                navigateToDashboard();
            }
            else{

            if (userFullName.isEmpty()) {
                startActivity(new Intent(this, MainActivity.class));
            } else {
                startActivity(new Intent(this, DashboardActivity.class));
            }
            finish();}
        }, 3000);

    }

    //below line of code is to create intent to dashboard
    private void navigateToDashboard() {
        finish();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }
}