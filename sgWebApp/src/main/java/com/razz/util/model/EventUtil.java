package com.razz.util.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.razz.model.Event;

public class EventUtil {

	public static void filterPublished(List<Event> events) {
		final String key = "#publish";
		final Iterator<Event> iter = events.iterator();
		while(iter.hasNext()) {
			final Event event = iter.next();
			final String desc = event.getDescription();
			if(desc == null || !desc.trim().toLowerCase().startsWith(key))
				iter.remove();
			else
				event.setDescription( desc.replace(key, "").trim() );
		}
	}
	
	public static void filterUpcomingDates(List<Event> events) {
		final long dtg = System.currentTimeMillis();
		filterGreaterThanOrEqualDtg(events, dtg);
	}
	
	public static void filterGreaterThanOrEqualDtg(List<Event> events, long dtg) {
		final Iterator<Event> iter = events.iterator();
		while(iter.hasNext()) {
			final Event event = iter.next();
			if(event.getDate() < dtg)
				iter.remove();
		}
	}
	
	public static void sortByDtgAscending(List<Event> events) {
		// sort by DTG
		Collections.sort(events, new Comparator<Event>() {
			public int compare(com.razz.model.Event e1, com.razz.model.Event e2) {
				return Long.compare(e1.getDate(), e2.getDate());
			}
		});
	}
	
}
