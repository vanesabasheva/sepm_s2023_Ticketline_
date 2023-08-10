package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Profile("generateData")
@Component
public class ArtistDataGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_ARTISTS_TO_GENERATE = 40;
    private static final String TEST_ARTIST_NAME = "Artist";


    private final ArtistRepository artistRepository;


    public ArtistDataGenerator(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }


    @PostConstruct
    private void generateArist() {
        if (artistRepository.findAll().size() > 0) {
            LOGGER.debug("artist already generated");
        } else {
            LOGGER.debug("generating {} artist entries", NUMBER_OF_ARTISTS_TO_GENERATE);
            List<Artist> artists = new ArrayList<>();
            for (int i = 1; i <= NUMBER_OF_ARTISTS_TO_GENERATE; i++) {
                Artist artist = Artist.ArtistBuilder.aArtist()
                    .withName(TEST_ARTIST_NAME + " " + i)
                    .build();
                artists.add(artist);
            }
            LOGGER.debug("saving artist {}", artists);
            artistRepository.saveAll(artists);
        }
    }


}
