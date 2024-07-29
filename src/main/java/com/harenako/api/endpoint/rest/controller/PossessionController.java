package com.harenako.api.endpoint.rest.controller;

import jakarta.websocket.server.PathParam;
// import com.harenako.api.endpoint.rest.model.Possession;
import com.harenako.api.service.PossessionService;
import com.harenako.api.service.mapper.ArgentObjectMapper;
import com.harenako.api.service.mapper.FluxArgentObjectMapper;
import com.harenako.api.service.mapper.MaterielObjectMapper;
import com.harenako.api.endpoint.rest.model.PossessionAvecType.TypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import school.hei.patrimoine.modele.possession.Possession;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.harenako.api.endpoint.rest.controller.Pagination.getPage;

@RestController
@AllArgsConstructor
public class PossessionController {
  private PossessionService service;
  private ArgentObjectMapper argentMapper = new ArgentObjectMapper();
  private FluxArgentObjectMapper fluxArgentMapper = new FluxArgentObjectMapper();
  private MaterielObjectMapper materielMapper = new MaterielObjectMapper();

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<?> getPossessionByPatrimoine(
      @PathParam("page") Integer page,
      @PathParam("page_size") Integer pageSize,
      @PathVariable("nom_patrimoine") String nom_patrimoine) {
    return ResponseEntity.ok().body(
            getPage(service.getPossessions(nom_patrimoine), page, pageSize)
    );
  }

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<?> getPossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    return ResponseEntity.of(
            Optional.of(service.getPossessionByNom(nom_patrimoine, nom_possession))
    );
  }

  @PutMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<?> crupdatePossessionInPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @RequestBody List<PossessionData> possessionDatas) {
    List<Possession> possessions = new ArrayList<>();
    for (PossessionData data : possessionDatas) {
      switch (data.getType()) {
        case TypeEnum.ARGENT:
          possessions.add(argentMapper.toModel(data.getArgent()));
          break;
        case TypeEnum.FLUXARGENT:
          possessions.add(fluxArgentMapper.toModel(data.getFluxArgent()));
          break;
        case TypeEnum.MATERIEL:
          possessions.add(materielMapper.toModel(data.getMateriel()));
          break;
        default:
          continue;
      }
    }
    return ResponseEntity.ok().body(service.crupdPossessions(nom_patrimoine, possessions));
  }

  @DeleteMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<?> deletePossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    return ResponseEntity.ok().body(service.deletePossession(nom_patrimoine, nom_possession));
  }
}
