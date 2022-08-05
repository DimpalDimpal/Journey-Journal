package com.ismt.journeyjournal.Journal;


import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;

import com.ismt.journeyjournal.User.JournalDb;

import java.util.List;
public class JournalRepository {

    //below line of code is for creating a variable
    private final JournalDb db;
    private static JournalDAO journalDao;

    //below line of code is for creating a constructor class for journal repository
    public JournalRepository(Context context){
        this.db = JournalDb.getDBInstance(context);
        journalDao = db.journalDAO();
    }

    //below line of code is for inserting the journal to the journal table
    public long insertJournal(Journal journal){
        return journalDao.insertJournal(journal);
    }

    //below line of code is for getting the live data of journals from journal table
    LiveData<List<Journal>> retrieveJournals(){
        return journalDao.retrieveJournals();
    }

    //below line of code is for updating the journal to the database
    public static void updateJournal(Journal journal) {
        new UpdateCourseAsyncTask(journalDao).execute(journal);
    }

    //below line of code is for deleting the journal from the database
    public void deleteJournal(Journal journal) {
        new DeleteCourseAsyncTask(journalDao).execute(journal);
    }

    // below line of code is for creating a async task method to update the journal
    private static class UpdateCourseAsyncTask extends AsyncTask<Journal, Void, Void> {
        private JournalDAO journalDAO;
        private UpdateCourseAsyncTask(JournalDAO journalDAO) {
            this.journalDAO = journalDAO;
        }

        @Override
        protected Void doInBackground(Journal... journals) {
            // below line is use to update
            // our journal modal in dao.
            journalDAO.updateJournal(journals[0]);
            return null;
        }
    }

    // below line of code is for creating a async task method to delete the journal
    private static class DeleteCourseAsyncTask extends AsyncTask<Journal, Void, Void> {
        private JournalDAO journalDAO;
        private DeleteCourseAsyncTask(JournalDAO journalDAO) {
            this.journalDAO = journalDAO;
        }

        @Override
        protected Void doInBackground(Journal... journals) {
            // below line is use to delete
            // our journal modal in dao.
            journalDAO.deleteJournal(journals[0]);
            return null;
        }
    }
}