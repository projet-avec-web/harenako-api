package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PossessionController;
import com.harenako.api.endpoint.rest.controller.PossessionData;
import com.harenako.api.endpoint.rest.model.PossessionAvecType.TypeEnum;
import com.harenako.api.service.PossessionService;
import com.harenako.api.service.mapper.ArgentObjectMapper;
import com.harenako.api.service.mapper.FluxArgentObjectMapper;
import com.harenako.api.service.mapper.MaterielObjectMapper;

import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Possession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

public class PossessionControllerTest {

  private final String nom_patrimoine = "patrimoine-test";

  @Mock private PossessionService service;

  @MockBean private ArgentObjectMapper argentMapper;

  @MockBean private FluxArgentObjectMapper fluxArgentMapper;

  @MockBean private MaterielObjectMapper materielMapper;

  @InjectMocks private PossessionController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPossessionByPatrimoine() {
    List<Possession> possessions = new ArrayList<>();
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));

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
    Possession possession = new Argent("possession-test", LocalDate.now(), 0);

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
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));

    when(service.crupdPossessions(nom_patrimoine, possessions)).thenReturn(possessions);

    List<PossessionData> possessionsReq = new ArrayList<>();
    possessionsReq.add(new PossessionData(TypeEnum.ARGENT, new com.harenako.api.endpoint.rest.model.Argent(), null, null));
    possessionsReq.add(new PossessionData(TypeEnum.ARGENT, new com.harenako.api.endpoint.rest.model.Argent(), null, null));
    ResponseEntity<?> response =
        controller.crupdatePossessionInPatrimoine(nom_patrimoine, possessionsReq);
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
