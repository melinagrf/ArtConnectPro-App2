package com.project.artconnect.persistence;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.ArtworkDao;
import com.project.artconnect.model.Artwork;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtworkDao implements ArtworkDao {

    @Override
    public List<Artwork> findAll() {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT artwork_id, Title, Type, Status, Price, artist_id FROM artwork";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) artworks.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération œuvres", e);
        }
        return artworks;
    }

    @Override
    public List<Artwork> findByArtistName(String artistName) {
        List<Artwork> artworks = new ArrayList<>();
        String sql = "SELECT aw.artwork_id, aw.Title, aw.Type, aw.Status, aw.Price, aw.artist_id " +
                "FROM artwork aw JOIN artist a ON aw.artist_id = a.artist_id WHERE a.name = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artistName);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) artworks.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur recherche œuvres artiste : " + artistName, e);
        }
        return artworks;
    }

    @Override
    public void save(Artwork artwork) {
        String sql = "INSERT INTO artwork (Title, Type, Status, Price, artist_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getType());
            stmt.setString(3, artwork.getStatus() != null ? artwork.getStatus().name() : null);
            stmt.setDouble(4, artwork.getPrice());
            stmt.setObject(5, artwork.getArtist_id() != null ? artwork.getArtist_id() : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion œuvre : " + artwork.getTitle(), e);
        }
    }

    @Override
    public void update(Artwork artwork) {
        String sql = "UPDATE artwork SET Title=?, Type=?, Status=?, Price=?, artist_id=? WHERE artwork_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artwork.getTitle());
            stmt.setString(2, artwork.getType());
            stmt.setString(3, artwork.getStatus() != null ? artwork.getStatus().name() : null);
            stmt.setDouble(4, artwork.getPrice());
            stmt.setObject(5, artwork.getArtist_id() != null ? artwork.getArtist_id() : null);
            stmt.setInt(6, artwork.getArtwork_id());
            int rows = stmt.executeUpdate();
            if (rows == 0) throw new RuntimeException("Œuvre introuvable id=" + artwork.getArtwork_id());
        } catch (SQLException e) {
            throw new RuntimeException("Erreur mise à jour œuvre : " + artwork.getTitle(), e);
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM artwork WHERE Title = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression œuvre : " + title, e);
        }
    }

    private Artwork mapRow(ResultSet rs) throws SQLException {
        Artwork a = new Artwork();
        a.setArtwork_id(rs.getInt("artwork_id"));
        a.setTitle(rs.getString("Title"));
        a.setType(rs.getString("Type"));
        a.setPrice(rs.getDouble("Price"));
        a.setArtist_id(rs.getInt("artist_id"));
        String statusStr = rs.getString("Status");
        if (statusStr != null) {
            switch (statusStr.trim().toUpperCase()) {
                case "AVAILABLE":
                case "FOR_SALE":
                    a.setStatus(Artwork.Status.FOR_SALE);
                    break;
                case "SOLD":
                    a.setStatus(Artwork.Status.SOLD);
                    break;
                case "EXHIBITED":
                    a.setStatus(Artwork.Status.EXHIBITED);
                    break;
                default:
                    a.setStatus(null);
            }
        }
        return a;
    }
}
