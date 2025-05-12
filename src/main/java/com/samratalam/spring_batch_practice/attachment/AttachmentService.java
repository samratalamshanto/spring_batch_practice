package com.samratalam.spring_batch_practice.attachment;

import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {
    String uploadFileAndReturnFileLocation(MultipartFile multipartFile);
}
