package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PatrimoineController;
import school.hei.patrimoine.modele.Patrimoine;
import com.harenako.api.service.PatrimoineService;
import com.harenako.api.service.mapper.*;

import school.hei.patrimoine.modele.Personne;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
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

  @InjectMocks
  private PatrimoineController controller;

  @Mock
  private PatrimoineService service;

  @Mock
  private PatrimoineObjectMapper mapper;

  @BeforeEach
  public void setUp() {
    // Initialiser les mocks
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testGetPatrimoines() {

    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));
    patrimoines.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()));


    when(service.getPatrimoines()).thenReturn(patrimoines);


    ResponseEntity<?> response = controller.getPatrimoine(0, 10);


    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    @SuppressWarnings("unchecked")
    List<Patrimoine> responseBody = (List<Patrimoine>) response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(2, responseBody.size(), "La taille du corps de la réponse doit être 2.");
  }


  @Test
  public void testGetPatrimoineByNom() {
    String nomPatrimoine = "patrimoine-test";
    Patrimoine patrimoine = new Patrimoine(nomPatrimoine, new Personne("possesseur-test"), LocalDate.now(), Set.of());

    when(service.getPatrimoineByNom(nomPatrimoine)).thenReturn(patrimoine);

    ResponseEntity<?> response = controller.getPatrimoineByNom(nomPatrimoine);

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    Patrimoine responseBody = (Patrimoine) response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(nomPatrimoine, responseBody.nom(), "Le nom du patrimoine dans la réponse doit correspondre à celui attendu.");
  }

  @Test
  public void testCrupdatePatrimoine() {

    List<Patrimoine> patrimoineModels = new ArrayList<>();
    patrimoineModels.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), new HashSet<>()));
    patrimoineModels.add(new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), new HashSet<>()));

    List<com.harenako.api.endpoint.rest.model.Patrimoine> patrimoineRequests = new ArrayList<>();
    patrimoineRequests.add(new com.harenako.api.endpoint.rest.model.Patrimoine());
    patrimoineRequests.add(new com.harenako.api.endpoint.rest.model.Patrimoine());

    when(mapper.toModel(patrimoineRequests.get(0))).thenReturn(patrimoineModels.get(0));
    when(mapper.toModel(patrimoineRequests.get(1))).thenReturn(patrimoineModels.get(1));
    when(service.crupdPatrimoines(patrimoineModels)).thenReturn(patrimoineModels);

    ResponseEntity<?> response = controller.crupdatePatrimoine(patrimoineRequests);

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    @SuppressWarnings("unchecked")
    List<com.harenako.api.endpoint.rest.model.Patrimoine> responseBody = (List<com.harenako.api.endpoint.rest.model.Patrimoine>) response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(2, responseBody.size(), "La taille du corps de la réponse doit être 2.");
  }
}
