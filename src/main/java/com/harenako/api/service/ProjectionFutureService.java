package com.harenako.api.service;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.*;
import com.harenako.api.service.mapper.PatrimoineObjectMapper;
import com.harenako.api.service.mapper.PersonneObjectMapper;
import com.harenako.api.service.mapper.PossessionObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.modele.EvolutionPatrimoine;
import school.hei.patrimoine.visualisation.xchart.GrapheurEvolutionPatrimoine;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

@Service
@PojaGenerated
@AllArgsConstructor
public class ProjectionFutureService {
  private PatrimoineObjectMapper patrimoineObjectMapper;
  private PatrimoineService patrimoineService;
  private PossessionObjectMapper possessionObjectMapper;

  public FluxImpossibles getPatrimoineFluxImpossibles(String nom_patrimoine, String debut, String fin) {
    Patrimoine patrimoine = patrimoineService.getPatrimoineByNom(nom_patrimoine);
    List<FluxArgent> fluxArgents = new ArrayList<>();

    for (PossessionAvecType possession : Objects.requireNonNull(patrimoine.getPossessions())) {
      fluxArgents.add((FluxArgent) possessionObjectMapper.toModel(possession));
    }

    return new FluxImpossibles()
            .nomArgent(nom_patrimoine)
            .date(LocalDate.parse(debut))
            .valeurArgent(patrimoine.getValeurComptable())
            .fluxArgents(fluxArgents);
  }

  public File getPatrimoineGraph(String nom_patrimoine, String debut, String fin) {
    Patrimoine patrimoine = patrimoineService.getPatrimoineByNom(nom_patrimoine);
    EvolutionPatrimoine evolutionPatrimoine = new EvolutionPatrimoine(nom_patrimoine, patrimoineObjectMapper.toModel(patrimoine), LocalDate.parse(debut), LocalDate.parse(fin));

    GrapheurEvolutionPatrimoine grapheurEvolutionPatrimoine = new GrapheurEvolutionPatrimoine();
    return grapheurEvolutionPatrimoine.apply(evolutionPatrimoine);
  }
}
