package com.harenako.api.service;

import com.harenako.api.endpoint.rest.model.Possession;
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
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@AllArgsConstructor
public class PossessionService {
  private final Serialiseur<Possession> serialiseur = new Serialiseur<Possession>();
  private final BucketComponent bucketComponent;
  private final BucketConf bucketConf;
  private final String PATRIMOINE_KEY = "patrimoines/";

  public List<Possession> getPossessions(String nom_patrimoine) {
    List<File> allPossessionsFiles = new ArrayList<>();

    ListObjectsV2Request listObjectsReqManual =
        ListObjectsV2Request.builder()
            .bucket(bucketComponent.getBucketName())
            .prefix(PATRIMOINE_KEY + nom_patrimoine + "/possessions/")
            .build();

    ListObjectsV2Response listObjResponse =
        bucketConf.getS3Client().listObjectsV2(listObjectsReqManual);

    for (S3Object s3Object : listObjResponse.contents()) {
      String key = s3Object.key();
      File possessionFile = bucketComponent.download(key);
      allPossessionsFiles.add(possessionFile);
    }

    List<Possession> possessions = new ArrayList<>();
    for (File file : allPossessionsFiles) {
      Possession possession = convertToPossession(file);
      possessions.add(possession);
    }

    return allPossessionsFiles.stream()
        .map(f -> convertToPossession(f))
        .collect(Collectors.toList());
  }

  public Possession getPossessionByNom(String nom_patrimoine, String nom_possession) {
    String bucketKey = PATRIMOINE_KEY + nom_patrimoine + "/possessions/" + nom_possession;
    File possessionFile = bucketComponent.download(bucketKey);
    if (possessionFile == null) return null;
    return convertToPossession(possessionFile);
  }

  public List<Possession> crupdPossessions(String nom_patrimoine, List<Possession> possessions) {
    for (Possession possession : possessions) {
      deletePossession(nom_patrimoine, nom_patrimoine);
      createPossession(nom_patrimoine, possession);
    }
    return possessions;
  }

  private void createPossession(String nom_patrimoine, Possession possession) {
    try {
      String possessionStr = serialiseur.serialise(possession);
      File possessionFile = new File(possession.getNom());
      writeContent(possessionStr, possessionFile);
      String directoryBucketKey =
          PATRIMOINE_KEY + nom_patrimoine + "/possessions/" + possession.getNom();
      bucketComponent.upload(possessionFile, directoryBucketKey);
    } catch (IOException e) {
      throw new RuntimeException("Error creating possession file", e);
    }
  }

  public String deletePossession(String nom_patrimoine, String nom_possession) {
    String bucketKey = PATRIMOINE_KEY + nom_patrimoine + "/possessions/" + nom_possession;
    DeleteObjectRequest deleteObjectRequest =
        DeleteObjectRequest.builder()
            .bucket(bucketComponent.getBucketName())
            .key(bucketKey)
            .build();
    bucketConf.getS3Client().deleteObject(deleteObjectRequest);
    return "delete suuccessfully";
  }

  private Possession convertToPossession(File file) {
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
