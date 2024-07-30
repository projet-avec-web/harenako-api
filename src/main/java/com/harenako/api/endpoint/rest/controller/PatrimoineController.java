package com.harenako.api.endpoint.rest.controller;


import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.service.PatrimoineService;
import com.harenako.api.service.mapper.PatrimoineObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.harenako.api.endpoint.rest.controller.Pagination.convertToPage;

@RestController
@AllArgsConstructor
public class PatrimoineController {
  private PatrimoineService service;
  private PatrimoineObjectMapper patrimoineObjectMapper;

  @GetMapping("/patrimoines")
  public ResponseEntity<Page<Patrimoine>> getPatrimoine(
      @RequestParam(name = "page", defaultValue = "0") Integer page,
      @RequestParam(name = "page_size", defaultValue = "10") Integer pageSize
  ) {
    List<Patrimoine> patrimoines = service.getPatrimoines().stream().map(patrimoine -> patrimoineObjectMapper.toRestModel(patrimoine)).toList();
    Page<Patrimoine> dataResponse = convertToPage(patrimoines, page, pageSize);

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(dataResponse.getTotalPages()));
    headers.add("X-Page-Size", String.valueOf(dataResponse.getTotalElements()));
    return ResponseEntity.ok()
            .headers(headers)
            .body(dataResponse);
  }

  @GetMapping("/patrimoines/{nom_patrimoine}")
  public ResponseEntity<Patrimoine> getPatrimoineByNom(
      @PathVariable("nom_patrimoine") String nom_patrimoine
  ) {
    return ResponseEntity.of(
            Optional.of(patrimoineObjectMapper.toRestModel(service.getPatrimoineByNom(nom_patrimoine)))
    );
  }

  @PutMapping("/patrimoines")
  public ResponseEntity<Page<Patrimoine>> crupdatePatrimoine(@RequestBody List<Patrimoine> patrimoines) {
    Page<Patrimoine> dataResponse = convertToPage(
            service.crupdPatrimoines(
                    patrimoines.stream().map(patrimoine -> patrimoineObjectMapper.toModel(patrimoine)).toList()
            ).stream().map(patrimoine -> patrimoineObjectMapper.toRestModel(patrimoine)).toList(),
            0, patrimoines.size());

    HttpHeaders headers = new HttpHeaders();
    headers.add("X-Page-Number", String.valueOf(dataResponse.getTotalPages()));
    headers.add("X-Page-Size", String.valueOf(dataResponse.getTotalElements()));

    return ResponseEntity.ok()
            .headers(headers)
            .body(dataResponse);
  }
}
