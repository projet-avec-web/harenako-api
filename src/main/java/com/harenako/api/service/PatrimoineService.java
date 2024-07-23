package com.harenako.api.service;

import static java.nio.file.Files.createTempDirectory;

import com.harenako.api.PojaGenerated;
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
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.modele.Personne;
import school.hei.patrimoine.serialisation.Serialiseur;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

@Service
@PojaGenerated
@AllArgsConstructor
public class PatrimoineService {
  private final Serialiseur serialiseur = new Serialiseur();
  private final BucketComponent bucketComponent;
  private final BucketConf bucketConf;
  private final String PATRIMOINE_KEY = "patrimoines/";

  public List<Patrimoine> getPatrimoines() {
    return getAllPatrimoines();
  }

  public Patrimoine getPatrimoineByNom(String nom) {
    return getPatrimoine(nom);
  }

  public List<Patrimoine> crupdPatrimoines(List<Patrimoine> patrimoines) {
    for (Patrimoine patrimoine : patrimoines) {
      createPatrimoine(patrimoine);
    }
    return patrimoines;
  }

  @SuppressWarnings("unchecked")
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

  private List<Patrimoine> getAllPatrimoines() {
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

    List<Patrimoine> patrimoines = new ArrayList<>();
    for (File file : allPatrimoineFiles) {
      Patrimoine patrimoine = convertToPatrimoine(file);
      patrimoines.add(patrimoine);
    }

    return patrimoines;
  }

  private Patrimoine getPatrimoine(String nom) {
    String bucketKey = PATRIMOINE_KEY + nom;
    File patrimoineFile = bucketComponent.download(bucketKey);
    return convertToPatrimoine(patrimoineFile);
  }

  private Patrimoine convertToPatrimoine(File file) {
    try {
      String patrimoineStr = readContent(file);
      return (Patrimoine) serialiseur.deserialise(patrimoineStr);
    } catch (IOException e) {
      throw new RuntimeException("Error convert patrimoine file to class", e);
    }
  }

  private school.hei.patrimoine.modele.Patrimoine convertToCorrectPatrimoine(
      Patrimoine patrimoine) {
    school.hei.patrimoine.modele.Patrimoine patrimoineTransformed =
        new school.hei.patrimoine.modele.Patrimoine(
            patrimoine.getNom(),
            new Personne(patrimoine.getPossesseur().getNom()),
            patrimoine.getT(),
            Set.of());
    return patrimoineTransformed;
  }

  private void writeContent(String content, File file) throws IOException {
    FileWriter writer = new FileWriter(file);
    writer.write(content);
    writer.close();
  }

  private String readContent(File file) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
      String line;
      if ((line = br.readLine()) != null) {
        return line;
      }
    } catch (IOException e) {
      throw new RuntimeException("Error reading patrimoine file", e);
    }
    return null;
  }
}
