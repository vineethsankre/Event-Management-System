package com.example.eventmanagementsystem.repository;

import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Sponsor;

import java.util.*;

public interface SponsorRepository {
    ArrayList<Sponsor> getAllSponsors();

    Sponsor getSponsorById(int sponsorId);

    Sponsor addSponsor(Sponsor sponsor);

    Sponsor updateSponsor(int sponsorId, Sponsor sponsor);

    void deleteSponsor(int sponsorId);

    List<Event> getSponsorEvents(int sponsorId);
}
