package com.harenako.api.service.mapper;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.FluxArgent;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class FluxArgentObjectMapper
    implements ObjectMapper<FluxArgent, com.harenako.api.endpoint.rest.model.FluxArgent> {
  private ArgentObjectMapper argentObjectMapper;
  private DeviseObjectMapper deviseObjectMapper;

  @Override
  public FluxArgent toModel(com.harenako.api.endpoint.rest.model.FluxArgent fluxArgent) {
    if (fluxArgent == null) return null;
    return new FluxArgent(
        fluxArgent.getNom(),
        argentObjectMapper.toModel(fluxArgent.getArgent()),
        fluxArgent.getDebut(),
        fluxArgent.getFin(),
        fluxArgent.getFluxMensuel(),
        fluxArgent.getDateDOperation(),
        deviseObjectMapper.toModel(fluxArgent.getDevise()));
  }

  @Override
  public com.harenako.api.endpoint.rest.model.FluxArgent toRestModel(FluxArgent fluxArgent) {
    if (fluxArgent == null) return null;
    return new com.harenako.api.endpoint.rest.model.FluxArgent()
        .nom(fluxArgent.getNom())
        .argent(argentObjectMapper.toRestModel(fluxArgent.getArgent()))
        .debut(fluxArgent.getDebut())
        .fin(fluxArgent.getFin())
        .fluxMensuel(fluxArgent.getFluxMensuel())
        .dateDOperation(fluxArgent.getDateOperation())
        .devise(deviseObjectMapper.toRestModel(fluxArgent.getDevise()));
  }
}
