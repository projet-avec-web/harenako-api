package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.service.PatrimoineService;
import com.harenako.api.service.mapper.PatrimoineObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.harenako.api.endpoint.rest.controller.Pagination.getPage;

@RestController
@AllArgsConstructor
public class PatrimoineController {
  private PatrimoineService service;
  private PatrimoineObjectMapper mapper = new PatrimoineObjectMapper();

  @GetMapping("/patrimoines")
  public ResponseEntity<?> getPatrimoine(
      @RequestParam("page") Integer page, @RequestParam("page_size") Integer pageSize) {
    List<Patrimoine> restPatrimoinesResponses = new ArrayList<>();
    for (school.hei.patrimoine.modele.Patrimoine patrimoine : service.getPatrimoines()) {
      restPatrimoinesResponses.add(mapper.toRestModel(patrimoine));
      getPage(restPatrimoinesResponses, page, pageSize);
    }
    return ResponseEntity.ok().body(restPatrimoinesResponses);
  }

  @GetMapping("/patrimoines/{nom_patrimoine}")
  public ResponseEntity<?> getPatrimoineByNom(
      @PathVariable("nom_patrimoine") String nom_patrimoine) {
    return ResponseEntity.of(Optional.of(mapper.toRestModel(service.getPatrimoineByNom(nom_patrimoine))));
  }

  @PutMapping("/patrimoines")
  public ResponseEntity<?> crupdatePatrimoine(@RequestBody List<Patrimoine> restPatrimoines) {
    List<school.hei.patrimoine.modele.Patrimoine> patrimoines = new ArrayList<>();
    for (Patrimoine restPatrimoine : restPatrimoines) {
      school.hei.patrimoine.modele.Patrimoine patrimoine = mapper.toModel(restPatrimoine);
      if (patrimoine != null) patrimoines.add(patrimoine);
    }
    return ResponseEntity.ok().body(service.crupdPatrimoines(patrimoines));
  }
}
