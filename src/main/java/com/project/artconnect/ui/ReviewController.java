package com.project.artconnect.ui;

import com.project.artconnect.model.Artwork;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Review;
import com.project.artconnect.persistence.JdbcReviewDao;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.service.CommunityService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewController {

    @FXML private ComboBox<CommunityMember> memberFilter;
    @FXML private TableView<Review> reviewTable;
    @FXML private TableColumn<Review, String> memberColumn;
    @FXML private TableColumn<Review, String> artworkColumn;
    @FXML private TableColumn<Review, Integer> ratingColumn;
    @FXML private TableColumn<Review, String> commentColumn;
    @FXML private TableColumn<Review, LocalDate> dateColumn;

    private final JdbcReviewDao reviewDao = new JdbcReviewDao();
    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final CommunityService communityService = ServiceProvider.getCommunityService();
    private final Map<Integer, String> memberNamesById = new HashMap<>();
    private final Map<Integer, String> artworkTitlesById = new HashMap<>();

    @FXML
    public void initialize() {
        memberColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(resolveMemberName(cell.getValue().getUser_id())));

        artworkColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(resolveArtworkTitle(cell.getValue().getArtwork_id())));

        ratingColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getRating()));
        ratingColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Integer rating, boolean empty) {
                super.updateItem(rating, empty);
                if (empty || rating == null) {
                    setText(null);
                    return;
                }
                int boundedRating = Math.max(0, Math.min(rating, 5));
                setText("★".repeat(boundedRating) + ".".repeat(5 - boundedRating));
            }
        });

        commentColumn.setCellValueFactory(new PropertyValueFactory<>("comment"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reviewDate"));

        memberFilter.setItems(FXCollections.observableArrayList(communityService.getAllMembers()));
        reloadReferenceData();
        refreshTable();
    }

    @FXML
    private void handleSearch() {
        CommunityMember selected = memberFilter.getValue();
        if (selected == null || selected.getUser_id() == 0) {
            refreshTable();
            return;
        }

        reloadReferenceData();
        List<Review> filtered = reviewDao.findByUserId(selected.getUser_id());
        reviewTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void handleReset() {
        memberFilter.setValue(null);
        refreshTable();
    }

    private void refreshTable() {
        reloadReferenceData();
        List<Review> all = reviewDao.findAll();
        reviewTable.setItems(FXCollections.observableArrayList(all));
    }

    private void reloadReferenceData() {
        memberNamesById.clear();
        for (CommunityMember member : communityService.getAllMembers()) {
            if (member.getUser_id() != 0) {
                memberNamesById.put(member.getUser_id(), member.getName());
            }
        }

        artworkTitlesById.clear();
        for (Artwork artwork : artworkService.getAllArtworks()) {
            if (artwork.getArtwork_id() != 0) {
                artworkTitlesById.put(artwork.getArtwork_id(), artwork.getTitle());
            }
        }
    }

    private String resolveMemberName(Integer userId) {
        if (userId == null || userId == 0) {
            return "-";
        }
        return memberNamesById.getOrDefault(userId, "User #" + userId);
    }

    private String resolveArtworkTitle(Integer artworkId) {
        if (artworkId == null || artworkId == 0) {
            return "-";
        }
        return artworkTitlesById.getOrDefault(artworkId, "Artwork #" + artworkId);
    }
}
