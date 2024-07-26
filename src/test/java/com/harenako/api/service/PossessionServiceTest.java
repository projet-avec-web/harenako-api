package com.harenako.api.service;

import static java.nio.file.Files.createTempFile;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.harenako.api.endpoint.rest.model.Possession;
import com.harenako.api.file.BucketComponent;
import com.harenako.api.file.BucketConf;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import school.hei.patrimoine.serialisation.Serialiseur;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.S3Object;

public class PossessionServiceTest {

  @Mock private Serialiseur<Possession> serialiseur;
  @Mock private BucketComponent bucketComponent;
  @Mock private BucketConf bucketConf;
  @Mock private software.amazon.awssdk.services.s3.S3Client s3Client;

  @InjectMocks private PossessionService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(bucketConf.getS3Client()).thenReturn(s3Client);
  }

  @Test
  public void testGetPossessions() throws IOException {
    String nom_patrimoine = "patrimoine-test";
    List<S3Object> s3Objects = new ArrayList<>();
    s3Objects.add(
        S3Object.builder().key("patrimoines/" + nom_patrimoine + "/possessions/possess1").build());
    s3Objects.add(
        S3Object.builder().key("patrimoines/" + nom_patrimoine + "/possessions/possess2").build());

    ListObjectsV2Response listObjectsResponse =
        ListObjectsV2Response.builder().contents(s3Objects).build();
    when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsResponse);

    Possession possession1 = new Possession();
    Possession possession2 = new Possession();
    String serializedData1 = mockDataSerialiser();
    String serializedData2 = mockDataSerialiser();

    File file1 = createTempFile("possess1", "").toFile();
    File file2 = createTempFile("possess2", "").toFile();
    writeContentToFile(file1, serializedData1);
    writeContentToFile(file2, serializedData2);

    when(bucketComponent.download(anyString())).thenReturn(file1).thenReturn(file2);
    when(serialiseur.deserialise(serializedData1)).thenReturn(possession1);
    when(serialiseur.deserialise(serializedData2)).thenReturn(possession2);

    List<Possession> actualPossessions = service.getPossessions(nom_patrimoine);

    assertNotNull(actualPossessions);
    assertEquals(2, actualPossessions.size());

    file1.delete();
    file2.delete();
  }

  @Test
  public void testGetPossessionByNom() throws IOException {
    String nom_patrimoine = "patrimoine-test";
    String nom_possession = "possess1";
    Possession expectedPossession = new Possession();
    String serializedData = mockDataSerialiser();

    File file = createTempFile(nom_possession, "").toFile();
    writeContentToFile(file, serializedData);

    when(bucketComponent.download(anyString())).thenReturn(file);
    when(serialiseur.deserialise(serializedData)).thenReturn(expectedPossession);

    Possession actualPossession = service.getPossessionByNom(nom_patrimoine, nom_possession);

    assertNotNull(actualPossession);
    assertEquals(expectedPossession, actualPossession);

    file.delete();
  }

  @Test
  public void testCrupdPossessions() throws IOException {
    String nom_patrimoine = "patrimoine-test";
    Possession possession1 = new Possession();
    Possession possession2 = new Possession();
    possession1.setNom("possess1");
    possession2.setNom("possess2");
    List<Possession> possessions = List.of(possession1, possession2);

    String serializedData1 = mockDataSerialiser();
    String serializedData2 = mockDataSerialiser();

    File file1 = createTempFile("possess1", "").toFile();
    File file2 = createTempFile("possess2", "").toFile();
    writeContentToFile(file1, serializedData1);
    writeContentToFile(file2, serializedData2);

    when(bucketComponent.download(anyString())).thenReturn(file1).thenReturn(file2);
    when(serialiseur.deserialise(anyString())).thenReturn(possession1).thenReturn(possession2);
    when(bucketComponent.upload(any(File.class), anyString())).thenReturn(null);

    List<Possession> updatedPossessions = service.crupdPossessions(nom_patrimoine, possessions);

    verify(bucketComponent, times(2)).upload(any(File.class), anyString());
    verify(s3Client, times(2)).deleteObject(any(DeleteObjectRequest.class));

    assertNotNull(updatedPossessions);
    assertEquals(possessions.size(), updatedPossessions.size());

    file1.delete();
    file2.delete();
  }

  @Test
  public void testRemovePossession() {
    String nom_patrimoine = "patrimoine-test";
    String nom_possession = "possess1";

    when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
        .thenReturn(DeleteObjectResponse.builder().build());

    String result = service.deletePossession(nom_patrimoine, nom_possession);

    assertEquals("delete suuccessfully", result);
  }

  @Test
  public void testDeletePossession() {
    String nom_patrimoine = "patrimoine-test";
    String nom_possession = "possess1";
    when(bucketComponent.download(anyString())).thenThrow(new RuntimeException("File not found"));
    service.deletePossession(nom_patrimoine, nom_possession);
    assertThrows(
        RuntimeException.class,
        () -> {
          service.getPossessionByNom(nom_patrimoine, nom_possession);
        });
  }

  private void writeContentToFile(File file, String content) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(content);
    }
  }

  private String mockDataSerialiser() {
    Serialiseur<Possession> serialiseur = new Serialiseur<>();
    Possession originalPossession = new Possession();
    return serialiseur.serialise(originalPossession);
  }
}
