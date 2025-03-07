package nl.novi.eindopdracht.mappers;

import nl.novi.eindopdracht.dtos.HelperDto;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.models.Category;
import nl.novi.eindopdracht.models.Request;
import nl.novi.eindopdracht.models.User;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper {

    public RequestDto toDto(Request request, String requesterEmail, String requesterPhoneNumber, String helperEmail, String helperPhoneNumber) {
        HelperDto helperDto = null;
        if (request.getHelper() != null) {
            Integer rating = request.getHelper().getRating() != null ? request.getHelper().getRating() : 0;
            helperDto = new HelperDto(request.getHelper().getUsername(), rating);
        }

        if ("Geaccepteerd".equals(request.getStatus())) {
            requesterEmail = request.getRequester().getEmail();
            requesterPhoneNumber = request.getRequester().getPhoneNumber();
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
                request.getRequester().getId(),
                helperDto,
                request.getPreferredDate(),
                requesterEmail,
                requesterPhoneNumber,
                helperEmail,
                helperPhoneNumber
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
