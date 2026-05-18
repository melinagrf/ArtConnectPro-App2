package com.project.artconnect.model;

import java.time.LocalDate;

public class Review {
    private Integer review_id;
    private Integer artwork_id;
    private Integer user_id;
    private int rating; // 1-5
    private String comment;
    private LocalDate reviewDate;
    private CommunityMember member; // Nouvel attribut utilisé pour l'affichage

    public CommunityMember getMember() { return member; }
    public void setMember(CommunityMember member) { this.member = member; }

    public Review() {
    }

    public Review(int review_id, int artwork_id, int rating, String comment) {
        this.review_id = review_id;
        this.artwork_id = artwork_id;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = LocalDate.now();
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int reviewer) {
        this.review_id = reviewer;
    }

    public int getArtwork_id() {
        return this.artwork_id;
    }

    public void setArtwork_id(int artwork_id) {
        this.artwork_id = artwork_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    public void setArtwork_id(Artwork a) {

    }


    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }
}
