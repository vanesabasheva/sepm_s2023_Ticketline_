package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;

import java.util.List;

public interface ArtistService {

    /**
     * gets all artists from the database.
     *
     * @return a list of all artists
     */
    List<ArtistDto> getAllArtists();
}
