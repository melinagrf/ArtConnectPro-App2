package com.project.artconnect.service.impl;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import java.util.*;

public class InMemoryArtworkService implements ArtworkService {
    private final Map<String, Artwork> artworks = new LinkedHashMap<>();

    public InMemoryArtworkService() {
        // Data initialized after ArtistService is ready
    }

//    public void initData(ArtistService artistService) {
//        addArtwork("Mona Lisa", "Painting", 850000000.0,
//                artistService.getArtistByName("Leonardo Vinci").orElse(null));
//        addArtwork("The Thinker", 1904, "Sculpture", 15000000.0,
//                artistService.getArtistByName("Auguste Rodin").orElse(null));
//        addArtwork("Water Lilies", 1919, "Painting", 40000000.0,
//                artistService.getArtistByName("Claude Monet").orElse(null));
//        addArtwork("The Two Fridas", 1939, "Painting", 5000000.0,
//                artistService.getArtistByName("Frida Kahlo").orElse(null));
//        addArtwork("Monolith, The Face of Half Dome", 1927, "Photography", 100000.0,
//                artistService.getArtistByName("Ansel Adams").orElse(null));
//        addArtwork("The Last Supper", 1498, "Painting", 450000000.0,
//                artistService.getArtistByName("Leonardo Vinci").orElse(null));
//    }

    private void addArtwork(String title, String type, double price, int artist_id, int artwork_id) {
        if (artist_id == 0)
            return;
        Artwork a = new Artwork(artwork_id, title, artist_id, type, price);
        artworks.put(title, a);
    }

    @Override
    public List<Artwork> getAllArtworks() {
        return new ArrayList<>(artworks.values());
    }

    @Override
    public Optional<Artwork> getArtworkByTitle(String title) {
        return Optional.ofNullable(artworks.get(title));
    }

    @Override
    public List<Artwork> getArtworksByArtist(Artist artist) {
        return List.of();
    }

    @Override
    public void createArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void updateArtwork(Artwork artwork) {
        artworks.put(artwork.getTitle(), artwork);
    }

    @Override
    public void deleteArtwork(String title) {
        artworks.remove(title);
    }
}
