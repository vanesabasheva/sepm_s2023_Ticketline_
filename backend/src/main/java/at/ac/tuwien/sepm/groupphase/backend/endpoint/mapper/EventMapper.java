package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedEventDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.EventSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Event;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@Mapper(componentModel = "spring")
public abstract class EventMapper {
    public DetailedEventDto eventToDetailedEventDto(Event event) {
        if (event == null) {
            return null;
        }
        String artistsFormatted = "";
        // format the set of artists to a string, but only show at most 3 artists!
        int i = 0;
        Set<Artist> artists = event.getArtists();
        int artistsSize = event.getArtists().size();
        for (Artist artist : artists) {
            artistsFormatted += artist.getName();
            if (i < 3) {
                if (artistsSize >= 3) {
                    artistsFormatted += ", ";
                } else if (i < artistsSize - 1) {
                    artistsFormatted += ", ";
                } else {
                    break;
                }
            }
            if (i == 2) {
                if (artistsSize != 3) {
                    artistsFormatted += "...";
                }
                break;
            }
            i++;
        }

        return DetailedEventDto.DetailedEventDtoBuilder.aDetailedEventDto()
            .withId(event.getId())
            .withName(event.getName())
            .withType(event.getType())
            .withLength(event.getLength())
            .withDescription(event.getDescription())
            .withArtists(artistsFormatted)
            .build();
    }


    public List<EventSearchDto> eventToEventSearchDto(List<Event> events) {
        List<EventSearchDto> eventSearchDtos = new ArrayList<>();
        for (Event event : events) {
            String artistsOfEvent = "";
            Set<Artist> artists = event.getArtists();
            Stream<Artist> artistStream = artists.stream().sorted(Comparator.comparing(Artist::getId));
            for (Artist artist : artistStream.toList()) {
                artistsOfEvent += artist.getName() + "; ";
            }

            EventSearchDto currentEvent = EventSearchDto.EventSearchDtoBuilder.aEventSearchDto()
                .withId(event.getId())
                .withName(event.getName())
                .withType(event.getType())
                .withDescription(event.getDescription())
                .withLength(event.getLength().toString())
                .withArtists(artistsOfEvent)
                .withImagePath(event.getImagePath())
                .build();
            eventSearchDtos.add(currentEvent);
        }
        return eventSearchDtos;
    }
}
