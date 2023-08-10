package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.ArtistSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ArtistMapper {

    @Named("EventArtistSearch")
    @Mapping(source = "artist.name", target = "name")
    @Mapping(source = "artist.id", target = "id")
    ArtistSearchDto artistToEventArtistSearchDto(Artist artist);

    @IterableMapping(qualifiedByName = "EventArtistSearch")
    List<ArtistSearchDto> artistToEventArtistSearchDto(List<Artist> artists);

    List<ArtistDto> artistToArtistDto(List<Artist> artists);
}
