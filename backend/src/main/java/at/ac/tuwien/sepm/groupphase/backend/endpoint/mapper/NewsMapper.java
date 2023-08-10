package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.DetailedNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.news.SimpleNewsDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.News;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface NewsMapper {

    @Named("simpleNews")
    @Mapping(target = "imagePath", source = "imagePath")
    SimpleNewsDto newsToSimpleNewsDto(News news);

    @IterableMapping(qualifiedByName = "simpleNews")
    List<SimpleNewsDto> newsToSimpleNewsDto(List<News> news);

    @Mapping(target = "eventId", source = "event.id")
    DetailedNewsDto newsToDetailedNewsDto(News news);

    @Mapping(target = "event.id", source = "eventId")
    News detailedNewsDtoToNews(DetailedNewsDto detailedNewsDto);
}
