package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.fileupload;

import jakarta.validation.constraints.Size;

public class FileDto {

    @Size(max = 200)
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static final class CreateDtoBuilder {
        private String imagePath;

        private CreateDtoBuilder() {
        }

        public static CreateDtoBuilder aCreateDto() {
            return new CreateDtoBuilder();
        }

        public CreateDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public FileDto build() {
            FileDto fileDto = new FileDto();
            fileDto.setImagePath(imagePath);
            return fileDto;
        }
    }

}
