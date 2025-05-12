package com.samratalam.spring_batch_practice.customer.controller;


import com.samratalam.spring_batch_practice.attachment.AttachmentService;
import com.samratalam.spring_batch_practice.customer.service.CustomerRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class V1CustomerRegistrationController {
    private final AttachmentService attachmentService;
    private final CustomerRegistrationService customerRegistrationService;

    @GetMapping("/import")
    ResponseEntity<?> importCustomers(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            if (!file.getContentType().equals("text/csv")) {
                return ResponseEntity.ok("wrong file type");
            }
        }
        String filePath = attachmentService.uploadFileAndReturnFileLocation(file);
        customerRegistrationService.importCustomers(filePath);
        return ResponseEntity.ok("ok");
    }
}
