package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomArtistService implements ArtistService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ArtistRepository artistRepository;
    private final ArtistMapper mapper;

    public CustomArtistService(ArtistRepository artistRepository, ArtistMapper mapper) {
        this.artistRepository = artistRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ArtistDto> getAllArtists() {
        List<Artist> artists = this.artistRepository.findAll();
        return this.mapper.artistToArtistDto(artists);
    }
}
