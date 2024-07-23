package com.harenako.api.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.harenako.api.PojaGenerated;

@RestController
@PojaGenerated
@AllArgsConstructor
public class PatrimoineController {

  @GetMapping("/patrimoines")
  public String getAllPatrimoines() {
    return "patrimoines api in implementation";
  }
}
