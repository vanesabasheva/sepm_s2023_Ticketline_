package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.Hall;
import at.ac.tuwien.sepm.groupphase.backend.entity.Seat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;

@Profile("generateData")
@Component
@DependsOn({"sectorDataGenerator"})
public class SeatDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final SeatRepository seatRepository;
    private final SectorRepository sectorRepository;
    private final HallRepository hallRepository;

    public SeatDataGenerator(SeatRepository seatRepository, SectorRepository sectorRepository, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.sectorRepository = sectorRepository;
        this.hallRepository = hallRepository;
    }

    private void generateSectorSeats(int rowStart, int rowEnd, int colStart, int colEnd, Sector sector) {
        for (int j = rowStart; j < rowEnd; j++) {
            for (int k = colStart; k < colEnd; k++) {
                Seat seat = Seat.SeatBuilder.aSeat()
                    .withSector(sector)
                    .withRow(j)
                    .withNumber(k)
                    .build();
                LOGGER.debug("saving seat {} from sector {}", seat, sector.getId());
                seatRepository.save(seat);
            }
        }
    }

    @PostConstruct
    private void generateSeats() {
        if (seatRepository.findAll().size() > 0) {
            LOGGER.debug("seats already generated");
        } else {
            LOGGER.debug("generating  seat entries");

            List<Hall> halls = hallRepository.findAll();

            for (Hall hall : halls) {
                List<Sector> sectors = sectorRepository.findAllByHallId(hall.getId());
                long count = sectors.size();
                int rowsPerSector = (int) (3 + Math.random() * 9);
                int columnsPerSector = (int) (3 + Math.random() * 6);

                int curNum = 1;
                int curRow = 1;
                for (int i = 0; i < count; i++) {
                    if (Math.ceil(count / 2.0) == i) {
                        if (count % 2 != 0) {
                            curNum = columnsPerSector / 2 + 2;
                            curRow += rowsPerSector + 1;
                        }
                    }
                    generateSectorSeats(curRow, curRow + rowsPerSector, curNum, curNum + columnsPerSector, sectors.get(i));
                    curNum += columnsPerSector + 1;
                }
            }
        }
    }
}
