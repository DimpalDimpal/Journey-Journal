package com.ismt.journeyjournal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ismt.journeyjournal.Journal.Journal;
import com.ismt.journeyjournal.Journal.JournalRVAdapter;
import com.ismt.journeyjournal.Journal.JournalRepository;
import com.ismt.journeyjournal.Journal.JourneyViewModal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class DashboardActivity extends AppCompatActivity {

    // below line of code is to create a variables for our recycler view.
    private Location myLocation;
    private RecyclerView journalRV;

    private TextView greeting;
    private static final int ADD_JOURNEY_REQUEST = 1;
    private static final int EDIT_JOURNEY_REQUEST = 2;
    private JourneyViewModal journeyViewModal;
    private FloatingActionButton addJourney;
    private ImageView logOut, aboutUs;
    private SharedPreferences sharedPreferences;

    private GoogleSignInOptions gso;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // below line of code is to initialize our variable for our recycler view and fab.
        journalRV = findViewById(R.id.journalRV);
        addJourney = findViewById(R.id.addJourney);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        aboutUs = findViewById(R.id.aboutUs);

        //below line of code is to set on click listener for aboutUs
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        //below line of code is to get current date and time in the dashboard
        TextView date=findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd \r\n HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        date.setText(currentDateandTime);

        sharedPreferences = getSharedPreferences("journal_prefs", Context.MODE_PRIVATE);

        greeting = findViewById(R.id.greeting);

        //below line of code is to display of the user
        String userFullName = sharedPreferences.getString("user", "");

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);

        //below line of code is to check if the user if logged in through their google login credentials or, not
        if (acct != null) {
            String personName = acct.getDisplayName();
            greeting.setText("Welcome, " + personName);
        }
        else{
            greeting.setText("Welcome, " + userFullName);
        }

        // below line of code is to add on click listener for floating action button.
        addJourney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, issueJourneyActivity.class);
                startActivity(intent);
            }
        });

        //below line of code is to initialize variable for logout button
        logOut = findViewById(R.id.logOut);

        // below line of code is to add on click listener for logout button.
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(DashboardActivity.this);
                builder.setMessage("Do you want to exit?");

                //below line of code is for exiting from the app
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(DashboardActivity.this);
                        if (acct != null) {
                            signOut();
                        }
                        else{
                            finish();
                            Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
                            startActivity(intent);
                            sharedPreferences.edit().remove("user").commit();
                            finish();
                        }




                    }
                });

                //below line of code is for cancel the exiting
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

                AlertDialog alert=builder.create();
                alert.show();

            }
        });

        // below line of code is to set layout manager to our adapter class.
        journalRV.setLayoutManager(new LinearLayoutManager(this));
        journalRV.setHasFixedSize(false);

        // below line of code is to initialize adapter for recycler view.
        final JournalRVAdapter adapter = new JournalRVAdapter();

        // setting adapter class for recycler view.
        journalRV.setAdapter(adapter);

        // passing a data from view modal.
        journeyViewModal = ViewModelProviders.of(this).get(JourneyViewModal.class);

        // below line is use to get all the journals from view modal.
        journeyViewModal.retrieveJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(List<Journal> journals) {
                // when the data is changed in our models we are
                // adding that list to our adapter class.
                adapter.submitList(journals);

            }
        });

        // below method is use to add swipe to delete method for item of recycler view.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // on recycler view item swiped then we are deleting the item of our recycler view.

                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(DashboardActivity.this)
                        // to set title, message, and icon
                        .setTitle("Delete")
                        .setMessage("Are you sure want to Delete?")
                        .setIcon(R.drawable.ic_red_delete)

                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                //deleting code

                                journeyViewModal.delete(adapter.getJournalAt(viewHolder.getAdapterPosition()));
                                Toast.makeText(DashboardActivity.this, "Selected Journal deleted", Toast.LENGTH_SHORT).show();

                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                overridePendingTransition(0, 0);
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                                dialog.dismiss();


                            }
                        })
                        .create();
                myQuittingDialogBox.show();


            }
            @Override
            public void onChildDraw (Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive){

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                        .addBackgroundColor(ContextCompat.getColor(DashboardActivity.this, R.color.red))
                        .addSwipeRightLabel("Delete")
                        .addSwipeLeftLabel("Delete")
                        .setSwipeLeftLabelColor(R.color.white)
                        .setSwipeRightLabelColor(R.color.white)
                        .addActionIcon(R.drawable.ic_delete)

                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).
                // below line is use to attach this to recycler view.
                        attachToRecyclerView(journalRV);

        // below line is use to set item click listener for our item of recycler view.
        adapter.setOnItemClickListener(new JournalRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Journal journal) {

                // after clicking on item of recycler view
                // we are opening a new activity and passing
                // a data to our activity.
                Intent intent = new Intent(DashboardActivity.this, issueJourneyActivity.class);
                intent.putExtra(issueJourneyActivity.EXTRA_ID, journal.getId());
                intent.putExtra(issueJourneyActivity.EXTRA_TITLE, journal.getTitle());
                intent.putExtra(issueJourneyActivity.EXTRA_DESCRIPTION, journal.getText());
//                intent.putExtra(issueJourneyActivity.EXTRA_LAT, myLocation.getLatitude());
//                intent.putExtra(issueJourneyActivity.EXTRA_LNG, myLocation.getLongitude());
                intent.putExtra(issueJourneyActivity.EXTRA_DATE, journal.getDate());
                intent.putExtra(issueJourneyActivity.EXTRA_IMG, journal.getImage());
                // below line is to start a new activity and
                // adding a edit course constant.
                startActivityForResult(intent, EDIT_JOURNEY_REQUEST);
            }
        });


    }

    //beow line of code is to create method to get sign out
    private void signOut() {
        finish();
        Intent intent = new Intent(DashboardActivity.this,MainActivity.class);
        startActivity(intent);
        finish();

        mGoogleSignInClient.signOut();
        Toast.makeText(this, "Signed out successfully!!",Toast.LENGTH_SHORT).show();

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();

                Toast.makeText(this, personName + "," + personGivenName + "," +
                        personFamilyName + "," + personEmail,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
            }




        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            Log.d("message",e.toString());
        }
    }

    @Override
    protected void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_JOURNEY_REQUEST && resultCode == RESULT_OK) {
            String jTitle = data.getStringExtra(issueJourneyActivity.EXTRA_TITLE);
            String jDesc = data.getStringExtra(issueJourneyActivity.EXTRA_DESCRIPTION);
            String img = data.getStringExtra(issueJourneyActivity.EXTRA_IMG);
            String jDate = data.getStringExtra(issueJourneyActivity.EXTRA_DATE);
            Journal journal = new Journal(0, jTitle, jDesc, img, 0, 0, jDate);
            journeyViewModal.insert(journal);
        } else if (requestCode == EDIT_JOURNEY_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(issueJourneyActivity.EXTRA_ID, -1);
            if (id == -1) {
                return;
            }
            String jTitle = data.getStringExtra(issueJourneyActivity.EXTRA_TITLE);
            String jDesc = data.getStringExtra(issueJourneyActivity.EXTRA_DESCRIPTION);
            String img = data.getStringExtra(issueJourneyActivity.EXTRA_IMG);
            String jDate = data.getStringExtra(issueJourneyActivity.EXTRA_DATE);
            Journal journal = new Journal(0, jTitle, jDesc, img, 0, 0, jDate);
            journal.setId(id);
            JournalRepository.updateJournal(journal);
        }


    }



}
