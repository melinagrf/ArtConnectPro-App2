package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Artwork entity representing a piece created by an artist.
 */
public class Artwork {
    private String title;
    private String type; // painting, sculpture, etc.
    private double price;
    private Status status; // FOR_SALE, SOLD, EXHIBITED
    private int artist_id;
    private int artwork_id;


    public enum Status {
        FOR_SALE, SOLD, EXHIBITED
    }

    public Artwork() {
    }

    public Artwork(Integer artwork_id, String title, int artist_id, String type, double price) {
        this.artist_id = artist_id;
        this.title = title;
        this.type = type;
        this.price = price;
        this.status = Status.FOR_SALE;
        this.artwork_id = artwork_id;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Integer getArtist_id() {
        return this.artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }


    public Integer getArtwork_id() { return this.artwork_id; }
    public void setArtwork_id(int id) { this.artwork_id = id; }

    @Override
    public String toString() {
        return title;
    }
}
