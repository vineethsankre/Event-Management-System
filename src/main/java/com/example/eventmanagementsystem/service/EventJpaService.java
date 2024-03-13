package com.example.eventmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import com.example.eventmanagementsystem.model.*;
import com.example.eventmanagementsystem.repository.*;

@Service
public class EventJpaService implements EventRepository {
	@Autowired
	private EventJpaRepository eventJpaRepository;

	@Autowired
	private SponsorJpaRepository sponsorJpaRepository;

	@Override
	public ArrayList<Event> getAllEvents() {
		List<Event> eventList = eventJpaRepository.findAll();
		ArrayList<Event> events = new ArrayList<>(eventList);
		return events;
	}

	@Override
	public Event getEventById(int eventId) {
		try {
			Event event = eventJpaRepository.findById(eventId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			return event;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public Event addEvent(Event event) {
		List<Integer> sponsorIds = new ArrayList<>();
		for (Sponsor sponsor: event.getSponsors()){
			sponsorIds.add(sponsor.getSponsorId());
		}
		List<Sponsor> sponsors = sponsorJpaRepository.findAllById(sponsorIds);
		event.setSponsors(sponsors);
		for (Sponsor sponsor: sponsors){
			sponsor.getEvents().add(event);
		}
		Event savedEvent = eventJpaRepository.save(event);
		sponsorJpaRepository.saveAll(sponsors);
		return savedEvent;
		
	}

	@Override
	public Event updateEvent(int eventId, Event event) {
		try {
			Event new_event = eventJpaRepository.findById(eventId).get();
			if (event.getEventName() != null) {
				new_event.setEventName(event.getEventName());
			}
			if (event.getDate() != null) {
				new_event.setDate(event.getDate());
			}
			if (event.getSponsors() != null){
				List<Sponsor> sponsors = new_event.getSponsors();
				for (Sponsor sponsor: sponsors){
					sponsor.getEvents().remove(new_event);
				}
				sponsorJpaRepository.saveAll(sponsors);
				List<Integer> newSponsorIds = new ArrayList<>();
				for (Sponsor sponsor: event.getSponsors()){
					newSponsorIds.add(sponsor.getSponsorId());
				}
				List<Sponsor> newSponsors = sponsorJpaRepository.findAllById(newSponsorIds).get();
				for (Sponsor sponsor: newSponsors){
					sponsor.getEvents().add(new_event);
				}
				sponsorJpaRepository.saveAll(newSponsors);
				new_event.setSponsors(newSponsors);
			}
			return eventJpaRepository.save(new_event);;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public void deleteEvent(int eventId) {
		try {
			Event event = eventJpaRepository.findById(eventId).get();

			List<Sponsor> sponsors = event.getSponsors();
			for (Sponsor sponsor : sponsors) {
				sponsor.getEvents().remove(event);
			}
			sponsorJpaRepository.saveAll(sponsors);

			eventJpaRepository.deleteById(eventId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);

	}

	@Override
	public List<Sponsor> getEventSponsors(int eventId) {
		try {
			Event event = eventJpaRepository.findById(eventId).get();
			return event.getSponsors();

		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
