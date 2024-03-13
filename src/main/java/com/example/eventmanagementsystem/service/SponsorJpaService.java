package com.example.eventmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import com.example.eventmanagementsystem.model.*;
import com.example.eventmanagementsystem.repository.*;

@Service
public class SponsorJpaService implements SponsorRepository {
	@Autowired
	private SponsorJpaRepository sponsorJpaRepository;

	@Autowired
	private EventJpaRepository eventJpaRepository;

	@Override
	public ArrayList<Sponsor> getAllSponsors() {
		List<Sponsor> sponsorList = sponsorJpaRepository.findAll();
		ArrayList<Sponsor> sponsors = new ArrayList<>(sponsorList);
		return sponsors;
	}

	@Override
	public Sponsor getSponsorById(int sponsorId) {
		try {
			Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
			return sponsor;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public Sponsor addSponsor(Sponsor sponsor) {
		List<Integer> eventIds = new ArrayList<>();
		for (Event event : sponsor.getEvents()) {
			eventIds.add(event.getEventId());
		}
		List<Event> events = eventJpaRepository.findAllById(eventIds);

	        if (events.size() != eventIds.size()) {
	            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
	        }
	        sponsor.setEvents(events);
	
	        return sponsorJpaRepository.save(sponsor);
	}

	@Override
	public Sponsor updateSponsor(int sponsorId, Sponsor sponsor) {
		try {
			Sponsor existingSponsor = sponsorJpaRepository.findById(sponsorId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			for (Event event : sponsor.getEvents()) {
				Event existingEvent = eventJpaRepository.findById(event.getEventId()).orElse(null);
				if (existingEvent == null) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
				}
			}
			if (sponsor.getSponsorName() != null) {
				existingSponsor.setSponsorName(sponsor.getSponsorName());
			}
			if (sponsor.getIndustry() != null) {
				existingSponsor.setIndustry(sponsor.getIndustry());
			}
			sponsorJpaRepository.save(existingSponsor);

			return existingSponsor;
		} catch (ResponseStatusException e) {
			throw e;
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	public void deleteSponsor(int sponsorId) {
		try {
			sponsorJpaRepository.deleteById(sponsorId);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		throw new ResponseStatusException(HttpStatus.NO_CONTENT);

	}

	@Override
	public List<Event> getSponsorEvents(int sponsorId) {
		try {
			Sponsor sponsor = sponsorJpaRepository.findById(sponsorId).get();
			return sponsor.getEvents();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}
}
