package com.project.artconnect.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Gallery {
    private Integer Gallery_id;
    private String name;
    private String address;
    private List<Exhibition> exhibitions = new ArrayList<>();

    public Gallery(){

    }
    public Gallery( Integer gallery_id, String name, String address) {
        this.name = name;
        this.Gallery_id = gallery_id;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return name;
    }

    public void setGallery_id(Integer id) {
        this.Gallery_id = id;
    }

    public Object getId() {
        return this.Gallery_id;
    }

    public void setId(int galleryId) {
        this.Gallery_id = galleryId;
    }

    public Collection<? extends Exhibition> getExhibitions() {
        return this.exhibitions;

    }

    public void setExhibitions(List<Exhibition> exhForGallery) {
        this.exhibitions = exhForGallery;
    }
}
