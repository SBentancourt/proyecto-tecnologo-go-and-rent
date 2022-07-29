package com.tecnologo.grupo3.goandrent.services.s3Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class S3ServiceTest {

    @InjectMocks
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void uploadFile() throws IOException {
        File file = new File("src/test/resources/prueba1.jpg");
        FileInputStream input = new FileInputStream(file);
        s3Service.uploadFile("prueba1.jpg", input, "alojamiento-1");
    }

    @Test
    void deleteObject() {
        s3Service.deleteObject("prueba1.jpg", "alojamiento-1");
    }

    @Test
    void deleteFolder() {
        List<String> list = new ArrayList<>();
        list.add("prueba1.jpg");
        s3Service.deleteFolder(list);
    }
}