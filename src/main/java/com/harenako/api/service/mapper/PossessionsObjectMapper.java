package com.harenako.api.service.mapper;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import java.util.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

@Component
@AllArgsConstructor
public class PossessionsObjectMapper
    implements ObjectMapper<
        Set<Possession>, List<com.harenako.api.endpoint.rest.model.PossessionAvecType>> {
  private ArgentObjectMapper argentObjectMapper;
  private FluxArgentObjectMapper fluxArgentObjectMapper;
  private MaterielObjectMapper materielObjectMapper;

  @Override
  public Set<Possession> toModel(List<PossessionAvecType> possessions) {
    Set<Possession> possessionsSet = new HashSet<>();

    possessions.forEach(
        possession -> {
          if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.ARGENT)) {
            possessionsSet.add(argentObjectMapper.toModel(possession.getArgent()));
          } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.FLUXARGENT)) {
            possessionsSet.add(fluxArgentObjectMapper.toModel(possession.getFluxArgent()));
          } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.MATERIEL)) {
            possessionsSet.add(materielObjectMapper.toModel(possession.getMateriel()));
          }
        });
    return possessionsSet;
  }

  @Override
  public List<com.harenako.api.endpoint.rest.model.PossessionAvecType> toRestModel(
      Set<Possession> possessions) {
    List<PossessionAvecType> possessionAvecTypeList = new ArrayList<>();

    possessions.forEach(
        possession -> {
          if (possession instanceof Argent) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.ARGENT)
                    .argent(argentObjectMapper.toRestModel((Argent) possession)));
          } else if (possession instanceof FluxArgent) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.FLUXARGENT)
                    .fluxArgent(fluxArgentObjectMapper.toRestModel((FluxArgent) possession)));
          } else if (possession instanceof Materiel) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.MATERIEL)
                    .materiel(materielObjectMapper.toRestModel((Materiel) possession)));
          }
        });
    return possessionAvecTypeList;
  }

  public List<com.harenako.api.endpoint.rest.model.PossessionAvecType> toRestModel(
      List<Possession> possessions) {
    List<PossessionAvecType> possessionAvecTypeList = new ArrayList<>();

    possessions.forEach(
        possession -> {
          if (possession instanceof Argent) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.ARGENT)
                    .argent(argentObjectMapper.toRestModel((Argent) possession)));
          } else if (possession instanceof FluxArgent) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.FLUXARGENT)
                    .fluxArgent(fluxArgentObjectMapper.toRestModel((FluxArgent) possession)));
          } else if (possession instanceof Materiel) {
            possessionAvecTypeList.add(
                new PossessionAvecType()
                    .type(PossessionAvecType.TypeEnum.MATERIEL)
                    .materiel(materielObjectMapper.toRestModel((Materiel) possession)));
          }
        });
    return possessionAvecTypeList;
  }
}
