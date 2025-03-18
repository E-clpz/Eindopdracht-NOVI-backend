package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.HelperDto;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestDto toDto(Request request, String helperEmail, String helperPhoneNumber, String fileUrl) {

        HelperDto helperDto = null;
        if (request.getHelper() != null) {
            helperDto = new HelperDto(
                    request.getHelper().getId(),
                    request.getHelper().getUsername(),
                    request.getHelper().getEmail(),
                    request.getHelper().getPhoneNumber(),
                    request.getHelper().getRating());
        }

        Long requesterId = request.getRequester() != null ? request.getRequester().getId() : null;

        if ("Geaccepteerd".equals(request.getStatus()) && request.getHelper() != null) {
            helperEmail = request.getHelper().getEmail();
            helperPhoneNumber = request.getHelper().getPhoneNumber();
        }

        return new RequestDto(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory().getName(),
                request.getStatus(),
                request.getCity(),
                requesterId,
                helperDto,
                request.getPreferredDate(),
                helperEmail,
                helperPhoneNumber,
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
