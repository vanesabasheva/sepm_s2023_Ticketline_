package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall;

public class HallDto {

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


    public static final class HallDtoBuilder {

        private Integer id;
        private String name;


        private HallDtoBuilder() {
        }

        //with methods for all attributes
        public HallDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public HallDtoBuilder withName(String name) {
            this.name = name;
            return this;
        }


        public HallDto build() {
            HallDto hallDto = new HallDto();
            hallDto.setId(id);
            hallDto.setName(name);
            return hallDto;
        }
    }

}
