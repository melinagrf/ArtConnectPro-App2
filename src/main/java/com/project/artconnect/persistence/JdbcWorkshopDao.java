package com.project.artconnect.persistence;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Workshop;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcWorkshopDao implements WorkshopDao {

    @Override
    public Optional<Workshop> findById(Long id) {
        String sql = "SELECT workshop_id, Title, date_, price, level, artist_id FROM workshop WHERE workshop_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id.intValue());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur atelier id=" + id, e);
        }
        return Optional.empty();
    }

    @Override
    public List<Workshop> findAll() {
        List<Workshop> workshops = new ArrayList<>();
        String sql = "SELECT workshop_id, Title, date_, price, level, artist_id FROM workshop";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) workshops.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération ateliers", e);
        }
        return workshops;
    }

    private Workshop mapRow(ResultSet rs) throws SQLException {
        Workshop w = new Workshop();
        w.setWorkshop_Id(String.valueOf(rs.getInt("workshop_id")));
        w.setTitle(rs.getString("Title"));
        Timestamp ts = rs.getTimestamp("date_");
        if (ts != null) w.setDate(ts.toLocalDateTime());
        w.setPrice(rs.getDouble("price"));
        w.setLevel(rs.getString("level"));
        return w;
    }
}