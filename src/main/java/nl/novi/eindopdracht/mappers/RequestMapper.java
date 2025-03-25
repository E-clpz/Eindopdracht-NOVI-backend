package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.dtos.UserDto;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestDto toDto(Request request, String fileUrl) {

        UserDto userDto = null;
        if (request.getHelper() != null) {
            userDto = new UserDto(
                    request.getHelper().getId(),
                    request.getHelper().getUsername(),
                    request.getHelper().getCity(),
                    request.getHelper().getEmail(),
                    request.getHelper().getPhoneNumber(),
                    request.getHelper().getRole(),
                    request.getHelper().getRating()
            );
        }

        Long requesterId = request.getRequester() != null ? request.getRequester().getId() : null;

        return new RequestDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory().getName(),
                request.getStatus(),
                request.getCity(),
                requesterId,
                userDto,
                request.getPreferredDate(),
                fileUrl
        );
    }


    public Request toEntity(RequestDto requestDto, Category category, User user) {
        return new Request(
                requestDto.getTitle(),
                requestDto.getDescription(),
                category,
                "Open",
                requestDto.getCity(),
                user,
                requestDto.getPreferredDate(),
                null
        );
    }
}
