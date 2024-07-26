package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PossessionController;
import com.harenako.api.endpoint.rest.model.Possession;
import com.harenako.api.service.PossessionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class PossessionControllerTest {

  private final String nom_patrimoine = "patrimoine-test";

  @Mock private PossessionService service;

  @InjectMocks private PossessionController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPossessionByPatrimoine() {
    List<Possession> possessions = new ArrayList<>();
    possessions.add(new Possession());
    possessions.add(new Possession());

    when(service.getPossessions(nom_patrimoine)).thenReturn(possessions);

    ResponseEntity<?> response = controller.getPossessionByPatrimoine(0, 10, "patrimoine-test");
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<?> responseBody = (List<?>) response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
  }

  @Test
  public void testGetPossessionByNomByPatrimoine() {
    String nom_possession = "possession-test";
    Possession possession = new Possession();
    possession.setNom(nom_possession);

    when(service.getPossessionByNom(nom_patrimoine, nom_possession)).thenReturn(possession);

    ResponseEntity<?> response =
        controller.getPossessionByNomByPatrimoine(nom_patrimoine, nom_possession);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Possession responseBody = (Possession) response.getBody();
    assertNotNull(responseBody);
    assertEquals(nom_possession, responseBody.getNom());
  }

  @Test
  public void testCrupdatePatrimoine() {
    List<Possession> possessions = new ArrayList<>();
    possessions.add(new Possession());
    possessions.add(new Possession());

    when(service.crupdPossessions(nom_patrimoine, possessions)).thenReturn(possessions);

    ResponseEntity<?> response =
        controller.crupdatePossessionInPatrimoine(nom_patrimoine, possessions);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<?> responseBody = (List<?>) response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
  }

  @Test
  public void testDeletePossessionByNomByPatrimoine() {
    String nom_possession = "possession-test";

    when(service.deletePossession(nom_patrimoine, nom_possession))
        .thenReturn("delete successfully");

    ResponseEntity<?> response =
        controller.deletePossessionByNomByPatrimoine(nom_patrimoine, nom_possession);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    String responseBody = (String) response.getBody();
    assertNotNull(responseBody);
    assertEquals("delete successfully", responseBody);
  }
}
