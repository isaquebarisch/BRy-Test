package com.test.demo.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.test.demo.repository.PhotoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import com.test.demo.model.Photo;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable Long id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()) {
            byte[] data = photoOptional.get().getData();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public Photo createPhoto(@RequestParam("file") MultipartFile file) {
        try {
            Photo photo = new Photo();
            photo.setData(file.getBytes());
            return photoRepository.save(photo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save photo");
        }
    }

    @PutMapping("/{id}")
    public Photo updatePhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()) {
            try {
                Photo photo = photoOptional.get();
                photo.setData(file.getBytes());
                return photoRepository.save(photo);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to update photo");
            }
        } else {
            throw new RuntimeException("Photo not found");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePhoto(@PathVariable Long id) {
        Optional<Photo> photoOptional = photoRepository.findById(id);
        if (photoOptional.isPresent()) {
            photoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
