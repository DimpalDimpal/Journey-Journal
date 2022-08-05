package com.ismt.journeyjournal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.ismt.journeyjournal.Journal.Journal;
import com.ismt.journeyjournal.Journal.JournalRepository;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;

public class issueJourneyActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {


    public static final String EXTRA_ID = "com.ismt.journeyjournal.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.ismt.journeyjournal.EXTRA_JOURNAL_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.ismt.journeyjournal.EXTRA_JOURNAL_DESCRIPTION";

    public static final String EXTRA_DATE = "com.ismt.journeyjournal.EXTRA_JOURNAL_DATE";
    public static final String EXTRA_IMG = "com.ismt.journeyjournal.EXTRA_JOURNAL_IMG";

    //below line of code for creating a variable
    private AppCompatImageButton addLocation;
    private AppCompatTextView displayLocation;
    private Location myLocation;
    private AppCompatEditText edtTitle, edtDesc;
    private Button btnSave, DatePicker, btnImage;
    private JournalRepository jRepository;
    private LocationManager locationManager;
    private ImageView loadImage, icBack;

    private String selectedImagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_journey);

        jRepository = new JournalRepository(this);

        //below line of code is for initializing the variables
        edtTitle = findViewById(R.id.edtTitle);
        edtDesc = findViewById(R.id.edtDesc);
        addLocation = findViewById(R.id.addLocation);
        displayLocation = findViewById(R.id.displayLocation);
        DatePicker = findViewById(R.id.DatePicker);
        btnImage = findViewById(R.id.btn_image);
        icBack = findViewById(R.id.icBack);
        btnSave = findViewById(R.id.btnSave);

        //below line of code is for setting the on click listener for back icon
        icBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(issueJourneyActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        //below line of code is for initializing the loadImage
        loadImage = findViewById(R.id.loadImage);

        //below line of code is for setting on click listener for btmImage
        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(issueJourneyActivity.this);
                builder.setMessage("Choose Image:");
                //below line of code is for the camera
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        checkAndRequestPermissions();

                    }

                    int code = 101;

                    // check and request permission for the different application
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    private void checkAndRequestPermissions() {
                        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);

                        } else if (code == 101) {
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, 101);
                        } else {

                            Intent intent = new Intent(Intent.ACTION_PICK);
                            startActivityForResult(intent, 102);
                        }
                    }
                });

                //below line of code is for the gallery
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        checkAndRequestPermissionsFromGallery();

                    }

                    int code = 103;

                    // check and request permission for the different application
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    private void checkAndRequestPermissionsFromGallery() {
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 103);

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();


            }
        });

//        below line of code is forsetting on click listener for addLocation
        addLocation.setOnClickListener(v -> {
            if (myLocation != null)
                displayLocation.setText("lat: " + myLocation.getLatitude() + ", lng: " + myLocation.getLongitude());
        });

        //below line of code is for the datpicker
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int dayOfMonth) {
                DatePicker.setText(month + "-" + dayOfMonth + "-" + year);
            }
        }, year, month, day);

        //below line of code is for setting on click listener for DatePicker
        DatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

        //below line of code is to set on click lister for btnSave
        btnSave.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            //if we get id for our data then we are setting values to our edit text fields.
            edtTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            edtDesc.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            DatePicker.setText(intent.getStringExtra(EXTRA_DATE));
        }

        //below line of code is to set on click listener on addLocation
        addLocation.setOnClickListener(this);
        accessLocationService();

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSave) {
            if (checkValues()) {
                Intent data = new Intent();

                int id = getIntent().getIntExtra(EXTRA_ID, -1);
                if (id != -1) {
                    // below line for passing our id.
                    data.putExtra(EXTRA_ID, id);
                    jRepository.updateJournal(
                            new Journal(
                                    id,
                                    edtTitle.getText().toString(),
                                    edtDesc.getText().toString(),
                                    selectedImagePath,
                                    myLocation.getLatitude(),
                                    myLocation.getLongitude(),
                                    DatePicker.getText().toString()));
                    finish();
                    Intent intent = new Intent(issueJourneyActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(issueJourneyActivity.this, "Selected Journey has been updated.", Toast.LENGTH_SHORT).show();


                }
                else {
                    long check = jRepository.insertJournal(
                            new Journal(
                                    0,
                                    edtTitle.getText().toString(),
                                    edtDesc.getText().toString(),
                                    selectedImagePath,
                                    myLocation.getLatitude(),
                                    myLocation.getLongitude(),
                                    DatePicker.getText().toString()));
                    Log.e("new journey inserted", check + "");
                    Intent intent = new Intent(this, DashboardActivity.class);
                    finish();
                    startActivity(intent);

                    Toast.makeText(issueJourneyActivity.this, "New Journey has been added.", Toast.LENGTH_SHORT).show();
                }

                edtTitle.setText("");
                edtDesc.setText("");
                displayLocation.setText("Location");
                DatePicker.setText("Select Date");

            }
        } else if (v.getId() == R.id.addLocation) {
            if (myLocation != null)
                displayLocation.setText("lat: " + myLocation.getLatitude() + ", lng: " + myLocation.getLongitude());
        }
    }

    //below line of code is to create a function for checking the empty field
    private boolean checkValues() {
        String t = edtTitle.getText().toString();
        String d = edtDesc.getText().toString();
        if (t.isEmpty()) {
            edtTitle.setHint("Please enter journey title");
            edtTitle.setHintTextColor(ContextCompat.getColor(this, R.color.red));
            return false;
        } else if (d.isEmpty()) {
            edtDesc.setHint("Please enter the description");
            edtDesc.setHintTextColor(ContextCompat.getColor(this, R.color.red));
            return false;
        } else {
            return true;
        }

    }

    //below line of code is to access the loaction
    private void accessLocationService() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);

    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.myLocation = location;
        Log.e("current Location", location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    //below line of code is to get the uri of image
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    //below line of cade is to get the path from uri
    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver()
                .query(contentUri, null, null, null, null);
        if (cursor == null) {
            filePath = contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {


            Bitmap photo = (Bitmap) data.getExtras().get("data");
            loadImage.setImageBitmap(photo);
            loadImage.setVisibility(View.VISIBLE);

            Uri tempUri = getImageUri(getApplicationContext(), photo);
            Log.e("uri",tempUri.toString());

            selectedImagePath = getPathFromUri(tempUri);


        }
        else if (requestCode == 103 && resultCode == Activity.RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (null != selectedImageUri) {
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    loadImage.setImageBitmap(bitmap);
                    loadImage.setVisibility(View.VISIBLE);
                    selectedImagePath = getPathFromUri(selectedImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}

