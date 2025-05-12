package com.samratalam.spring_batch_practice.attachment;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    @Value("${bulk.file.upload.url}")
    String fileUploadUrl;


    @Override
    public String uploadFileAndReturnFileLocation(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        fileName = String.format("%s_%s_%s", System.currentTimeMillis(), 2, fileName);
        String fileUploadLocation = "";
        try {

            String filePath = fileUploadUrl;
            File directory = new File(filePath);

            if (!directory.exists()) {
                directory.mkdirs();
            }


            fileUploadLocation = filePath + File.separator + fileName;
            File file = new File(fileUploadLocation);

            // multipartFile.transferTo(filePath);

            file.createNewFile();
            FileOutputStream output = new FileOutputStream(file);
            output.write(multipartFile.getBytes());
            output.close();

        } catch (Exception e) {
            //log.error(e.getMessage());
        }
        return fileUploadLocation;
    }
}
