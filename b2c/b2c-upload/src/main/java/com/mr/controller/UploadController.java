package com.mr.controller;

import com.mr.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "upload")
public class UploadController {

    @Autowired
    private UploadService service;

    @PostMapping("image")
    public ResponseEntity<String> upload(@RequestParam("file")MultipartFile file) throws IOException {

       String url= service.uploadImg(file);

        return ResponseEntity.ok(url);
    }


}
