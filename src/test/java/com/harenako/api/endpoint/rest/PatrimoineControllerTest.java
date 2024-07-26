package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PatrimoineController;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.service.PatrimoineService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class PatrimoineControllerTest {

  @Mock private PatrimoineService service;

  @InjectMocks private PatrimoineController controller;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPatrimoines() {
    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine());
    patrimoines.add(new Patrimoine());

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
    Patrimoine patrimoine = new Patrimoine();
    patrimoine.setNom(nomPatrimoine);

    when(service.getPatrimoineByNom(nomPatrimoine)).thenReturn(patrimoine);

    ResponseEntity<?> response = controller.getPatrimoineByNom(nomPatrimoine);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    Patrimoine responseBody = (Patrimoine) response.getBody();
    assertNotNull(responseBody);
    assertEquals(nomPatrimoine, responseBody.getNom());
  }

  @Test
  public void testCrupdatePatrimoine() {
    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine());
    patrimoines.add(new Patrimoine());

    when(service.crupdPatrimoines(patrimoines)).thenReturn(patrimoines);

    ResponseEntity<?> response = controller.crupdatePatrimoine(patrimoines);
    assertNotNull(response);
    assertEquals(200, response.getStatusCodeValue());
    List<?> responseBody = (List<?>) response.getBody();
    assertNotNull(responseBody);
    assertEquals(2, responseBody.size());
  }
}
