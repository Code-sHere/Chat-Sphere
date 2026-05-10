package com.chatapp.demo.Controllers;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

@RestController
public class AttachmentController {

    @PostMapping("/upload")
    public Map<String, String> upload(
            @RequestParam("file") MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename()
                .replace(" ", "_");

        String fileName = UUID.randomUUID()
                + "_"
                + originalName;

        Path uploadPath = Paths.get("uploads");

        if (!Files.exists(uploadPath)) {

            Files.createDirectories(
                    uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(file.getInputStream(), filePath);

        Map<String, String> respons = new HashMap<>();

        respons.put("fileUrl", "/uploads/" + fileName);

        respons.put(
                "fileName",
                file.getOriginalFilename());

        respons.put(
                "fileType",
                file.getContentType());

        return respons;

    }

}
