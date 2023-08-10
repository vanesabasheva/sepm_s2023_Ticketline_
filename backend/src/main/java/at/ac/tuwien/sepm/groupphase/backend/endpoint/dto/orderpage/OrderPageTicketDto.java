package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderPageTicketDto {
    private Integer id;
    private BigDecimal price;
    private Integer number;
    private Integer row;
    private String sectorName;
    private boolean standing;
    private String eventName;
    private String hallName;
    private LocalDateTime performanceStart;
    List<String> artistNames;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public String getSectorName() {
        return sectorName;
    }

    public void setSectorName(String sectorName) {
        this.sectorName = sectorName;
    }

    public boolean isStanding() {
        return standing;
    }

    public void setStanding(boolean standing) {
        this.standing = standing;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getHallName() {
        return hallName;
    }

    public void setHallName(String hallName) {
        this.hallName = hallName;
    }

    public LocalDateTime getPerformanceStart() {
        return performanceStart;
    }

    public void setPerformanceStart(LocalDateTime performanceStart) {
        this.performanceStart = performanceStart;
    }

    public List<String> getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(List<String> artistNames) {
        this.artistNames = artistNames;
    }
}
