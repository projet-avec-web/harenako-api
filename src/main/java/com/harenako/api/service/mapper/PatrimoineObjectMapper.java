package com.harenako.api.service.mapper;

import com.harenako.api.PojaGenerated;
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
@PojaGenerated
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
            }
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
        if (patrimoine == null) return null;
        List<PossessionAvecType> possessions = new ArrayList<>();

        patrimoine.possessions().forEach(possession -> {
            if (possession instanceof Argent) {
                possessions.add(new PossessionAvecType().type(PossessionAvecType.TypeEnum.ARGENT).argent(
                        argentObjectMapper.toRestModel((Argent) possession)
                ));
            } else if (possession instanceof FluxArgent) {
                possessions.add(new PossessionAvecType().type(PossessionAvecType.TypeEnum.FLUXARGENT).fluxArgent(
                        fluxArgentObjectMapper.toRestModel((FluxArgent) possession)
                ));
            } else if (possession instanceof Materiel) {
                possessions.add(new PossessionAvecType().type(PossessionAvecType.TypeEnum.MATERIEL).materiel(
                        materielObjectMapper.toRestModel((Materiel) possession)
                ));
            }
        });

        return new com.harenako.api.endpoint.rest.model.Patrimoine()
                .nom(patrimoine.nom())
                .t(patrimoine.t())
                .possesseur(personneObjectMapper.toRestModel(patrimoine.possesseur()))
                .possessions(possessions);
    }
}
