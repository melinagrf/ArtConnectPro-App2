package com.project.artconnect.service.impl;

import com.project.artconnect.dao.WorkshopDao;
import com.project.artconnect.model.Booking;
import com.project.artconnect.model.CommunityMember;
import com.project.artconnect.model.Workshop;
import com.project.artconnect.persistence.JdbcWorkshopDao;
import com.project.artconnect.service.WorkshopService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JdbcWorkshopService implements WorkshopService {

    private final WorkshopDao workshopDao = new JdbcWorkshopDao();

    @Override
    public List<Workshop> getAllWorkshops() {
        return workshopDao.findAll();
    }

    @Override
    public Optional<Workshop> getWorkshopByTitle(String title) {
        return workshopDao.findAll().stream()
                .filter(w -> w.getTitle().equalsIgnoreCase(title))
                .findFirst();
    }

    @Override
    public void bookWorkshop(Workshop workshop, CommunityMember member) {
        // La réservation est gérée en base via une table de jointure booking.
        // À implémenter avec un JdbcBookingDao si la table existe dans votre schéma.
        throw new UnsupportedOperationException("bookWorkshop: implémenter JdbcBookingDao");
    }

    @Override
    public List<Booking> getBookingsByMember(CommunityMember member) {
        // À implémenter avec un JdbcBookingDao si la table existe dans votre schéma.
        throw new UnsupportedOperationException("getBookingsByMember: implémenter JdbcBookingDao");
    }
}
