package com.razz.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:google.properties")
public class GoogleCalendarService {

	@Autowired
	private LoggerService logger;

	@Value("${calendarId}")
	private String calendarId;

	private Calendar calendarService;

	void init() throws Exception {
		// Build a new authorized API client service.
		// Note: Do not confuse this class with the
		// com.google.api.services.calendar.model.Calendar class.
		calendarService = getCalendarService();
	}

	Calendar getCalendarService() throws Exception {
		//
		Credential credential = null;
		
		//
		final String gooAppCredKey = "GOOGLE_APPLICATION_CREDENTIALS";
		final String gooAppCredValue = System.getProperty(gooAppCredKey);
		if (gooAppCredValue != null) {
			logger.debug(getClass(), "%s=%s", gooAppCredKey, gooAppCredValue);
			if (gooAppCredValue.trim().length() <= 0 || !Paths.get(gooAppCredValue).toFile().exists()) {
				final String error = String.format("%s is incorrect. %s=%s", gooAppCredKey, gooAppCredKey,
						gooAppCredValue);
				throw new FileNotFoundException(error);
			}
			try(FileInputStream fis = new FileInputStream(gooAppCredValue)) {
				credential = GoogleCredential.fromStream(fis).createScoped(
						Collections.singleton(CalendarScopes.CALENDAR_READONLY));
			}
		}
		
		//
		else {
			logger.debug(getClass(), "loading default credentials...");
			credential =  GoogleCredential.getApplicationDefault();
		}

		//
		final HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
		final JsonFactory jSonFactory = JacksonFactory.getDefaultInstance();
		final String APPLICATION_NAME = GoogleCalendarService.class.getSimpleName();
		return new Calendar.Builder(HTTP_TRANSPORT, jSonFactory, credential).setApplicationName(APPLICATION_NAME).build();
	}
	
	public List<com.razz.model.Event> getEvents() throws Exception {
		return getEvents(0);
	}

	public List<com.razz.model.Event> getEventsStartingToday() throws Exception {
		final LocalDateTime now = LocalDateTime.now();
		final int year = now.getYear();
		final int month = now.getMonthValue();
		final int dayOfMonth = now.getDayOfMonth();
		final LocalDate today = LocalDate.of(year, month, dayOfMonth);
		final ZoneId zoneId = ZoneId.systemDefault();
		final long fromDate = today.atStartOfDay(zoneId).toInstant().toEpochMilli();
		return getEvents(fromDate);
	}

	public List<com.razz.model.Event> getEvents(long fromDate) throws Exception {
		logger.debug(getClass(), "getEvents : calendarId=%s", calendarId);

		//
		if (calendarService == null)
			init();

		//
		final Events googleCalendarEvents = calendarService.events().list(calendarId).setSingleEvents(true).execute();
		//
		final List<com.razz.model.Event> eventList = new ArrayList<>();
		for (Event googleCalendarEvent : googleCalendarEvents.getItems()) {
			//
			final long eventStartDate = getEventStartDate(googleCalendarEvent.getStart());
			if (eventStartDate < fromDate)
				continue;

			//
			final String id = googleCalendarEvent.getId();
			final String location = googleCalendarEvent.getLocation();
			final String description = googleCalendarEvent.getDescription();

			//
			final com.razz.model.Venue venue = new com.razz.model.Venue();
			venue.setLocation(location);

			//
			final com.razz.model.Event event = new com.razz.model.Event();
			event.setId(id);
			event.setDate(eventStartDate);
			event.setDescription(description);
			event.setVenue(venue);

			//
			eventList.add(event);
		}
		
		//
		return eventList;
	}

	long getEventStartDate(EventDateTime eventDateTime) {
		if (eventDateTime == null)
			return 0L;
		DateTime start = eventDateTime.getDateTime();
		if (start == null)
			start = eventDateTime.getDate();
		return start.getValue();
	}
	
}
