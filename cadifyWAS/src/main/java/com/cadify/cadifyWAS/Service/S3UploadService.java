package com.cadify.cadifyWAS.Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class S3UploadService {

    private final AmazonS3 amazonS3;
    private final String bucketName;

    @Autowired
    public S3UploadService(AmazonS3 amazonS3, @Value("${aws.s3.bucket.name}") String bucketName) {
        this.amazonS3 = amazonS3;
        this.bucketName = bucketName;
    }

    public void uploadFile(String fileName, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        InputStream inputStream = file.getInputStream();

        amazonS3.putObject(bucketName, fileName, inputStream, metadata);
    }

    public S3Object downloadFile(String fileName) {
        return amazonS3.getObject(bucketName, fileName);
    }

}
