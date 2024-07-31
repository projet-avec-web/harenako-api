package com.harenako.api.endpoint.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.harenako.api.endpoint.rest.controller.PatrimoineController;
import com.harenako.api.endpoint.rest.model.Patrimoine;

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
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

public class PatrimoineControllerTest {

  @Mock private PatrimoineService service;
  
  @Mock private PatrimoineObjectMapper mapper;
  
  @InjectMocks private PatrimoineController controller;

  @BeforeEach
  public void setUp() {
    // Initialiser les mocks
    MockitoAnnotations.openMocks(this);
  }

  private static List<school.hei.patrimoine.modele.Patrimoine> patrimoinesSchool = List.of(
    new school.hei.patrimoine.modele.Patrimoine(null, null, null, Set.of(
      new school.hei.patrimoine.modele.possession.Argent("argent", LocalDate.now(), 0))),
    new school.hei.patrimoine.modele.Patrimoine(null, null, null, Set.of(
      new school.hei.patrimoine.modele.possession.Argent("argent", LocalDate.now(), 0)))
  );

  @Test
  public void testGetPatrimoines() {
    when(service.getPatrimoines()).thenReturn(patrimoinesSchool);

    ResponseEntity<Page<Patrimoine>> response = controller.getPatrimoine(0, 10);

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    Page<Patrimoine> responseBody = response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(2, responseBody.getContent().size(), "La taille du corps de la réponse doit être 2.");
  }

  @Test
  public void testGetPatrimoineByNom() {
    String nomPatrimoine = "patrimoine-test";
    school.hei.patrimoine.modele.Patrimoine patrimoine = new school.hei.patrimoine.modele.Patrimoine(nomPatrimoine, new Personne("possesseur-test"), LocalDate.now(), Set.of());

    when(service.getPatrimoineByNom(nomPatrimoine)).thenReturn(patrimoine);

    ResponseEntity<Page<Patrimoine>> response = controller.getPatrimoineByNom(nomPatrimoine);

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    Page<Patrimoine> responseBody = response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(nomPatrimoine, responseBody.get().findFirst().get(), "Le nom du patrimoine dans la réponse doit correspondre à celui attendu.");
  }

  @Test
  public void testCrupdatePatrimoines() {
    List<Patrimoine> patrimoines = new ArrayList<>();
    patrimoines.add(new Patrimoine());
    patrimoines.add(new Patrimoine());

    when(mapper.toModel(patrimoines.get(0))).thenReturn(patrimoinesSchool.get(0));
    when(mapper.toModel(patrimoines.get(1))).thenReturn(patrimoinesSchool.get(1));
    when(service.crupdPatrimoines(patrimoinesSchool)).thenReturn(patrimoinesSchool);

    ResponseEntity<Page<Patrimoine>> response = controller.crupdatePatrimoine(patrimoines);

    assertNotNull(response, "La réponse ne doit pas être nulle.");
    assertEquals(200, response.getStatusCodeValue(), "Le code de statut de la réponse doit être 200.");

    Page<Patrimoine> responseBody = response.getBody();
    assertNotNull(responseBody, "Le corps de la réponse ne doit pas être nul.");
    assertEquals(2, responseBody.getSize(), "La taille du corps de la réponse doit être 2.");
  }
}
