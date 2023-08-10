package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.fileupload.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FileUploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.lang.invoke.MethodHandles;


@RestController
@RequestMapping(value = "/api/v1")
public class FileUploadEndpoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final FileUploadService fileUploadService;

    @Autowired
    public FileUploadEndpoint(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/image")
    @Operation(summary = "upload File", security = @SecurityRequirement(name = "apiKey"))
    public FileDto uploadFile(@RequestParam(value = "image") MultipartFile image) {
        try {
            return this.fileUploadService.uploadFile(image);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

}
