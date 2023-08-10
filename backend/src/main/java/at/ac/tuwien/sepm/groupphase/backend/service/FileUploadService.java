package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.fileupload.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileUploadService {

    /**
     * Uploads the given file to the server.
     *
     * @param image to be uploaded
     * @return the uploaded filePath in a DTO
     * @throws ValidationException if the given file is not valid
     */
    FileDto uploadFile(MultipartFile image) throws ValidationException;
}
