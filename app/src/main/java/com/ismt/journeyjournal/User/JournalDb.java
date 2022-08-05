package com.ismt.journeyjournal.User;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.ismt.journeyjournal.Journal.Journal;
import com.ismt.journeyjournal.Journal.JournalDAO;

// adding annotation for our database entities and db version.
@Database( entities = {User.class, Journal.class},exportSchema = false,version=2)
public abstract class JournalDb extends RoomDatabase {
    //below line is to set the name for our database
    public static final String databaseName="JournalDb";

    // below line is to create instance
    // for our database class.
    public static JournalDb instance;

    // on below line we are getting instance for our database.
    public static synchronized JournalDb getDBInstance(Context context) {

        //to check if the instance is null or not.
        if(instance == null) {

            // if the instance is null we are creating a new instance
            instance = Room.databaseBuilder(context.getApplicationContext(), JournalDb.class, databaseName)
                    .allowMainThreadQueries().addCallback(roomCallback).build();

        }

        // after creating an instance we are returning our instance
        return instance;
    }

    // below line is to create abstract variable for dao.
    public abstract UserDAO userDao();
    public abstract JournalDAO journalDAO();

    // below line is to create a callback for our room database.
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // this method is called when database is created
            // and below line is to populate our data.
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    // we are creating an async task class to perform task in background.
    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        PopulateDbAsyncTask(JournalDb instance) {
            JournalDAO jdao = instance.journalDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
