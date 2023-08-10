package at.ac.tuwien.sepm.groupphase.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.time.LocalDate;
import java.util.Set;


@Entity
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate publicationDate;

    @Column(length = 200)
    private String imagePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @ManyToMany
    @JoinTable(
        name = "read_news",
        joinColumns = @JoinColumn(name = "news_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<ApplicationUser> users;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }


    public Set<ApplicationUser> getUsers() {
        return users;
    }

    public void setUsers(Set<ApplicationUser> users) {
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(final LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(final Event event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "News{"
            +
            "id=" + id
            +
            ", title='" + title + '\''
            +
            ", content='" + content + '\''
            +
            ", publicationDate=" + publicationDate
            +
            ", imagePath='" + imagePath + '\''
            +
            ", event=" + event
            +
            ", users=" + users
            +
            '}';
    }

    public static final class NewsBuilder {
        private Integer id;
        private String title;
        private String summary;
        private String content;
        private LocalDate publicationDate;
        private String imagePath;
        private Event event;
        private Set<ApplicationUser> users;

        private NewsBuilder() {
        }

        public static NewsBuilder aNews() {
            return new NewsBuilder();
        }

        public NewsBuilder withId(Integer id) {
            this.id = id;
            return this;
        }

        public NewsBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public NewsBuilder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public NewsBuilder withContent(String content) {
            this.content = content;
            return this;
        }

        public NewsBuilder withImagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public NewsBuilder withPublicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public NewsBuilder withEvent(Event event) {
            this.event = event;
            return this;
        }

        public NewsBuilder withUsers(Set<ApplicationUser> users) {
            this.users = users;
            return this;
        }

        public News build() {
            News news = new News();
            news.setId(id);
            news.setTitle(title);
            news.setSummary(summary);
            news.setContent(content);
            news.setPublicationDate(publicationDate);
            news.setImagePath(imagePath);
            news.setEvent(event);
            news.setUsers(users);
            return news;
        }
    }
}

