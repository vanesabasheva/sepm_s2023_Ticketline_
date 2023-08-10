package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Profile("generateData")
@Component
@DependsOn({"artistDataGenerator"})
public class EventDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int NUMBER_OF_EVENTS_TO_GENERATE = 60;
    private static final String TEST_EVENT_NAME = "EventName";
    private static final String[] TEST_TYPE = {"Ballet", "Concert", "Opera", "Theatre", "Musical"};
    private static final String[] IMAGE_PATH = {"cat.png", "cat_black.png", "duck.jpg", "turtle.png"};
    private static final Duration TEST_DURATION = Duration.ofHours(1);
    private static final String TEST_DESCRIPTION = "Description";

    private final EventRepository eventRepository;
    private final ArtistRepository artistRepository;

    public EventDataGenerator(EventRepository eventRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.artistRepository = artistRepository;
    }

    @PostConstruct
    private void generateEvent() {
        if (eventRepository.findAll().size() > 0) {
            LOGGER.debug("event already generated");
        } else {
            LOGGER.debug("generating {} event entries", NUMBER_OF_EVENTS_TO_GENERATE);
            List<Artist> artists = artistRepository.findAllFetchEvents();
            Set<Event> events = new HashSet<>();
            Set<Artist> updatedArtists = new HashSet<>();
            for (int i = 1; i < NUMBER_OF_EVENTS_TO_GENERATE; i++) {

                int startIndex = (5 * i) % artists.size();
                int endIndex = Math.min(startIndex + 5, artists.size());
                List<Artist> artistList = artists.subList(startIndex, endIndex);

                Event event = Event.EventBuilder.aEvent()
                    .withName(TEST_EVENT_NAME + " " + i)
                    .withType(TEST_TYPE[i % TEST_TYPE.length])
                    .withLength(TEST_DURATION)
                    .withDescription(TEST_DESCRIPTION + " " + i)
                    .withArtists(Set.of(artistList.get(0), artistList.get(1), artistList.get(2), artistList.get(3), artistList.get(4)))
                    .withImagePath(IMAGE_PATH[i % IMAGE_PATH.length])
                    .build();
                events.add(event);

                for (Artist artist : artistList) {
                    Set<Event> newEvents = artist.getEvents();
                    newEvents.add(event);
                    artist.setEvents(newEvents);
                    updatedArtists.add(artist);
                }
            }
            LOGGER.debug("saving event {}", events);
            eventRepository.saveAll(events);
            artistRepository.saveAll(updatedArtists);
        }
    }
}
