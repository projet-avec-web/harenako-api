package com.harenako.api.service.mapper;

import com.harenako.api.PojaGenerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.Materiel;

@Component
@PojaGenerated
@NoArgsConstructor
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
        if (materiel == null) return null;
        return new com.harenako.api.endpoint.rest.model.Materiel()
                .nom(materiel.getNom())
                .t(materiel.getT())
                .valeurComptable(materiel.getValeurComptable());
    }
}
