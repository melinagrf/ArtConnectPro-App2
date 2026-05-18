package com.project.artconnect.persistence;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.ExhibitionDao;
import com.project.artconnect.model.Exhibition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcExhibitionDao implements ExhibitionDao {

    @Override
    public List<Exhibition> findAll() {
        List<Exhibition> exhibitions = new ArrayList<>();
        String sql = "SELECT exhibition_id, Theme, Entitled, Start_date, Gallery_id FROM exhibition";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) exhibitions.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération expositions", e);
        }
        return exhibitions;
    }

    @Override
    public void save(Exhibition exhibition) {
        String sql = "INSERT INTO exhibition (Theme, Entitled, Start_date, Gallery_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, exhibition.getTheme());
            stmt.setString(2, exhibition.getEntitled());
            stmt.setDate(3, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            stmt.setInt(4, exhibition.getGallery());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion exposition : " + exhibition.getEntitled(), e);
        }
    }

    @Override
    public void update(Exhibition exhibition) {
        String sql = "UPDATE exhibition SET Theme=?, Entitled=?, Start_date=?, Gallery_id=? WHERE exhibition_id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, exhibition.getTheme());
            stmt.setString(2, exhibition.getEntitled());
            stmt.setDate(3, exhibition.getStartDate() != null ? Date.valueOf(exhibition.getStartDate()) : null);
            stmt.setInt(4, exhibition.getGallery());
            stmt.setInt(5, exhibition.getExhibition_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur mise à jour exposition : " + exhibition.getEntitled(), e);
        }
    }

    @Override
    public void delete(String title) {
        String sql = "DELETE FROM exhibition WHERE Entitled = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur suppression exposition : " + title, e);
        }
    }

    private Exhibition mapRow(ResultSet rs) throws SQLException {
        Exhibition e = new Exhibition();
        e.setExhibition_id(rs.getInt("exhibition_id"));
        e.setEntitled(rs.getString("Entitled"));
        e.setTheme(rs.getString("Theme"));
        Date startDate = rs.getDate("Start_date");
        if (startDate != null) e.setStartDate(startDate.toLocalDate());
        e.setGallery_id(rs.getInt("Gallery_id"));
        return e;
    }
}