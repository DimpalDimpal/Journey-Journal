package com.ismt.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ismt.journeyjournal.User.User;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    //below line of code is to create a variable.
    private EditText edtFullName, edtUserName, edtEmail, edtPassword, edtConfirmPassword;
    private TextView loginHere;
    private Button btnRegister;

    private UserRepository userRepository;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    private SignInButton signInButton;
    private Button signOutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        //build the GoogleSignInClient with the options specified by gso
        gsc = GoogleSignIn.getClient(this,gso);

        //below line of code is to initialize each variable.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        userRepository = new UserRepository(this);
        edtFullName= findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtUserName = findViewById(R.id.edtUserName);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);

        btnRegister= findViewById(R.id.btnRegister);
        loginHere= findViewById(R.id.loginHere);

        //below line of code is to set on click listener for signInButton
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        //below line of code is to set on click listener for btnRegister
        btnRegister.setOnClickListener(view -> {

            registerUser();
            int uid = 0;
            String fullName= edtFullName.getText().toString();
            String username= edtUserName.getText().toString();
            String email= edtEmail.getText().toString();
            String password= edtPassword.getText().toString();


            User user = new User(0, fullName, username, email, password);
            userRepository.insertUser(user);

        });

        //below line of code is to set on click listener for loginHere
        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    //below line is to create intent to dashboard
    void navigateToDashboard(){
        finish();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    //below line of code is for signin
    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 1000) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                task.getResult(ApiException.class);
                navigateToDashboard();
            }catch(ApiException e){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }

        }


    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr){
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void registerUser() {
        String fullName= edtFullName.getText().toString();
        String username= edtUserName.getText().toString();
        String email= edtEmail.getText().toString();
        String password= edtPassword.getText().toString();
        String confirmPassword= edtConfirmPassword.getText().toString();

//        Validations

        if(fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            Toast.makeText(this, "Required fields are empty", Toast.LENGTH_SHORT).show();
        }
        else if (!password.equals(confirmPassword)){
            Toast.makeText(this, "Passwords doesnot match", Toast.LENGTH_SHORT).show();

            edtConfirmPassword.setError("Retype your password");
        }
        else if (!validate(email)){
            Toast.makeText(this, "Please enter correct format of email address as: \n example@xyzs.com", Toast.LENGTH_SHORT).show();
        }
        else{
            fullName= edtFullName.getText().toString();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            Toast.makeText(RegisterActivity.this, fullName + " registered", Toast.LENGTH_SHORT).show();


        }

    }

    //for password show hide
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

    //for show hide confirm password
    public void ShowHideConfPwd(View view) {

        if(view.getId()==R.id.imgShowHideConf){
            if(edtConfirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.show_pwd);
                //Show Password
                edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.hide_pwd);
                //Hide Password
                edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        }
    }
}
















