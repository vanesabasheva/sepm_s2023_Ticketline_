package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;

import java.util.List;

public interface MerchandiseService {
    
    /**
     * get all merchandise .
     *
     * @param withPoints if true, the points of the merchandise will be included.
     * @return list of merchandise
     */
    List<MerchandiseDto> getMerchandise(boolean withPoints);

    /**
     * get all merchandise from a booking.
     *
     * @param bookingMerchandises list of bookingMerchandiseDto
     * @return list of merchandise
     */
    List<MerchandiseDto> getInfoFromBooking(List<BookingMerchandiseDto> bookingMerchandises);
}
