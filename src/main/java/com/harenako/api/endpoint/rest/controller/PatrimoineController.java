package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.PojaGenerated;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@PojaGenerated
@RestController
@AllArgsConstructor
public class PatrimoineController {

  @GetMapping("/patrimoines")
  public String getAllPatrimoines() {
    return "patrimoines api in implementation";
  }
}
