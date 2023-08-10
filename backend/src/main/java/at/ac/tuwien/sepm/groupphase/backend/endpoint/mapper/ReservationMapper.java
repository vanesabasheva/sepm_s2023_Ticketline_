package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleReservationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Reservation;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface ReservationMapper {


    @Mapping(source = "id", target = "id")
    @Mapping(source = "cart", target = "cart")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "ticket.id", target = "ticketId")
    @Mapping(source = "expirationTs", target = "expirationTs")
    @Named("simpleReservation")
    SimpleReservationDto reservationToSimpleReservationDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "simpleReservation")
    List<SimpleReservationDto> reservationToSimpleReservationDto(List<Reservation> reservation);


    @Named("DetailedReservation")
    @Mapping(source = "reservation.ticket.performance.event.name", target = "eventName")
    @Mapping(source = "reservation.ticket", target = "ticket")
    @Mapping(source = "reservation.ticket.seat", target = "ticket.seat")
    @Mapping(source = "reservation.ticket.seat.sector.id", target = "ticket.seat.sector")
    DetailedReservationDto reservationToDetailedReservationDto(Reservation reservation);

    @IterableMapping(qualifiedByName = "DetailedReservation")
    List<DetailedReservationDto> reservationToDetailedReservationDto(List<Reservation> reservation);

}
