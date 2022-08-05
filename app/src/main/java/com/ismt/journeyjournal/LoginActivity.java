package com.ismt.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ismt.journeyjournal.User.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    // below line of code is to create a variables
    private TextView registerHere;
    private EditText edtEmail, edtPassword;
    private Button btnLogin;
    private UserRepository repository;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        repository = new UserRepository(this);

        // below line of code is to initialize our variable
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        registerHere= findViewById(R.id.registerHere);
        btnLogin = findViewById(R.id.btnLogin);

        //below line of code is to create preferences
        preferences=  getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);

        //below line of code is to set on click listener for registerHere
        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //below line of code is to set on click listener for btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(edtEmail.getText().toString().isEmpty() || edtPassword.getText().toString().isEmpty()){
                   Toast.makeText(LoginActivity.this, "Required fields are empty", Toast.LENGTH_SHORT).show();
               }else{
                   User user = repository.selectUser(edtEmail.getText().toString(),
                           edtPassword.getText().toString());
                   if(user != null){

                       // below line of code is to create intent to dashboard
                       String userFullName = user.getFullName();
                       Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                       startActivity(intent);

                       // below line of code is to set shared preferences
                       SharedPreferences.Editor editor = preferences.edit();
                       editor.putString("user",userFullName).apply();
                                                }
                   else{

                       //below line of code is to create a toast for invalid login credentials
                       Toast.makeText(LoginActivity.this, "Login Credentials doesnot match", Toast.LENGTH_SHORT).show();
                   }

               }

            }
        });
    }

    //below line of code is for password show hide
    public void ShowHidePwd(View view) {

        if(view.getId()==R.id.imgShowHide){
            if(edtPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.show_pwd);
                //Show Password
                edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.hide_pwd);
                //Hide Password
                edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}