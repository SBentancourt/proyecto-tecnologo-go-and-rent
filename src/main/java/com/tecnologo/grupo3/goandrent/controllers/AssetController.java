package com.tecnologo.grupo3.goandrent.controllers;
import com.tecnologo.grupo3.goandrent.services.s3Service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/assets")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class AssetController {

    @Autowired
    private S3Service s3Service;

    @PostMapping("/upload")
    public String handleUploadForm(@RequestParam("file") MultipartFile multipart) {
        String fileName = multipart.getOriginalFilename();
        String message = "";

        try {
            s3Service.uploadFile(fileName, multipart.getInputStream(), "prueba1");
            message = "Your file has been uploaded successfully!";
        } catch (Exception ex) {
            message = "Error uploading file: " + ex.getMessage();
        }

        return message;
    }

    @DeleteMapping(value = "/delete-object", params = "key")
    void deleteObject(@RequestParam String key){
        s3Service.deleteObject(key, "prueba1");
    }
}
