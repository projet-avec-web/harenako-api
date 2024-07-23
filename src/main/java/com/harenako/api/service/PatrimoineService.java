package com.harenako.api.service;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@PojaGenerated
@AllArgsConstructor
public class PatrimoineService {

  public String getPatrimoines() {
    return "get all patrimoine: not implemented";
  }

  public String getPatrimoineByNom(String nom) {
    return "get patrimoine by nom: not implemented";
  }

  public String crupdPatrimoines(List<Patrimoine> patrimoines) {
    return "create patrimoines: not implmented";
  }
}
