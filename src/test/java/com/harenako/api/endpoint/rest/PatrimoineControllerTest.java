package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PatrimoineController;
import school.hei.patrimoine.modele.Patrimoine;
import com.harenako.api.service.PatrimoineService;
import com.harenako.api.service.mapper.*;

import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

public class PatrimoineControllerTest {

  @Mock private PatrimoineService service;

  @MockBean private PatrimoineObjectMapper mapper;

  @InjectMocks private PatrimoineController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPatrimoines() {
    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));

    when(service.getPatrimoines()).thenReturn(patrimoines);

    ResponseEntity<?> response = controller.getPatrimoine(0, 10);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<?> responseBody = (List<?>) response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
  }

  @Test
  public void testGetPatrimoineByNom() {
    String nomPatrimoine = "patrimoine-test";
    Patrimoine patrimoine = new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of());

    when(service.getPatrimoineByNom(nomPatrimoine)).thenReturn(patrimoine);

    ResponseEntity<?> response = controller.getPatrimoineByNom(nomPatrimoine);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Patrimoine responseBody = (Patrimoine) response.getBody();
    assertNotNull(responseBody);
    assertEquals(nomPatrimoine, responseBody.nom());
  }

  @Test
  @Test
  public void testCrupdatePatrimoine() {
    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));

    when(service.crupdPatrimoines(patrimoines)).thenReturn(patrimoines);

    List<com.harenako.api.endpoint.rest.model.Patrimoine> patrimoinesReq = new ArrayList<>();
    patrimoinesReq.add(new com.harenako.api.endpoint.rest.model.Patrimoine());
    patrimoinesReq.add(new com.harenako.api.endpoint.rest.model.Patrimoine());
    ResponseEntity<?> response = controller.crupdatePatrimoine(patrimoinesReq);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<?> responseBody = (List<?>) response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
  }
}
