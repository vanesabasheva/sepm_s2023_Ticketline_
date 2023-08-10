package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.bookings.BookingMerchandiseDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MerchandiseMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Merchandise;
import at.ac.tuwien.sepm.groupphase.backend.repository.MerchandiseRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.MerchandiseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class CustomMerchandiseService implements MerchandiseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final MerchandiseRepository merchandiseRepository;
    private final MerchandiseMapper mapper;

    public CustomMerchandiseService(MerchandiseRepository merchandiseRepository, MerchandiseMapper mapper) {
        this.merchandiseRepository = merchandiseRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MerchandiseDto> getMerchandise(boolean withPoints) {
        LOGGER.trace("getMerchandise()");
        List<Merchandise> entities;
        if (withPoints) {
            entities = this.merchandiseRepository.findAllMerchandiseWithPoints();
        } else {
            entities = this.merchandiseRepository.findAll();
        }
        return mapper.merchandiseListToMerchandiseDtoList(entities);
    }

    @Override
    public List<MerchandiseDto> getInfoFromBooking(List<BookingMerchandiseDto> bookingMerchandises) {
        LOGGER.trace("getInfoFromBooking({})", bookingMerchandises);
        //TODO add validation
        List<Merchandise> merchandises = this.merchandiseRepository.findAllById(
            bookingMerchandises.stream().map(bookingMerchandiseDto -> bookingMerchandiseDto.getId())
                .collect(Collectors.toList()));
        return this.mapper.merchandiseListToMerchandiseDtoList(merchandises);
    }
}
