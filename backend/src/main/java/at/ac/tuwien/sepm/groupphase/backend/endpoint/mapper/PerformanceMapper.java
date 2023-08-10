package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedOrderPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedPerformanceSectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PerformanceTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.PerformanceSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public abstract class PerformanceMapper {
    @Autowired
    protected LocationMapper locationMapper;
    @Autowired
    protected SectorMapper sectorMapper;
    @Autowired
    protected SeatMapper seatMapper;
    @Autowired
    protected EventMapper eventMapper;
    @Autowired
    protected HallMapper hallMapper;

    public DetailedPerformanceDto performanceToDetailedPerformanceDto(Performance performance) {
        if (performance == null) {
            return null;
        }
        Hall hall = performance.getHall();
        if (hall == null) {
            return null;
        }
        Location location = hall.getLocation();
        if (location == null) {
            return null;
        }
        if (performance.getEvent() == null) {
            return null;
        }

        LocationDto locationDto = locationMapper.locationToLocationDto(location);
        Map<Integer, DetailedPerformanceSectorDto> detailedPerformanceSectorDto =
            sectorMapper.performanceSectorSetToDetailedPerformanceSectorDtoMap(performance.getPerformanceSectors());

        PerformanceTicketDto[][] performanceTickets = seatMapper.performanceToReservedSeat(performance);

        return DetailedPerformanceDto.DetailedPerformanceDtoBuilder.aDetailedPerformanceDto()
            .withTimestamp(performance.getDatetime())
            .withEventName(performance.getEvent().getName())
            .withHallName(hall.getName())
            .withLocation(locationDto)
            .withPerformanceSector(detailedPerformanceSectorDto)
            .withPerformanceTickets(performanceTickets)
            .build();
    }

    public DetailedPerformanceDto performanceDtotoDetailedPerformanceDtoForSearch(Performance performance) {
        if (performance == null) {
            return null;
        }
        Hall hall = performance.getHall();
        if (hall == null) {
            return null;
        }
        Location location = hall.getLocation();
        if (location == null) {
            return null;
        }
        if (performance.getEvent() == null) {
            return null;
        }

        LocationDto locationDto = locationMapper.locationToLocationDto(location);
        return DetailedPerformanceDto.DetailedPerformanceDtoBuilder.aDetailedPerformanceDto()
            .withId(performance.getId())
            .withTimestamp(performance.getDatetime())
            .withEventName(performance.getEvent().getName())
            .withHallName(hall.getName())
            .withLocation(locationDto)
            .build();
    }

    public DetailedOrderPerformanceDto performanceToDetailedOrderPerformanceDto(Performance performance) {
        if (performance == null) {
            return null;
        }
        return DetailedOrderPerformanceDto.DetailedOrderPerformanceDtoBuilder.aDetailedOrderPerformanceDto()
            .withId(performance.getId())
            .withDatetime(performance.getDatetime())
            .withEvent(eventMapper.eventToDetailedEventDto(performance.getEvent()))
            .withHall(hallMapper.hallToDetailedHallDto(performance.getHall()))
            .build();
    }

    public List<PerformanceSearchDto> performanceToPerformanceSearchDto(List<Performance> performances) {

        if (performances == null) {
            return null;
        }
        List<PerformanceSearchDto> performanceSearchDtos = new ArrayList<>();
        for (Performance performance : performances) {
            Hall hall = performance.getHall();
            if (hall == null) {
                return null;
            }
            Location location = hall.getLocation();
            if (location == null) {
                return null;
            }
            if (performance.getEvent() == null) {
                return null;
            }

            LocationDto locationDto = locationMapper.locationToLocationDto(location);
            PerformanceSearchDto performanceSearchDto = PerformanceSearchDto.PerformanceSearchDtoBuilder.aPerformanceSearchDtoBuilder()
                .withId(performance.getId())
                .withTimestamp(performance.getDatetime())
                .withEventName(performance.getEvent().getName())
                .withHallName(hall.getName())
                .withLocation(locationDto)
                .withImagePath(performance.getEvent().getImagePath())
                .build();
            performanceSearchDtos.add(performanceSearchDto);
        }
        return performanceSearchDtos;
    }
}


