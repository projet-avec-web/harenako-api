package com.harenako.api.endpoint.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PossessionController;
import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import com.harenako.api.service.PossessionService;
import com.harenako.api.service.mapper.ArgentObjectMapper;
import com.harenako.api.service.mapper.FluxArgentObjectMapper;
import com.harenako.api.service.mapper.MaterielObjectMapper;
import com.harenako.api.service.mapper.PossessionsObjectMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.Possession;

public class PossessionControllerTest {
  // This test depends on the controller
  @InjectMocks private PossessionController controller;

  private final String nom_patrimoine = "patrimoine-test";

  @Mock private PossessionService service;

  @Mock private PossessionsObjectMapper possessionsObjectMapper;

  @MockBean private ArgentObjectMapper argentMapper;

  @MockBean private FluxArgentObjectMapper fluxArgentMapper;

  @MockBean private MaterielObjectMapper materielMapper;

  private static List<Possession> possessions =
      List.of(
          new Argent("possession-test", LocalDate.now(), 0),
          new Argent("possession-test", LocalDate.now(), 0));

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPossessionByPatrimoine() {
    when(service.getPossessions(nom_patrimoine)).thenReturn(possessions);

    ResponseEntity<Page<PossessionAvecType>> response = controller.getPossessionByPatrimoine(0, 10, nom_patrimoine);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Page<PossessionAvecType> responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.getContent().size());
  }

  @Test
  public void testGetPossessionByNomByPatrimoine() {
    String nom_possession = "possession-test";
    Possession possession = new Argent("possession-test", LocalDate.now(), 0);

    when(service.getPossessionByNom(nom_patrimoine, nom_possession)).thenReturn(possession);

    ResponseEntity<Page<PossessionAvecType>> response =
        controller.getPossessionByNomByPatrimoine(nom_patrimoine, nom_possession);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Page<PossessionAvecType> responseBody =  response.getBody();
    assertNotNull(responseBody);
    assertEquals(nom_possession, responseBody.getContent().get(0));
  }

  @Test
  public void testCrupdatePossessions() {
    List<Possession> possessions = new ArrayList<>();
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));
    possessions.add(new Argent("possession-test", LocalDate.now(), 0));

    when(service.crupdPossessions(nom_patrimoine, possessions)).thenReturn(possessions);

    List<PossessionAvecType> possessionsReq = new ArrayList<>();
    possessionsReq.add(new PossessionAvecType());
    possessionsReq.add(new PossessionAvecType());
    ResponseEntity<Page<PossessionAvecType>> response =
        controller.crupdatePossessionInPatrimoine(nom_patrimoine, possessionsReq);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Page<PossessionAvecType> responseBody = response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.getSize());
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
