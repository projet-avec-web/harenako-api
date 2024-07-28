package com.harenako.api.service;

import static java.nio.file.Files.createTempFile;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.harenako.api.endpoint.rest.model.Patrimoine;
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

public class PatrimoineServiceTest {

  @Mock private Serialiseur<Patrimoine> serialiseur;

  @Mock private BucketComponent bucketComponent;

  @Mock private BucketConf bucketConf;

  @Mock private software.amazon.awssdk.services.s3.S3Client s3Client;

  @InjectMocks private PatrimoineService service;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
    when(bucketConf.getS3Client()).thenReturn(s3Client);
  }

  @Test
  public void testSerializationDeserialization() {
    Serialiseur<Patrimoine> serialiseur = new Serialiseur<>();
    Patrimoine originalPatrimoine = new Patrimoine();
    String serializedData = serialiseur.serialise(originalPatrimoine);

    assertNotNull(serializedData, "Serialized data should not be null");

    Patrimoine deserializedPatrimoine = serialiseur.deserialise(serializedData);

    assertNotNull(deserializedPatrimoine, "Deserialized patrimoine should not be null");
    assertEquals(
        originalPatrimoine,
        deserializedPatrimoine,
        "Deserialized patrimoine should match the original");
  }

  @Test
  public void testGetPatrimoines() throws IOException {
    List<S3Object> s3Objects = new ArrayList<>();
    s3Objects.add(
        S3Object.builder().key("patrimoines/patrimoine_patrimoine-test1/patrimoine-test1").build());
    s3Objects.add(
        S3Object.builder().key("patrimoines/patrimoine_patrimoine-test2/patrimoine-test2").build());

    ListObjectsV2Response listObjectsResponse =
        ListObjectsV2Response.builder().contents(s3Objects).build();

    when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsResponse);

    Patrimoine patrimoine1 = new Patrimoine();
    Patrimoine patrimoine2 = new Patrimoine();
    String serializedData1 = mockDataSerialiser();
    String serializedData2 = mockDataSerialiser();

    File file1 = createTempFile("patrimoine-test1", "").toFile();
    File file2 = createTempFile("patrimoine-test2", "").toFile();
    writeContentToFile(file1, serializedData1);
    writeContentToFile(file2, serializedData2);

    when(bucketComponent.download(anyString())).thenReturn(file1).thenReturn(file2);
    when(serialiseur.deserialise(serializedData1)).thenReturn(patrimoine1);
    when(serialiseur.deserialise(serializedData2)).thenReturn(patrimoine2);

    List<Patrimoine> actualPatrimoines = service.getPatrimoines();

    assertNotNull(actualPatrimoines);
    assertEquals(2, actualPatrimoines.size());
    assertEquals(new Patrimoine(), actualPatrimoines.get(0));

    file1.delete();
    file2.delete();
  }

  @Test
  public void testGetPatrimoineByNom() throws IOException {
    String nom = "patrimoine-test";
    S3Object s3Objects =
        S3Object.builder().key("patrimoines/patrimoine_patrimoine-test/patrimoine-test").build();

    ListObjectsV2Response listObjectsResponse =
        ListObjectsV2Response.builder().contents(s3Objects).build();

    when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(listObjectsResponse);

    Patrimoine expectedPatrimoine = new Patrimoine();
    String serializedData = mockDataSerialiser();

    File file = createTempFile(nom, "").toFile();
    writeContentToFile(file, serializedData);

    when(bucketComponent.download(anyString())).thenReturn(file);
    when(serialiseur.deserialise(serializedData)).thenReturn(expectedPatrimoine);

    Patrimoine actualPatrimoine = service.getPatrimoineByNom(nom);

    assertNotNull(actualPatrimoine);
    assertEquals(expectedPatrimoine, actualPatrimoine);

    file.delete();
  }

  @Test
  public void testCrupdPatrimoines() {
    Patrimoine patrimoine1 = new Patrimoine();
    Patrimoine patrimoine2 = new Patrimoine();
    List<Patrimoine> patrimoines = List.of(patrimoine1, patrimoine2);

    when(bucketComponent.upload(any(File.class), anyString())).thenReturn(null);

    List<Patrimoine> updatedPatrimoines = service.crupdPatrimoines(patrimoines);

    verify(bucketComponent, times(2)).upload(any(File.class), anyString());
    assertNotNull(updatedPatrimoines);
    assertEquals(patrimoines.size(), updatedPatrimoines.size());
  }

  private void writeContentToFile(File file, String content) throws IOException {
    try (FileWriter writer = new FileWriter(file)) {
      writer.write(content);
    }
  }

  @Test
  public void testDeletePatrimoine() {
    String nom = "patrimoine-test";
    when(s3Client.deleteObject(any(DeleteObjectRequest.class)))
        .thenReturn(DeleteObjectResponse.builder().build());
    service.deletePatrimoine(nom);
    assertNull(service.getPatrimoineByNom(nom));
  }

  private String mockDataSerialiser() {
    Serialiseur<Patrimoine> serialiseur = new Serialiseur<>();
    Patrimoine originalPatrimoine = new Patrimoine();
    return serialiseur.serialise(originalPatrimoine);
  }
}
