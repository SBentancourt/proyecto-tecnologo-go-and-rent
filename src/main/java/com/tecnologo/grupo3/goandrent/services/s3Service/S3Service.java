package com.tecnologo.grupo3.goandrent.services.s3Service;

import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.*;

import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

@Service
public class S3Service {
    private final static String BUCKET = "produccion-proyectofinal-imagenes";
    private final AwsBasicCredentials credentials = AwsBasicCredentials.create("AKIA53NZALD2B4NZJQSH", "m041s+dY29eL75rZt790lZsC4pVnK0kyIvU8DMF8");
    private final StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

    private final S3Client client = S3Client.builder().region(Region.of("us-east-1"))
            .credentialsProvider(credentialsProvider)
            .build();

    public void uploadFile(String fileName, InputStream inputStream, String folder)
            throws S3Exception, AwsServiceException, SdkClientException, IOException {

        PutObjectRequest request = PutObjectRequest
                .builder()
                .bucket(BUCKET)
                // -- Si la carpeta ya existe no la vuelve a crear
                .key("alojamientos/"+folder+"/"+fileName)
                .build();
        System.out.println("REQUEST");
        System.out.println(request);

        client.putObject(request,
                RequestBody.fromInputStream(inputStream, inputStream.available()));
    }

    public void deleteObject(String key, String folder){
        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                .builder().bucket(BUCKET).key("alojamientos/"+folder+"/"+key).build();
        client.deleteObject(deleteObjectRequest);
    }

    public void deleteFolder(List<String> images){
        for (String s: images){
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest
                    .builder().bucket(BUCKET).key("alojamientos/"+s).build();

            client.deleteObject(deleteObjectRequest);
        }
    }

}
