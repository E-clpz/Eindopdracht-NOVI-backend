package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestDto toDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory().getName(),
                request.getStatus(),
                request.getCity(),
                request.getRequester().getId(),
                request.getHelper() != null ? request.getHelper().getId() : null,
                request.getPreferredDate()
        );
    }

    public Request toEntity(RequestDto requestDto, Category category, User user) {
        Request request = new Request();
        request.setTitle(requestDto.getTitle());
        request.setDescription(requestDto.getDescription());
        request.setCategory(category);
        request.setCity(requestDto.getCity());
        request.setPreferredDate(requestDto.getPreferredDate());
        request.setRequester(user);

        return request;
    }
}
