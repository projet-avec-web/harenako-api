package com.harenako.api.service;

import static java.nio.file.Files.createTempDirectory;

import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.file.BucketComponent;
import com.harenako.api.file.BucketConf;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.serialisation.Serialiseur;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@AllArgsConstructor
public class PatrimoineService {
  private final Serialiseur<Patrimoine> serialiseur = new Serialiseur<Patrimoine>();
  private final BucketComponent bucketComponent;
  private final BucketConf bucketConf;
  private final String PATRIMOINE_KEY = "patrimoines/";

  public List<Patrimoine> getPatrimoines() {
    List<File> allPatrimoineFiles = new ArrayList<>();

    ListObjectsV2Request listObjectsReqManual =
        ListObjectsV2Request.builder()
            .bucket(bucketComponent.getBucketName())
            .prefix(PATRIMOINE_KEY)
            .build();

    ListObjectsV2Response listObjResponse =
        bucketConf.getS3Client().listObjectsV2(listObjectsReqManual);

    for (S3Object s3Object : listObjResponse.contents()) {
      String key = s3Object.key();
      File patrimoineFile = bucketComponent.download(key);
      allPatrimoineFiles.add(patrimoineFile);
    }

    return allPatrimoineFiles.stream()
        .map(f -> convertToPatrimoine(f))
        .collect(Collectors.toList());
  }

  public Patrimoine getPatrimoineByNom(String nom) {
    String bucketKey = PATRIMOINE_KEY + nom + "/" + nom;
    try {
      File patrimoineFile = bucketComponent.download(bucketKey);
      if (patrimoineFile == null) return null;
      return convertToPatrimoine(patrimoineFile);
    } catch (NoSuchKeyException e) {
      System.err.println("File not found in S3 bucket for key: " + bucketKey);
      return null;
    } catch (S3Exception e) {
      throw new RuntimeException(
          "Error occurred while finding patrimoine file for key: " + bucketKey, e);
    }
  }

  public List<Patrimoine> crupdPatrimoines(List<Patrimoine> patrimoines) {
    for (Patrimoine patrimoine : patrimoines) {
      deletePatrimoine(patrimoine.getNom());
      createPatrimoine(patrimoine);
    }
    return patrimoines;
  }

  private void createPatrimoine(Patrimoine patrimoine) {
    try {
      String patrimoineStr = serialiseur.serialise(patrimoine);
      String patrimoineDirectory = patrimoine.getNom();
      File patrimoineDirectoryToUpload = createTempDirectory(patrimoineDirectory).toFile();
      File patrimoineFile =
          new File(patrimoineDirectoryToUpload.getAbsolutePath() + "/" + patrimoine.getNom());
      writeContent(patrimoineStr, patrimoineFile);
      String directoryBucketKey = PATRIMOINE_KEY + patrimoineDirectory;
      bucketComponent.upload(patrimoineDirectoryToUpload, directoryBucketKey);
    } catch (IOException e) {
      throw new RuntimeException("Error creating patrimoine file", e);
    }
  }

  public void deletePatrimoine(String nom) {
    String bucketKey = PATRIMOINE_KEY + nom + "/" + nom;
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder()
            .bucket(bucketComponent.getBucketName())
            .key(bucketKey)
            .build();
    bucketConf.getS3Client().deleteObject(deleteObjectRequest);
  }

  private Patrimoine convertToPatrimoine(File file) {
    return serialiseur.deserialise(readContent(file));
  }

  private void writeContent(String content, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    writer.write(content);
    writer.close();
  }

  private String readContent(File file) {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      return br.readLine();
    } catch (IOException e) {
      throw new RuntimeException("Error reading patrimoine file", e);
    }
  }
}
