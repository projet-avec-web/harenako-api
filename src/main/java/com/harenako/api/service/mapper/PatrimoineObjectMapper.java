package com.harenako.api.service.mapper;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class PatrimoineObjectMapper implements ObjectMapper<Patrimoine, com.harenako.api.endpoint.rest.model.Patrimoine> {
    private PersonneObjectMapper personneObjectMapper;
    private ArgentObjectMapper argentObjectMapper;
    private FluxArgentObjectMapper fluxArgentObjectMapper;
    private MaterielObjectMapper materielObjectMapper;

    @Override
    public Patrimoine toModel(com.harenako.api.endpoint.rest.model.Patrimoine patrimoine) {
        if (patrimoine == null) return null;
        Set<Possession> possessions = new HashSet<>();

        for (PossessionAvecType possession: Objects.requireNonNull(patrimoine.getPossessions())) {
            if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.ARGENT)) {
                possessions.add(argentObjectMapper.toModel(possession.getArgent()));
            } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.FLUXARGENT)) {
                possessions.add(fluxArgentObjectMapper.toModel(possession.getFluxArgent()));
            } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.MATERIEL)) {
                possessions.add(materielObjectMapper.toModel(possession.getMateriel()));
            } else return null;
        }

        return new Patrimoine(
                patrimoine.getNom(),
                personneObjectMapper.toModel(patrimoine.getPossesseur()),
                patrimoine.getT(),
                possessions
        );
    }

    @Override
    public com.harenako.api.endpoint.rest.model.Patrimoine toRestModel(Patrimoine patrimoine) {
        return null;
    }
}
