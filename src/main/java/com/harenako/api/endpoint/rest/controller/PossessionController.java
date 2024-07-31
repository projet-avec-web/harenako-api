package com.harenako.api.endpoint.rest.controller;

import static com.harenako.api.endpoint.rest.controller.Pagination.convertToPage;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import com.harenako.api.service.PossessionService;
import com.harenako.api.service.mapper.PossessionsObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class PossessionController {
  private PossessionService service;
  private PossessionsObjectMapper possessionsObjectMapper;

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<Page<PossessionAvecType>> getPossessionByPatrimoine(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize,
      @PathVariable("nom_patrimoine") String nom_patrimoine) {
    List<PossessionAvecType> patrimoines =
        possessionsObjectMapper.toRestModel(service.getPossessions(nom_patrimoine));
    Page<PossessionAvecType> dataResponse = convertToPage(patrimoines, page, pageSize);

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(dataResponse.getTotalPages()));
    headers.add("X-Page-Size", String.valueOf(dataResponse.getTotalElements()));

    return ResponseEntity.ok().headers(headers).body(dataResponse);
  }

  @GetMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<Page<PossessionAvecType>> getPossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    
    List<PossessionAvecType> possession = possessionsObjectMapper
        .toRestModel(List.of(service.getPossessionByNom(nom_patrimoine, nom_possession)));
        
    Page<PossessionAvecType> dataResponse = convertToPage(possession, 0, 1);
    
    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(dataResponse.getTotalPages()));
    headers.add("X-Page-Size", String.valueOf(dataResponse.getTotalElements()));
    
    if (possession == null)
      return ResponseEntity.ok().headers(headers).body(dataResponse);
    return ResponseEntity.status(404).build();
  }

  @PutMapping("/patrimoines/{nom_patrimoine}/possessions")
  public ResponseEntity<Page<PossessionAvecType>> crupdatePossessionInPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @RequestBody List<PossessionAvecType> possessions) {
    Page<PossessionAvecType> dataResponse =
        convertToPage(
            possessionsObjectMapper.toRestModel(
                service.crupdPossessions(
                    nom_patrimoine,
                    possessionsObjectMapper.toModel(possessions).stream().toList())),
            0,
            possessions.size());

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(dataResponse.getTotalPages()));
    headers.add("X-Page-Size", String.valueOf(dataResponse.getTotalElements()));

    return ResponseEntity.ok().headers(headers).body(dataResponse);
  }

  @DeleteMapping("/patrimoines/{nom_patrimoine}/possessions/{nom_possession}")
  public ResponseEntity<String> deletePossessionByNomByPatrimoine(
      @PathVariable("nom_patrimoine") String nom_patrimoine,
      @PathVariable("nom_possession") String nom_possession) {
    return ResponseEntity.ok().body(service.deletePossession(nom_patrimoine, nom_possession));
  }
}
