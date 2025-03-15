package nl.novi.eindopdracht.services;

import jakarta.transaction.Transactional;
import nl.novi.eindopdracht.dtos.RequestDto;
import nl.novi.eindopdracht.exceptions.ConflictException;
import nl.novi.eindopdracht.exceptions.InvalidFileException;
import nl.novi.eindopdracht.exceptions.ResourceNotFoundException;
import nl.novi.eindopdracht.exceptions.UnauthorizedException;
import nl.novi.eindopdracht.mappers.RequestMapper;
import nl.novi.eindopdracht.models.*;
import nl.novi.eindopdracht.repositories.CategoryRepository;
import nl.novi.eindopdracht.repositories.FileRepository;
import nl.novi.eindopdracht.repositories.RequestRepository;
import nl.novi.eindopdracht.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final RequestMapper requestMapper;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, CategoryRepository categoryRepository, RequestMapper requestMapper, UserRepository userRepository, FileRepository fileRepository) {
        this.requestRepository = requestRepository;
        this.categoryRepository = categoryRepository;
        this.requestMapper = requestMapper;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Transactional
    public RequestDto acceptRequest(Long requestId, User helper) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new ResourceNotFoundException("Hulpvraag niet gevonden"));

        if ("Geaccepteerd".equals(request.getStatus())) {
            throw new ConflictException("Deze hulpvraag is al geaccepteerd.");
        }

        request.setStatus("Geaccepteerd");
        request.setHelper(helper);
        request = requestRepository.save(request);

        String helperEmail = request.getHelper().getEmail();
        String helperPhoneNumber = request.getHelper().getPhoneNumber();

        String fileUrl = null;
        if (request.getFile() != null) {
            fileUrl = request.getFile().getFileUrl();
        }

        return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
    }

    public List<RequestDto> getAllRequestsForHelpers(String categoryName, String city, String sortByDate) {
        List<Request> requests;

        if (categoryName != null) {
            Category category = categoryRepository.findByName(categoryName)
                    .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + categoryName));
            requests = requestRepository.findByCategory(category);
        } else {
            requests = requestRepository.findAll();
        }

        if (city != null) {
            requests = requests.stream()
                    .filter(request -> request.getCity().equalsIgnoreCase(city))
                    .collect(Collectors.toList());
        }

        if ("asc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r1.getPreferredDate().compareTo(r2.getPreferredDate()));
        } else if ("desc".equalsIgnoreCase(sortByDate)) {
            requests.sort((r1, r2) -> r2.getPreferredDate().compareTo(r1.getPreferredDate()));
        }

        return requests.stream().map(request -> {
            String helperEmail = request.getHelper() != null ? request.getHelper().getEmail() : null;
            String helperPhoneNumber = request.getHelper() != null ? request.getHelper().getPhoneNumber() : null;

            String fileUrl = null;
            if (request.getFile() != null) {
                fileUrl = request.getFile().getFileUrl();
            }

            return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
        }).collect(Collectors.toList());
    }

    public List<RequestDto> getRequestsForRequester(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        List<Request> requests = requestRepository.findByRequester(user);

        return requests.stream().map(request -> {
            String helperEmail = null;
            String helperPhoneNumber = null;
            if (request.getHelper() != null) {
                helperEmail = request.getHelper().getEmail();
                helperPhoneNumber = request.getHelper().getPhoneNumber();
            }

            String fileUrl = null;
            if (request.getFile() != null) {
                fileUrl = request.getFile().getFileUrl();
            }

            return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
        }).collect(Collectors.toList());
    }

    @Transactional
    public RequestDto updateRequest(Long id, RequestDto requestDto, MultipartFile file, UserDetails user) throws IOException {
        Category category = categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + requestDto.getCategory()));

        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));

        if (request.getStatus().equals("Gesloten") || request.getStatus().equals("Geaccepteerd")) {
            throw new IllegalStateException("Je kunt een hulpvraag niet meer wijzigen als de status 'Gesloten' of 'Geaccepteerd' is.");
        }

        if (!(request.getRequester().getUsername().equals(user.getUsername()) ||
                user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen bijwerken, of je moet admin zijn.");
        }

        request.setTitle(requestDto.getTitle());
        request.setDescription(requestDto.getDescription());
        request.setPreferredDate(requestDto.getPreferredDate());
        request.setCity(requestDto.getCity());
        request.setCategory(category);

        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();

            if (contentType != null && !(contentType.startsWith("image/") || contentType.equals("audio/mp3") || contentType.equals("application/pdf"))) {
                throw new InvalidFileException("Alleen afbeeldingsbestanden, MP3's en PDF-bestanden worden geaccepteerd. Bestandstype: " + contentType);
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new InvalidFileException("Bestand is te groot. De limiet is 5 MB.");
            }

            byte[] fileBytes = file.getBytes();

            String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/downloadFromDB/")
                    .path(file.getOriginalFilename())
                    .toUriString();

            FileDocument fileDocument = new FileDocument(file.getOriginalFilename(), fileBytes, contentType, request, fileUrl);
            fileRepository.save(fileDocument);

            request.setFile(fileDocument);
        }

        requestRepository.save(request);

        String helperEmail = request.getHelper() != null ? request.getHelper().getEmail() : null;
        String helperPhoneNumber = request.getHelper() != null ? request.getHelper().getPhoneNumber() : null;

        String fileUrl = null;
        FileDocument fileDocument = request.getFile();

        if (fileDocument != null) {
            fileUrl = fileDocument.getFileUrl();
        }

        return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
    }

    public void deleteRequest(Long id, UserDetails user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request met id: " + id + " niet gevonden"));
        if (!request.getRequester().getUsername().equals(user.getUsername()) && user.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().contains(Role.ADMIN.toString()))) {
            throw new UnauthorizedException("Je mag alleen je eigen hulpvragen verwijderen, of je moet admin zijn.");
        }
        requestRepository.delete(request);
    }

    public RequestDto createRequest(RequestDto requestDto, UserDetails userDetails, MultipartFile file) throws IOException {
        Category category = categoryRepository.findByName(requestDto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie niet gevonden: " + requestDto.getCategory()));

        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User niet gevonden"));

        Request request = requestMapper.toEntity(requestDto, category, user);

        request.setStatus("Open");
        request.setRequester(user);

        if (file != null && !file.isEmpty()) {
            String contentType = file.getContentType();
            if (contentType != null &&
                    !(contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
                throw new InvalidFileException("Alleen afbeeldingsbestanden en PDF-bestanden worden geaccepteerd.");
            }

            if (file.getSize() > 5 * 1024 * 1024) {
                throw new InvalidFileException("Bestand mag maximaal 5 MB groot zijn");
            }

            FileDocument fileDocument = new FileDocument();
            fileDocument.setFileData(file.getBytes());
            fileDocument.setFileName(file.getOriginalFilename());
            fileDocument.setRequest(request);

            fileRepository.save(fileDocument);
        }

        request = requestRepository.save(request);

        String helperEmail = request.getHelper() != null ? request.getHelper().getEmail() : null;
        String helperPhoneNumber = request.getHelper() != null ? request.getHelper().getPhoneNumber() : null;

        String fileUrl = null;
        if (request.getFileName() != null) {
            fileUrl = "/api/files/downloadFromDB/" + request.getId();
        }

        return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
    }


    public RequestDto getRequestsById(long id, UserDetails user) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request id: " + id + " niet gevonden"));

        if (!request.getRequester().getUsername().equals(user.getUsername())) {
            throw new UnauthorizedException("Je bent niet gemachtigd deze request te bekijken");
        }

        String helperEmail = request.getHelper() != null ? request.getHelper().getEmail() : null;
        String helperPhoneNumber = request.getHelper() != null ? request.getHelper().getPhoneNumber() : null;

        String fileUrl = null;
        if (request.getFileName() != null) {
            fileUrl = "/api/files/downloadFromDB/" + request.getFileName();
        }

        return requestMapper.toDto(request, helperEmail, helperPhoneNumber, fileUrl);
    }
}
