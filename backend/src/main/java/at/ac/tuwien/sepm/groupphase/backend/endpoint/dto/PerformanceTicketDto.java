package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class PerformanceTicketDto {
    private boolean reserved;
    private Integer ticketId;
    private Integer sectorId;
    private Integer number;
    private Integer row;

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public Integer getSectorId() {
        return sectorId;
    }

    public void setSectorId(Integer sectorId) {
        this.sectorId = sectorId;
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

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public static final class PerformanceTicketBuilder {

        private boolean reserved;
        private Integer ticketId;
        private Integer sectorId;
        private Integer number;
        private Integer row;

        private PerformanceTicketBuilder() {
        }

        public static PerformanceTicketBuilder aPerformanceTicket() {
            return new PerformanceTicketBuilder();
        }

        public PerformanceTicketBuilder withReserved(boolean reserved) {
            this.reserved = reserved;
            return this;
        }

        public PerformanceTicketBuilder withSectorId(Integer sectorId) {
            this.sectorId = sectorId;
            return this;
        }

        public PerformanceTicketBuilder withNumber(Integer number) {
            this.number = number;
            return this;
        }

        public PerformanceTicketBuilder withRow(Integer row) {
            this.row = row;
            return this;
        }

        public PerformanceTicketBuilder withTicketId(Integer ticketId) {
            this.ticketId = ticketId;
            return this;
        }

        public PerformanceTicketDto build() {
            PerformanceTicketDto performanceTicketDto = new PerformanceTicketDto();
            performanceTicketDto.setReserved(reserved);
            performanceTicketDto.setSectorId(sectorId);
            performanceTicketDto.setNumber(number);
            performanceTicketDto.setRow(row);
            performanceTicketDto.setTicketId(ticketId);
            return performanceTicketDto;
        }
    }
}
