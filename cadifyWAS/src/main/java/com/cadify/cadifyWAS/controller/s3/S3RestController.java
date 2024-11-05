package com.cadify.cadifyWAS.controller.s3;

import com.amazonaws.services.s3.model.S3Object;
import com.cadify.cadifyWAS.service.S3UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/s3")
public class S3RestController {


    private final S3UploadService s3Service;

    @Autowired
    public S3RestController(S3UploadService s3Service) {
        this.s3Service = s3Service;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("userId") String userId, @RequestParam("file") MultipartFile file) {
        try {
            s3Service.uploadFile(userId, file.getOriginalFilename(), file);
            return ResponseEntity.ok("File uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }

    @GetMapping("/download/{userId}/{fileName}")
    public ResponseEntity<S3Object> downloadFile(@PathVariable String userId, @PathVariable String fileName) {
        S3Object s3Object = s3Service.downloadFile(userId, fileName);
        return ResponseEntity.ok(s3Object);
    }


}
