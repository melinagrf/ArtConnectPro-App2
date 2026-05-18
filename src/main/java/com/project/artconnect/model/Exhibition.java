package com.project.artconnect.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Exhibition {
    private int exhibition_id;
    private String entitled;
    private LocalDate startDate;
    private int gallery_id;
    private String theme;

    public Exhibition() {
    }

    public Exhibition(int id, String title, LocalDate startDate, String theme, int gallery) {
        this.exhibition_id = id;
        this.entitled = title;
        this.startDate = startDate;
        this.gallery_id = gallery;
        this.theme = theme;
    }


    public String getEntitled() {
        return this.entitled;
    }

    public void setEntitled(String title) {
        this.entitled = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }



    public int getGallery() {
        return gallery_id;
    }

    public void setGallery_id(int gallery_id) {
        this.gallery_id = gallery_id;
    }


    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }


    public String toString() {
        return entitled;
    }

    public int getExhibition_id() {
        return this.exhibition_id;
    }

    public void setExhibition_id(int id) {
        this.exhibition_id = id;
    }


}
