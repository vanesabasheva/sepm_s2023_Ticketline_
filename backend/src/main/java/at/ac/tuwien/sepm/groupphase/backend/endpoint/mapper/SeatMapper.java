package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedSeatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.PerformanceSector;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;

@Mapper
public abstract class SeatMapper {

    @Autowired
    protected TicketRepository ticketRepository;
    @Autowired
    protected SectorMapper sectorMapper;

    public PerformanceTicketDto[][] performanceToReservedSeat(Performance performance) {
        Set<PerformanceSector> performanceSector = performance.getPerformanceSectors();
        int maxRow = performanceSector.stream()
            .flatMap(ps -> ps.getSector().getSeats().stream())
            .mapToInt(Seat::getRow)
            .max()
            .orElse(0);
        int maxNumber = performanceSector.stream()
            .flatMap(ps -> ps.getSector().getSeats().stream())
            .mapToInt(Seat::getNumber)
            .max()
            .orElse(0);
        PerformanceTicketDto[][] performanceTicketDtos = new PerformanceTicketDto[maxRow + 1][maxNumber + 1];

        Set<Ticket> tickets = performance.getTickets();

        for (Ticket ticket : tickets) {
            Seat seat = ticket.getSeat();
            boolean reserved = true;
            if (ticket.getOrder() == null) {
                if (ticket.getReservation() == null) {
                    reserved = false;
                } else if (ticket.getReservation().getExpirationTs().isBefore(LocalDateTime.now())) {
                    reserved = false;
                }
            }
            performanceTicketDtos[seat.getRow()][seat.getNumber()] = PerformanceTicketDto.PerformanceTicketBuilder.aPerformanceTicket()
                .withRow(seat.getRow())
                .withNumber(seat.getNumber())
                .withSectorId(seat.getSector().getId())
                .withReserved(reserved)
                .withTicketId(ticket.getId())
                .build();
        }
        return performanceTicketDtos;
    }

    public DetailedSeatDto seatToDetailedSeatDto(Seat seat) {
        if (seat == null) {
            return null;
        }
        return DetailedSeatDto.DetailedSeatDtoBuilder.aDetailedSeatDto()
            .withRow(seat.getRow())
            .withNumber(seat.getNumber())
            .withSector(sectorMapper.sectorToDetailedSectorDto(seat.getSector()))
            .build();
    }

    /**
     * Seat to seat dto.
     *
     * @param seat the seat
     * @return the seat dto
     */
    abstract SeatDto seatToDto(Seat seat);

    /**
     * Dto to seat.
     *
     * @param seatDto the seat dto
     * @return the seat
     */
    abstract Seat dtoToSeat(SeatDto seatDto);
}
