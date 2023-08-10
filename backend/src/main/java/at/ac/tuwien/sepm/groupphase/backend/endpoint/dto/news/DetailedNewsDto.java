package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Objects;

public class DetailedNewsDto extends SimpleNewsDto {

    @NotNull(message = "Content must not be null")
    @Size(min = 1)
    private String content;

    @NotNull(message = "EventId must not be null")
    private Integer eventId;

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailedNewsDto that)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        return Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content);
    }

    @Override
    public String toString() {
        return "DetailedNewsDto{"
            + "content='" + content + '\''
            + '}';
    }


    public static final class DetailedNewsDtoBuilder {
        private Integer id;
        private LocalDate publicationDate;
        private String title;
        private String summary;
        private String content;
        private Integer eventId;

        private DetailedNewsDtoBuilder() {
        }

        public static DetailedNewsDtoBuilder aDetailedNewsDto() {
            return new DetailedNewsDtoBuilder();
        }

        public DetailedNewsDtoBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public DetailedNewsDtoBuilder withPublicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public DetailedNewsDtoBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public DetailedNewsDtoBuilder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public DetailedNewsDtoBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public DetailedNewsDtoBuilder withEventId(Integer eventId) {
            this.eventId = eventId;
            return this;
        }

        public DetailedNewsDto build() {
            DetailedNewsDto detailedNewsDto = new DetailedNewsDto();
            detailedNewsDto.setId(id);
            detailedNewsDto.setPublicationDate(publicationDate);
            detailedNewsDto.setTitle(title);
            detailedNewsDto.setSummary(summary);
            detailedNewsDto.setContent(content);
            detailedNewsDto.setEventId(eventId);
            return detailedNewsDto;
        }
    }


}
