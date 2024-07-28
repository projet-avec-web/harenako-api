package com.harenako.api.service.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.Materiel;

@Component
@AllArgsConstructor
public class MaterielObjectMapper implements ObjectMapper<Materiel, com.harenako.api.endpoint.rest.model.Materiel> {
    private DeviseObjectMapper deviseObjectMapper;

    @Override
    public Materiel toModel(com.harenako.api.endpoint.rest.model.Materiel materiel) {
        if (materiel == null) return null;
        return new Materiel(
                materiel.getNom(),
                materiel.getT(),
                materiel.getValeurComptable(),
                materiel.getDateDAcquisition(),
                materiel.getTauxDappreciationAnnuel(),
                deviseObjectMapper.toModel(materiel.getDevise())
        );
    }

    @Override
    public com.harenako.api.endpoint.rest.model.Materiel toRestModel(Materiel materiel) {
        return null;
    }
}
