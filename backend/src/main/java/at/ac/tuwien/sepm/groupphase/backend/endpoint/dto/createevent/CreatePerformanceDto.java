package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreatePerformanceDto {


    private Integer hallId;
    private String dateTime;
    private CreatePerformanceSectorDto[] sectorPrices;


    public Integer getHallId() {
        return hallId;
    }

    public void setHallId(Integer hallId) {
        this.hallId = hallId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public CreatePerformanceSectorDto[] getSectorPrices() {
        return sectorPrices;
    }

    public void setSectorPrices(CreatePerformanceSectorDto[] sectorPrices) {
        this.sectorPrices = sectorPrices;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        //check if dateTime is in the future
        try {
            LocalDateTime p = LocalDateTime.parse(dateTime, DateTimeFormatter.ISO_DATE_TIME);
            if (dateTime == null) {
                errors.add("DateTime must not be null");
            } else {
                if (p.isBefore(LocalDateTime.now())) {
                    errors.add(String.format("DateTime: %s for Hall: %s must be in the future", dateTime, hallId));
                }
            }
        } catch (DateTimeParseException e) {
            errors.add("DateTime must be in ISO-8601 format");
        }

        if (hallId == null) {
            errors.add("HallName must not be null");
        }
        if (sectorPrices == null) {
            errors.add("PerformanceSectors must not be null");
        } else {
            for (CreatePerformanceSectorDto performanceSector : sectorPrices) {
                errors.addAll(performanceSector.validate());
            }
        }
        return errors;
    }

    @Override
    public String toString() {
        return "CreatePerformanceDto{"
            + "hallId='" + hallId + '\''
            + ", dateTime='" + dateTime + '\''
            + ", performanceSectors=" + Arrays.toString(sectorPrices)
            + '}';
    }

    public static final class CreatePerformanceDtoBuilder {
        private Integer hallId;
        private String dateTime;
        private CreatePerformanceSectorDto[] sectorPrices;

        private CreatePerformanceDtoBuilder() {
        }

        public static CreatePerformanceDtoBuilder aCreatePerformanceDto() {
            return new CreatePerformanceDtoBuilder();
        }

        public CreatePerformanceDtoBuilder withHallId(Integer hallId) {
            this.hallId = hallId;
            return this;
        }

        public CreatePerformanceDtoBuilder withDateTime(String dateTime) {
            this.dateTime = dateTime;
            return this;
        }

        public CreatePerformanceDtoBuilder withSectorPrices(CreatePerformanceSectorDto[] sectorPrices) {
            this.sectorPrices = sectorPrices;
            return this;
        }

        public CreatePerformanceDto build() {
            CreatePerformanceDto createPerformanceDto = new CreatePerformanceDto();
            createPerformanceDto.setHallId(hallId);
            createPerformanceDto.setDateTime(dateTime);
            createPerformanceDto.setSectorPrices(sectorPrices);
            return createPerformanceDto;
        }
    }
}
