package com.ismt.journeyjournal.Journal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class JourneyViewModal extends AndroidViewModel {

    // below line is to create a new variable for journal repository.
    private JournalRepository jRepository;

    // below line is to create a variable for live
    // data where all the journals are present.
    private LiveData<List<Journal>> allJournals;

    // constructor for our view modal.
    public JourneyViewModal(@NonNull Application application) {
        super(application);
        jRepository = new JournalRepository(application);
        allJournals = jRepository.retrieveJournals();
    }

    // below method is used to insert the data to our repository.
    public void insert(Journal journal) {
        jRepository.insertJournal(journal);
    }

    // below method is used update data in our repository.
    public void update(Journal journal) {
        jRepository.updateJournal(journal);
    }

    // below method is used delete the data from our repository.
    public void delete(Journal journal) {
        jRepository.deleteJournal(journal);
    }

    // below method is used get all the courses in our list.
    public LiveData<List<Journal>> retrieveJournals() {
        return allJournals;
    }

}
