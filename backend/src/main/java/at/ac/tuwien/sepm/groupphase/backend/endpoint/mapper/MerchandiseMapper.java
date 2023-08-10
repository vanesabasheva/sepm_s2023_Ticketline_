package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface MerchandiseMapper {

    MerchandiseDto merchandiseToMerchandiseDto(Merchandise merchandise);

    List<MerchandiseDto> merchandiseListToMerchandiseDtoList(List<Merchandise> merchandises);
}

