package com.example.eventmanagementsystem.repository;

import java.util.List;
import java.util.ArrayList;
import com.example.eventmanagementsystem.model.Event;
import com.example.eventmanagementsystem.model.Sponsor;

public interface EventRepository {
    ArrayList<Event> getAllEvents();

    Event getEventById(int eventId);

    Event addEvent(Event event);

    Event updateEvent(int eventId, Event event);

    void deleteEvent(int eventId);

    List<Sponsor> getEventSponsors(int eventId);

}
