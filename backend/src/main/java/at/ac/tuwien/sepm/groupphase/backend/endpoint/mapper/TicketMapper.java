package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPageTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;

@Mapper
public interface TicketMapper {

    @Named("simpleTicket")
    SimpleTicketDto ticketToSimpleTicket(Ticket ticket);


    @IterableMapping(qualifiedByName = "simpleTicket")
    List<SimpleTicketDto> ticketToSimpleTicket(List<Ticket> tickets);


    @Mapping(target = "performance.event.artists", source = "ticket.performance.event.artists", qualifiedByName = "mapArtists")
    DetailedTicketDto ticketToDetailedTicket(Ticket ticket);

    @Named("mapArtists")
    default String mapArtists(Set<Artist> artists) {
        String artistsFormatted = "";
        // format the set of artists to a string, but only show at most 3 artists!
        int i = 0;
        int artistsSize = artists.size();
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
        return artistsFormatted;
    }
}
