package com.harenako.api.service;

import com.harenako.api.PojaGenerated;
import java.io.File;
import java.time.LocalDate;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.modele.FluxImpossibles;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Possession;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

@Service
@PojaGenerated
@AllArgsConstructor
public class ProjectionFutureService {
  private PatrimoineService patrimoineService;
  private PossessionService possessionService;

  public FluxImpossibles getPatrimoineFluxImpossibles(
      String nom_patrimoine, String debut, String fin) {
    Patrimoine patrimoine = patrimoineService.getPatrimoineByNom(nom_patrimoine);
    Set<FluxArgent> fluxArgents = new LinkedHashSet<>();

    for (Possession possession :
        Objects.requireNonNull(possessionService.getPossessions(nom_patrimoine))) {
      if (possession instanceof Argent) {
        for (FluxArgent fluxArgent : ((Argent) possession).getFluxArgents())
          fluxArgents.add(fluxArgent);
      }
    }

    return new FluxImpossibles(
        LocalDate.parse(debut), nom_patrimoine, patrimoine.getValeurComptable(), fluxArgents);
  }

  public File getPatrimoineGraph(String nom_patrimoine, String debut, String fin) {
    Patrimoine patrimoine = patrimoineService.getPatrimoineByNom(nom_patrimoine);
    EvolutionPatrimoine evolutionPatrimoine =
        new EvolutionPatrimoine(
            nom_patrimoine, patrimoine, LocalDate.parse(debut), LocalDate.parse(fin));

    GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
    return grapheurEvolutionPatrimoine.apply(evolutionPatrimoine);
  }
}
