package com.harenako.api.service;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class ProjectionFutureService {

  public String getPatrimoineFluxImpossibles(String nom_patrimoine, String debut, String fin) {
    return "get flux impossible: not implemented";
  }

  public String getPatrimoineGraph(String nom_patrimoine, String debut, String fin) {
    return "get patrimoine graph: not implemented";
  }
}
