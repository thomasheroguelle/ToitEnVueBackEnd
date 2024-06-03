package com.masterpiece.ToitEnVueBackEnd.service.impl.file;

import com.masterpiece.ToitEnVueBackEnd.exceptions.file.EmptyFileException;
import com.masterpiece.ToitEnVueBackEnd.service.file.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path root = Paths.get("uploads");

    @Override
    public void init() {
        try {
            Files.createDirectories(root);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public void save(MultipartFile file) {
        if (file.isEmpty()) {
            throw new EmptyFileException("Veuillez insérer un fichier");
        }
        try {
            this.init();
            Files.copy(file.getInputStream(), this.root.resolve(Objects.requireNonNull(file.getOriginalFilename())));
        } catch (Exception e) {
            if (e instanceof FileAlreadyExistsException) {
                throw new RuntimeException("Un fichier portant ce nom existe déjà.");
            }
            throw new RuntimeException("Une erreur s'est produite lors de l'enregistrement du fichier : " + e.getMessage());
        }
    }


    @Override
    public Resource load(String filename) {
        try {
            Path file = root.resolve(filename); // on peut revenir a la racine du fichier avec le resolve , c'est ce qui fait que l'attaquant peut accéder à tout fichier
            Resource resource = new UrlResource(file.toUri()); // trouver une méthode qui empeche d'aller a la racine et transformer les filename en uuid.content-type

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    @Override
    public void deleteAll() {
        loadAll().forEach(path -> {
                    try {
                        Files.delete(path);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public Stream<Path> loadAllRelative() {
        return this.loadAll().map(this.root::relativize);
    }

    private Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root));
        } catch (IOException e) {
            throw new RuntimeException("Could not load the files!");
        }
    }
}
