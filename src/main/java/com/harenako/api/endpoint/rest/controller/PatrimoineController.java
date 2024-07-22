package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.service.patrimoine.PatrimoineService;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@AllArgsConstructor
public class PatrimoineController {
  private PatrimoineService service;

  @GetMapping("/patrimoines")
  public ResponseEntity<?> getPatrimoine(
      @PathParam("page") Integer page, @PathParam("pageSize") Integer pageSize) {
    return ResponseEntity.ok("get patrimoines");
  }

  @GetMapping("/patrimoines/{nom_patrimoine}")
  public ResponseEntity<?> getPatrimoineByNom(
      @PathVariable("nom_patrimoine") String nom_patrimoine) {
    return ResponseEntity.ok("get one patrimoines");
  }

  @PutMapping("/patrimoines")
  public ResponseEntity<?> crupdatePatrimoine(@RequestBody List<Patrimoine> patrimoines) {
    List<Patrimoine> patrimoineResults = service.crupdPatrimoines(patrimoines);
    if (patrimoineResults != null) {
      return ResponseEntity.ok(patrimoineResults);
    }
    return ResponseEntity.status(500).body("Internal Server Error");
  }
}
