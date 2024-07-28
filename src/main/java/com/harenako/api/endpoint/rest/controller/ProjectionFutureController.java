package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.PojaGenerated;
import com.harenako.api.service.ProjectionFutureService;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@PojaGenerated
@RestController
@AllArgsConstructor
public class ProjectionFutureController {
  private ProjectionFutureService service;

  @GetMapping("/patrimoines/{nom_patrimoine}/flux-impossibles")
  public ResponseEntity<?> getPatrimoineFluxImpossibles(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathParam("debut") String debut,
      @PathParam("fin") String fin) {
    return ResponseEntity.of(
            Optional.of(service.getPatrimoineFluxImpossibles(nom_patrimoine, debut, fin))
    );
  }

  @GetMapping("/patrimoines/{nom_patrimoine}/graphe")
  public ResponseEntity<?> getPatrimoineGraph(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathParam("debut") String debut,
      @PathParam("fin") String fin) {
    return ResponseEntity.of(
            Optional.of(service.getPatrimoineGraph(nom_patrimoine, debut, fin))
    );
  }
}
