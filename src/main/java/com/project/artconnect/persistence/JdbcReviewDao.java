package com.project.artconnect.persistence;

import com.project.artconnect.config.DatabaseConfig;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcReviewDao {

    public List<Review> findByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT review_id, Date_, rating, comment, artwork_id, user_id FROM review WHERE user_id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) reviews.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur reviews user_id=" + userId, e);
        }
        return reviews;
    }

    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT review_id, Date_, rating, comment, artwork_id, user_id FROM review";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) reviews.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Erreur récupération reviews", e);
        }
        return reviews;
    }

    public void save(Review review) {
        String sql = "INSERT INTO review (Date_, rating, comment, artwork_id, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, review.getReviewDate() != null
                    ? Date.valueOf(review.getReviewDate())
                    : Date.valueOf(java.time.LocalDate.now()));
            stmt.setInt(2, review.getRating());
            stmt.setString(3, review.getComment());
            stmt.setInt(4, review.getArtwork_id());
            stmt.setInt(5, review.getUser_id());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erreur insertion review", e);
        }
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setReview_id(rs.getInt("review_id"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        Date d = rs.getDate("Date_");
        if (d != null) r.setReviewDate(d.toLocalDate());
        r.setArtwork_id(rs.getInt("artwork_id"));
        r.setUser_id(rs.getInt("user_id"));

        // Objets partiels pour l'affichage dans l'UI
        Artwork a = new Artwork();
        a.setArtwork_id(rs.getInt("artwork_id"));
        r.setArtwork_id(a);

        CommunityMember m = new CommunityMember();
        m.setUser_id(rs.getInt("user_id"));
        r.setUser_id(m.getUser_id());

        return r;
    }
}