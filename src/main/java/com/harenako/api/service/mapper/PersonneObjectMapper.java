package com.harenako.api.service.mapper;

import com.harenako.api.PojaGenerated;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.Personne;

@Component
@PojaGenerated
public class PersonneObjectMapper implements ObjectMapper<Personne, com.harenako.api.endpoint.rest.model.Personne>{
    @Override
    public Personne toModel(com.harenako.api.endpoint.rest.model.Personne personne) {
        if (personne == null) return null;
        return new Personne(personne.getNom());
    }

    @Override
    public com.harenako.api.endpoint.rest.model.Personne toRestModel(Personne personne) {
        if (personne == null) return null;
        return new com.harenako.api.endpoint.rest.model.Personne().nom(personne.nom());
    }
}
