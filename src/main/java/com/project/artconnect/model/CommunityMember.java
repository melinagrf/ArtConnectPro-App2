package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.List;

public class CommunityMember {
    private String name;
    private String email;
    private String city;
    private Integer user_id;

    public CommunityMember() {
    }

    public CommunityMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }




    @Override
    public String toString() {
        return name;
    }

    public int getUser_id() { return this.user_id; }
    public void setUser_id(int id) { this.user_id = id; }


}
