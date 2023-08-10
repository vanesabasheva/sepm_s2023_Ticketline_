package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.CreateDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import at.ac.tuwien.sepm.groupphase.backend.exception.ConflictException;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Service;

import java.util.List;

/*
 Describes a service for managing events
 */
@Service
public interface EventService {

    /**
     * Get the events filtered by the given artist/band name.
     *
     * @param artist the artist whose events should be listed
     * @return list of the events of the given artist
     */
    List<Event> getAllEventsOfArtist(ArtistSearchDto artist);

    /**
     * Get the artist filtered by the given artist/band name.
     *
     * @param name the name of the artists that should be listed
     * @return all artists matching the given name
     */
    List<Artist> getAllArtists(ArtistSearchDto name);

    /**
     * Get the events filtered by the given parameters.
     *
     * @param parameters the parameters of the events that should be listed
     * @return list of the found events
     */
    List<Event> getAllEventsWithParameters(EventSearchDto parameters);


    /**
     * creates an event.
     *
     * @param createDto the event to be created.
     * @return the created event.
     * @throws FatalException      if an error occurred while accessing the database.
     * @throws ValidationException if the given event is not valid.
     * @throws ConflictException   if the given event is in conflict with another event.
     */
    DetailedEventDto createEvent(CreateDto createDto) throws FatalException, ValidationException, ConflictException;
}
