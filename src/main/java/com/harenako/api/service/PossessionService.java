package com.harenako.api.service;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import java.util.List;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@PojaGenerated
@NoArgsConstructor
public class PossessionService {
  public String getPossessions(String nom_patrimoine) {
    return "get all possessions: not implemented";
  }

  public String getPossessionByNom(String nom_patrimoine, String nom_possession) {
    return "get possession by nom: not implemented";
  }

  public String crupdPossessions(String nom_patrimoine, List<Patrimoine> patrimoines) {
    return "create possessions: not implemented";
  }

  public String removePossession(String nom_patrimoine, String nom_possession) {
    return "delete possessions: not implemented";
  }
}
