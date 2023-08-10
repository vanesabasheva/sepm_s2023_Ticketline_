package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.hall.HallDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.HallMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.HallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomHallService implements HallService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final HallRepository hallRepository;
    private final HallMapper mapper;

    public CustomHallService(HallRepository hallRepository, HallMapper mapper) {
        this.hallRepository = hallRepository;
        this.mapper = mapper;
    }

    @Override
    public List<HallDto> getHalls() {
        List<Hall> halls = hallRepository.findAll();
        return this.mapper.hallListToHallDtoList(halls);
    }
}
