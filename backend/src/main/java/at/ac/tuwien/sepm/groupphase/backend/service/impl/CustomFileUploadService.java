package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.fileupload.FileDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.FatalException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.FileUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class CustomFileUploadService implements FileUploadService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final ResourceLoader resourceLoader;

    public CustomFileUploadService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public FileDto uploadFile(MultipartFile image) throws ValidationException {
        String filePath = null;
        if (image != null && !image.isEmpty()) {
            if (!isImage(image)) {
                LOGGER.warn("Unable to save image, wrong file type");
                throw new ValidationException("Validation error", List.of("Unable to save image, wrong file type"));
            }
            try {
                String originalFilename = image.getOriginalFilename();
                String fileExtension = null;
                if (originalFilename != null && !originalFilename.isEmpty()) {
                    int dotIndex = originalFilename.lastIndexOf('.');
                    if (dotIndex > 0 && dotIndex < originalFilename.length() - 1) {
                        fileExtension = originalFilename.substring(dotIndex + 1);
                    }
                }
                // create image, overwrite newsDto.imagePath
                String uuid = UUID.randomUUID().toString();
                Resource resource = resourceLoader.getResource("classpath:");
                // get absolute path of /backend
                Path path = Paths.get(resource.getFile().getAbsolutePath()).getParent().getParent();
                String imgPath = uuid + "." + fileExtension;
                filePath = path + "/src/eventImages/" + imgPath;
                File imgFile = new File(filePath);
                // create newsCover directory if it doesn't exist
                if (!imgFile.getParentFile().exists()) {
                    if (!imgFile.getParentFile().mkdirs()) {
                        LOGGER.warn("Unable to create directory for image");
                        throw new FatalException("Unable to create directory for image");
                    }
                }
                image.transferTo(imgFile);
                return FileDto.CreateDtoBuilder.aCreateDto().withImagePath(imgPath).build();
            } catch (IOException e) {
                LOGGER.warn("Unable to save image" + e.getMessage());
                throw new FatalException("Unable to save image");
            }
        } else {
            throw new ValidationException("Validation error", List.of("no Image given"));
        }
    }

    private boolean isImage(MultipartFile file) {
        String[] allowedContentTypes = {"image/jpeg", "image/png", "image/jpg", "image/gif"};
        // Check if the file's content type is one of the allowed image types
        for (String contentType : allowedContentTypes) {
            if (contentType.equals(file.getContentType())) {
                return true;
            }
        }
        return false;
    }
}
