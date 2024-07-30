package com.harenako.api.service.mapper;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Argent;
import school.hei.patrimoine.modele.possession.FluxArgent;
import school.hei.patrimoine.modele.possession.Materiel;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.*;

@Component
@NoArgsConstructor
@AllArgsConstructor
public class PatrimoineObjectMapper implements ObjectMapper<Patrimoine, com.harenako.api.endpoint.rest.model.Patrimoine> {
    private PersonneObjectMapper personneObjectMapper;
    private PossessionsObjectMapper possessionsObjectMapper;

    @Override
    public Patrimoine toModel(com.harenako.api.endpoint.rest.model.Patrimoine patrimoine) {
        if (patrimoine == null) return null;

        return new Patrimoine(
                patrimoine.getNom(),
                personneObjectMapper.toModel(patrimoine.getPossesseur()),
                patrimoine.getT(),
                possessionsObjectMapper.toModel(Objects.requireNonNull(patrimoine.getPossessions()))
        );
    }

    @Override
    public com.harenako.api.endpoint.rest.model.Patrimoine toRestModel(Patrimoine patrimoine) {
        if (patrimoine == null) return null;
        return new com.harenako.api.endpoint.rest.model.Patrimoine()
                .nom(patrimoine.nom())
                .t(patrimoine.t())
                .possesseur(personneObjectMapper.toRestModel(patrimoine.possesseur()))
                .possessions(possessionsObjectMapper.toRestModel(patrimoine.possessions()));
    }
}
