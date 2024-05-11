package com.masterpiece.ToitEnVueBackEnd.controller.file;

import com.masterpiece.ToitEnVueBackEnd.model.file.File;
import com.masterpiece.ToitEnVueBackEnd.repository.housing.HousingRepository;
import com.masterpiece.ToitEnVueBackEnd.service.file.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;
@Controller
@RequestMapping("api/v1/files")
public class FileController {
    @Autowired
    FileStorageService storageService;
    @Autowired
    HousingRepository housingRepository;
    @GetMapping
    public ResponseEntity<List<File>> getListFiles() {
        List<File> files = storageService.loadAllRelative().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FileController.class, "getFile", path.getFileName().toString()).build().toString();

            File file = new File();
            file.setFile_name(filename);
            return file;
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(files);
    }


    @GetMapping("/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAll() {
        try {
            storageService.deleteAll();
            return ResponseEntity.ok("Tous les fichiers ont été supprimés avec succès.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la suppression des fichiers: " + e.getMessage());
        }
    }
}
