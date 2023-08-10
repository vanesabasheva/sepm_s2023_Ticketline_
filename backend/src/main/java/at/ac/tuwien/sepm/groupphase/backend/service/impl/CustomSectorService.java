package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent.SectorDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.SectorMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Service
public class CustomSectorService implements SectorService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


    private final SectorRepository sectorRepository;

    private final SectorMapper sectorMapper;

    @Autowired
    public CustomSectorService(SectorRepository sectorRepository, SectorMapper sectorMapper) {

        this.sectorRepository = sectorRepository;
        this.sectorMapper = sectorMapper;
    }

    @Override
    public List<SectorDto> getSectorsFromHall(Integer hallId) {
        List<Sector> sectors = this.sectorRepository.findAllByHallId(hallId);
        return this.sectorMapper.sectorListToDetailedSectorDtoList(sectors);
    }
}
