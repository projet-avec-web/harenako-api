package com.harenako.api.service.mapper;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.Patrimoine;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
@AllArgsConstructor
public class PatrimoineObjectMapper implements ObjectMapper<Patrimoine, com.harenako.api.endpoint.rest.model.Patrimoine> {
    private PersonneObjectMapper personneObjectMapper;
    private PossessionObjectMapper possessionObjectMapper;

    @Override
    public Patrimoine toModel(com.harenako.api.endpoint.rest.model.Patrimoine patrimoine) {
        if (patrimoine == null) return null;
        Set<Possession> possessions = new HashSet<>();
        for (PossessionAvecType possession: Objects.requireNonNull(patrimoine.getPossessions())) {
            possessions.add(possessionObjectMapper.toModel(possession));
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
