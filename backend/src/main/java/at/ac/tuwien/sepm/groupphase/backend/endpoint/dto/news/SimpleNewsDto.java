package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

public class SimpleNewsDto {

    private Integer id;
    private LocalDate publicationDate;
    @NotNull(message = "Title must not be null")
    @Size(min = 1, max = 100)
    private String title;
    @NotNull(message = "Summary must not be null")
    @Size(min = 1, max = 1000)
    private String summary;
    @Size(max = 200)
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SimpleNewsDto that)) {
            return false;
        }
        return Objects.equals(id, that.id)
            && Objects.equals(publicationDate, that.publicationDate)
            && Objects.equals(title, that.title)
            && Objects.equals(summary, that.summary)
            && Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publicationDate, title, summary, imagePath);
    }

    @Override
    public String toString() {
        return "SimpleNewsDto{"
            + "id=" + id
            + ", publicationDate=" + publicationDate
            + ", title='" + title + '\''
            + ", summary='" + summary + '\''
            + ", imagePath='" + imagePath + '\''
            + '}';
    }

    public static final class SimpleNewsDtoBuilder {
        private Integer id;
        private LocalDate publicationDate;
        private String title;
        private String summary;
        private String imagePath;


        private SimpleNewsDtoBuilder() {
        }

        public static SimpleNewsDtoBuilder aSimpleNewsDto() {
            return new SimpleNewsDtoBuilder();
        }

        public SimpleNewsDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public SimpleNewsDtoBuilder withPublicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public SimpleNewsDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public SimpleNewsDtoBuilder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public SimpleNewsDtoBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public SimpleNewsDto build() {
            SimpleNewsDto simpleNewsDto = new SimpleNewsDto();
            simpleNewsDto.setId(id);
            simpleNewsDto.setPublicationDate(publicationDate);
            simpleNewsDto.setTitle(title);
            simpleNewsDto.setSummary(summary);
            simpleNewsDto.setImagePath(imagePath);
            return simpleNewsDto;
        }
    }
}


