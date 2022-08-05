package com.ismt.journeyjournal.Journal;
import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;
//below line of code is for creating the journal dao for getting live data from journal table, inserting, updating and deleting.
@Dao
public interface JournalDAO {
    @Insert
    long insertJournal(Journal journal);

    @Query("select * from journal")
    LiveData<List<Journal>> retrieveJournals();

    @Update
    void updateJournal(Journal journal);

    @Delete
    void deleteJournal(Journal journal);
}