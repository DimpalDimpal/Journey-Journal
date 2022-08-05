package com.ismt.journeyjournal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AboutUsActivity extends AppCompatActivity {

    //below line of code is to create a variable.
    private ImageView icFacebook, icLinkedin, icInstagram, icTwitter, icBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        //below line of code is to initialize each variable.
        icFacebook = findViewById(R.id.icFacebook);
        icLinkedin = findViewById(R.id.icLinkedin);
        icInstagram = findViewById(R.id.icInstagram);
        icTwitter = findViewById(R.id.icTwitter);
        icBack = findViewById(R.id.icBack);

        //below line of code is to set on click listener for icFacebook
        icFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com"));
                startActivity(intent);
            }
        });

        //below line of code is to set on click listener for icLinkedin
        icLinkedin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.linkedin.com"));
                startActivity(intent);
            }
        });

        //below line of code is to set on click listener for icInstagram
        icInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com"));
                startActivity(intent);
            }
        });

        //below line of code is to set on click listener for icTwitter
        icTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com"));
                startActivity(intent);
            }
        });

        //below line of code is to set on click listener for icBack
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AboutUsActivity.this, DashboardActivity.class);
                finish();
                startActivity(intent);
            }
        });



    }
}