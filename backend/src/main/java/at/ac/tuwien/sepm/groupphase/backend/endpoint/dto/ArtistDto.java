package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class ArtistDto {

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static final class ArtistBuilder {
        private Integer id;
        private String name;

        private ArtistBuilder() {
        }

        public static ArtistBuilder anArtist() {
            return new ArtistBuilder();
        }

        public ArtistBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public ArtistBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public ArtistDto build() {
            ArtistDto artistDto = new ArtistDto();
            artistDto.setId(id);
            artistDto.setName(name);
            return artistDto;
        }
    }
}
