package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PatrimoineController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

@SpringBootTest
public class PatrimoineControllerTest {
  @InjectMocks
  private PatrimoineController controller;

  @MockBean
  private PatrimoineService service;

  //@Autowired private

  @BeforeEach
  public void setUp() {
    // Initialiser les mocks
    MockitoAnnotations.openMocks(this);
  }

  private static List<Patrimoine> patrimoines = List.of(
          new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of()),
          new Patrimoine("patrimoine-test", new Personne("possesseur-test"), LocalDate.now(), Set.of())
  );


  @Test
  public void testGetPatrimoines() {
    when(service.getPatrimoines()).thenReturn(patrimoines);

    ResponseEntity<List<com.harenako.api.endpoint.rest.model.Patrimoine>> response = ResponseEntity.ok(

    );

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCode().value(), "Le code de statut de la réponse doit être 200.");

    List<Patrimoine> responseBody = response.getBody();
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
    List<Patrimoine> patrimoineModels = patrimoines;

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
