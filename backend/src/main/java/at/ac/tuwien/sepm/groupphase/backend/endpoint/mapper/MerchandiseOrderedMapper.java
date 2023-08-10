package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.orderpage.OrderPageMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.entity.MerchandiseOrdered;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public abstract class MerchandiseOrderedMapper {
    public List<OrderPageMerchandiseDto> merchandiseOrderedsToOrderPageMerchandiseDtos(Set<MerchandiseOrdered> merchandiseOrdered) {
        return merchandiseOrdered.stream().map(this::merchandiseOrderedToOrderPageMerchandiseDto).toList();
    }

    public OrderPageMerchandiseDto merchandiseOrderedToOrderPageMerchandiseDto(MerchandiseOrdered merchandiseOrdered) {
        OrderPageMerchandiseDto dto = new OrderPageMerchandiseDto();
        dto.setId(merchandiseOrdered.getId());
        dto.setQuantity(merchandiseOrdered.getQuantity());
        Merchandise merchandise = merchandiseOrdered.getMerchandise();
        dto.setPrice(merchandise.getPrice());
        dto.setPointsPrice(merchandise.getPointsPrice());
        dto.setPoints(merchandiseOrdered.getPoints());
        dto.setTitle(merchandise.getTitle());
        return dto;
    }
}
