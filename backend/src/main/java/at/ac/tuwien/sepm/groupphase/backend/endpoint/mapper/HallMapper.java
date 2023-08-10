package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.DetailedHallDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface HallMapper {


    DetailedHallDto hallToDetailedHallDto(Hall hall);

    @Named("hall")
    HallDto hallToHallDto(Hall hall);

    @IterableMapping(qualifiedByName = "hall")
    List<HallDto> hallListToHallDtoList(List<Hall> halls);


}
