package com.harenako.api.service;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;

import java.util.Collections;
import java.util.List;

import com.harenako.api.file.BucketComponent;
import com.harenako.api.file.BucketConf;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.serialisation.Serialiseur;

@Service
@PojaGenerated
@AllArgsConstructor
public class PossessionService {
  private final Serialiseur serialiseur = new Serialiseur();
  private final BucketComponent bucketComponent;
  private final BucketConf bucketConf;
  private final String POSSESSION_KEY = "possessions/";
  private final PatrimoineService patrimoineService;
  public List<Possession> getPossessions(String nom_patrimoine) {
    List<Possession> possessions = patrimoineService
            .getPatrimoineByNom(nom_patrimoine)
            .getPossessions();
    return listToPagedList(possessions);
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

  private List<Possession> listToPagedList(
          List<Possession> possessions,
          Integer page,
          Integer pageSize) {
    var start = (page - 1) * pageSize;
    var end = Math.min(start + pageSize, possessions.size());
    if (start >= possessions.size() || start < 0) {
      return Collections.emptyList();
    }
    return possessions.subList(start, end);
  }
}
