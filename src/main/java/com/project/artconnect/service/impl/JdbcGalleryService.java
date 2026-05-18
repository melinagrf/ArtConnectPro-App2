package com.project.artconnect.service.impl;

import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.dao.GalleryDao;
import com.project.artconnect.model.Exhibition;
import com.project.artconnect.model.Gallery;
import com.project.artconnect.persistence.JdbcExhibitionDao;
import com.project.artconnect.persistence.JdbcGalleryDao;
import com.project.artconnect.service.GalleryService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcGalleryService implements GalleryService {

    private final GalleryDao galleryDao = new JdbcGalleryDao();
    private final ExhibitionDao exhibitionDao = new JdbcExhibitionDao();

    @Override
    public List<Gallery> getAllGalleries() {
        List<Gallery> galleries = galleryDao.findAll();
        List<Exhibition> allExhibitions = exhibitionDao.findAll();

        for (Gallery gallery : galleries) {
            List<Exhibition> exhForGallery = allExhibitions.stream()
                    .filter(e -> String.valueOf(gallery.getId()).equals(e.getGallery()))
                    .collect(Collectors.toList());
            gallery.setExhibitions(exhForGallery);
        }
        return galleries;
    }

    @Override
    public Optional<Gallery> getGalleryByName(String name) {
        return getAllGalleries().stream()
                .filter(g -> g.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    @Override
    public List<Exhibition> getExhibitionsByGallery(Gallery gallery) {
        return exhibitionDao.findAll().stream()
                .filter(e -> String.valueOf(gallery.getId()).equals(e.getGallery()))
                .collect(Collectors.toList());
    }
}