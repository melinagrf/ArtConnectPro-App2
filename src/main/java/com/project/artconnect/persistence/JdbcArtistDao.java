package com.project.artconnect.persistence;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.ArtistDao;
import com.project.artconnect.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcArtistDao implements ArtistDao {

    @Override
    public List<Artist> findAll() {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT artist_id, name, email, birthyear, city FROM artist";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                artists.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la récupération des artistes", e);
        }
        return artists;
    }

    @Override
    public List<Artist> findByCity(String city) {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT artist_id, name, email, birthyear, city FROM artist WHERE city = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, city);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    artists.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la recherche des artistes par ville : " + city, e);
        }
        return artists;
    }

    @Override
    public void save(Artist artist) {
        String sql = "INSERT INTO artist (name, email, birthyear, city) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getEmail());
            stmt.setObject(3, artist.getBirthYear());
            stmt.setString(4, artist.getCity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de l'insertion de l'artiste : " + artist.getName(), e);
        }
    }

    @Override
    public void update(Artist artist) {
        String sql = "UPDATE artist SET name = ?, email = ?, birthyear = ?, city = ? WHERE artist_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artist.getName());
            stmt.setString(2, artist.getEmail());
            stmt.setObject(3, artist.getBirthYear());
            stmt.setString(4, artist.getCity());
            stmt.setInt(5, Integer.parseInt(artist.getId()));
            int rows = stmt.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("Aucun artiste trouvé avec l'id : " + artist.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la mise à jour de l'artiste : " + artist.getName(), e);
        }
    }

    @Override
    public void delete(String artistName) {
        String sql = "DELETE FROM artist WHERE name = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, artistName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur lors de la suppression de l'artiste : " + artistName, e);
        }
    }

    private Artist mapRow(ResultSet rs) throws SQLException {
        Artist a = new Artist();
        a.setArtist_id(rs.getString("artist_id"));
        a.setName(rs.getString("name"));
        a.setEmail(rs.getString("email"));
        a.setBirthYear(rs.getInt("birthyear"));
        a.setCity(rs.getString("city"));
        return a;
    }
}