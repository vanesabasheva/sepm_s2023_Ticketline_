package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.LocationDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.checkout.CheckoutLocation;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.search.LocationSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Location;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public abstract class LocationMapper {

    public abstract LocationDto locationToLocationDto(Location location);

    public abstract List<LocationDto> locationListToLocationDtoList(List<Location> location);

    public CheckoutLocation locationToCheckoutLocation(Location location) {
        CheckoutLocation checkoutLocation = new CheckoutLocation();
        checkoutLocation.setLocationId(location.getId());
        checkoutLocation.setCity(location.getCity());
        checkoutLocation.setCountry(location.getCountry());
        checkoutLocation.setPostalCode(location.getPostalCode());
        checkoutLocation.setStreet(location.getStreet());
        return checkoutLocation;
    }

    public abstract LocationSearchDto locationToLocationSearchDto(Location location);
}
