package be.kdg.kandoe.service.implementation;

import be.kdg.kandoe.service.declaration.StorageService;
import be.kdg.kandoe.service.exception.StorageServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StorageServiceImpl implements StorageService{

    private final Path rootLocation;

    private String location = "/home/sven/Desktop/IntegratieProject2/src/main/resources/profilePictures"; //Here we will store files

    public StorageServiceImpl() {
        this.rootLocation = Paths.get(location);
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageServiceException("Could not initialize storage");
        }
    }

    @Override
    public void store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageServiceException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageServiceException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
            throw new StorageServiceException("Could not store file");
        }

    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        }
        catch (IOException e) {
            //todo
            throw new StorageServiceException("Could not store file");
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageServiceException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageServiceException("Could not read file");
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

}
