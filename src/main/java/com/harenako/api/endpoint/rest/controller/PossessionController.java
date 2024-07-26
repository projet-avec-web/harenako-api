package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.endpoint.rest.model.Possession;
import com.harenako.api.service.PossessionService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class PossessionController {
  private PossessionService service;

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<?> getPossessionByPatrimoine(
      @PathParam("page") Integer page,
      @PathParam("pageSize") Integer pageSize,
      @PathVariable("nom_patrimoine") String nom_patrimoine) {
    return ResponseEntity.ok().body(service.getPossessions(nom_patrimoine));
  }

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<?> getPossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    return ResponseEntity.ok().body(service.getPossessionByNom(nom_patrimoine, nom_possession));
  }

  @PutMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<?> crupdatePossessionInPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @RequestBody List<Possession> possessions) {
    return ResponseEntity.ok().body(service.crupdPossessions(nom_patrimoine, possessions));
  }

  @DeleteMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<?> deletePossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    return ResponseEntity.ok().body(service.deletePossession(nom_patrimoine, nom_possession));
  }
}
