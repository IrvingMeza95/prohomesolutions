package com.EF2.prohomesolutions.services;

import com.EF2.prohomesolutions.exceptions.MyException;
import com.EF2.prohomesolutions.models.File;
import com.EF2.prohomesolutions.repositories.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private FileRepo fileRepo;

    public File saveImage(MultipartFile file) throws MyException {
        if (file != null) {
            try {
                File image = File.builder()
                        .mime(file.getContentType())
                        .name(file.getName())
                        .content(file.getBytes())
                        .build();
                return fileRepo.save(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;
    }

    public File update(MultipartFile file, String fileId) throws MyException{
        if (file != null) {
            try {
                File image = new File();
                if (fileId != null) {
                    Optional<File> response = fileRepo.findById(fileId);

                    if (response.isPresent()) {
                        image = response.get();
                    }
                }
                image.setMime(file.getContentType());
                image.setName(file.getName());
                image.setContent(file.getBytes());
                return fileRepo.save(image);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }
        return null;

    }

    public File getOne(String id) {
        return fileRepo.getOne(id);
    }
}
