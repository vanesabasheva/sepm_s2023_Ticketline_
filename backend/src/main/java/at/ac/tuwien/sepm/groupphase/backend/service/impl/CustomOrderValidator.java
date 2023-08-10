package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomOrderValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final MerchandiseRepository merchandiseRepository;

    @Autowired
    public CustomOrderValidator(MerchandiseRepository merchandiseRepository) {
        this.merchandiseRepository = merchandiseRepository;
    }

    public void validPoints(int userPoints, BookingDto bookingDto) throws ValidationException {
        LOGGER.info("Validating if user ({} Points) has enough points to get merchandise for free {}", userPoints, bookingDto);
        List<String> validationMsg = new ArrayList<>();
        try {
            boolean hasMerchandise = bookingDto.getMerchandise() != null && !bookingDto.getMerchandise().isEmpty();
            if (hasMerchandise) {
                int totalPoints = 0;
                for (BookingMerchandiseDto bookingMerchandiseDto : bookingDto.getMerchandise()) {
                    if (bookingMerchandiseDto.getBuyWithPoints()) {
                        Merchandise merchandise = merchandiseRepository.getReferenceById(bookingMerchandiseDto.getId());
                        totalPoints += merchandise.getPointsPrice() * bookingMerchandiseDto.getQuantity();
                    }
                }
                if (userPoints < totalPoints) {
                    validationMsg.add("User has not enough points to get merchandise for free");
                }
            }

            if (!validationMsg.isEmpty()) {
                throw new ValidationException("Validation of Points failed: ", validationMsg);
            }
        } catch (EntityNotFoundException e) {
            throw new NotFoundException("Could not find Merchandise", e);
        }
    }
}
