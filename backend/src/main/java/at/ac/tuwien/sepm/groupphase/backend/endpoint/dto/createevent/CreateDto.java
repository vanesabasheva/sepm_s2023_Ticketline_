package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.createevent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateDto {

    CreateEventDto event;
    CreatePerformanceDto[] performances;

    public CreateEventDto getEvent() {
        return event;
    }

    public void setEvent(CreateEventDto event) {
        this.event = event;
    }

    public CreatePerformanceDto[] getPerformances() {
        return performances;
    }

    public void setPerformances(CreatePerformanceDto[] performances) {
        this.performances = performances;
    }

    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (event == null) {
            errors.add("Event must not be null");
        } else {
            errors.addAll(event.validate());
        }
        if (performances == null || performances.length == 0) {
            errors.add("Performances must not be null");
        } else {
            for (CreatePerformanceDto performances : performances) {
                errors.addAll(performances.validate());
            }
        }
        return errors;
    }

    @Override
    public String toString() {
        return "CreateDto{"
            + "event=" + event
            + ", performances= " + Arrays.toString(performances)
            + '}';
    }

    public static final class CreateDtoBuilder {
        CreateEventDto event;
        CreatePerformanceDto[] performances;

        private CreateDtoBuilder() {
        }

        public static CreateDtoBuilder aCreateDto() {
            return new CreateDtoBuilder();
        }

        public CreateDtoBuilder withEvent(CreateEventDto event) {
            this.event = event;
            return this;
        }

        public CreateDtoBuilder withPerformance(CreatePerformanceDto[] performances) {
            this.performances = performances;
            return this;
        }

        public CreateDto build() {
            CreateDto createDto = new CreateDto();
            createDto.setEvent(event);
            createDto.setPerformances(performances);
            return createDto;
        }
    }
}
