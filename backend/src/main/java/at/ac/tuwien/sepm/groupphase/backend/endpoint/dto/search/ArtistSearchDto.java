package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search;

public class ArtistSearchDto {
    private Integer id;
    private String name;

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
