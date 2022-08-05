package com.ismt.journeyjournal.Journal;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//creating the journal entity on our database
@Entity(tableName = "journal")
public class Journal {

    @PrimaryKey (autoGenerate = true)
    @NonNull

    private int id;
    private String title;

    private String text;
    private String image;
    private double lat;
    private double lng;
    private String date;

    //below line of code is for creating the constructor class for our journal entity.
    public Journal(int id, String title, String text, String image, double lat, double lng, String date) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    //below line of code is for getting
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getImage() {
        return image;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getDate() {
        return date;
    }

    //below line of code is for getting
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
