package com.project.artconnect.ui;

import com.project.artconnect.model.Artist;
import com.project.artconnect.model.Artwork;
import com.project.artconnect.service.ArtistService;
import com.project.artconnect.service.ArtworkService;
import com.project.artconnect.util.ServiceProvider;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.HashMap;
import java.util.Map;

public class ArtworkController {
    @FXML
    private TableView<Artwork> artworkTable;
    @FXML
    private TableColumn<Artwork, String> titleColumn;
    @FXML
    private TableColumn<Artwork, String> typeColumn;
    @FXML
    private TableColumn<Artwork, Double> priceColumn;
    @FXML
    private TableColumn<Artwork, String> statusColumn;
    @FXML
    private TableColumn<Artwork, String> artistColumn;

    private final ArtistService artistService = ServiceProvider.getArtistService();
    private final ArtworkService artworkService = ServiceProvider.getArtworkService();
    private final Map<Integer, String> artistNamesById = new HashMap<>();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        reloadArtistNames();
        artistColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(resolveArtistName(cellData.getValue().getArtist_id()))
        );

        artworkTable.setItems(FXCollections.observableArrayList(artworkService.getAllArtworks()));
    }

    private void reloadArtistNames() {
        artistNamesById.clear();
        for (Artist artist : artistService.getAllArtists()) {
            try {
                artistNamesById.put(Integer.parseInt(artist.getId()), artist.getName());
            } catch (NumberFormatException ignored) {
            }
        }
    }

    private String resolveArtistName(Integer artistId) {
        if (artistId == null || artistId == 0) {
            return "-";
        }
        return artistNamesById.getOrDefault(artistId, String.valueOf(artistId));
    }
}
