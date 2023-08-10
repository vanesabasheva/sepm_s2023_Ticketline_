package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class TicketDto {
    private Integer id;
    private SimpleSeatDto seat;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SimpleSeatDto getSeat() {
        return seat;
    }

    public void setSeat(SimpleSeatDto seat) {
        this.seat = seat;
    }
    
}
