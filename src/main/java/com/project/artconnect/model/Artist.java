package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Artist entity representing a creator in the community.
 */
public class Artist {
    private String artist_id;
    private String name;
    private Integer birthYear;
    private String email;
    private String city;


    public Artist() {
    }

    public Artist(String artist_id, String name, Integer birthYear, String email, String city) {
        this.artist_id = artist_id;
        this.name = name;
        this.birthYear = birthYear;
        this.city = city;
        this.email = email;
    }

    // Getters and Setters
    public String getId() {return artist_id;}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public void setArtist_id(String artist_id) {
        this.artist_id = artist_id;
    }

    public String toString() {
        return name;
    }

    public String getEmail() {return this.email;}
    public void setEmail(String email){this.email = email; }

}
