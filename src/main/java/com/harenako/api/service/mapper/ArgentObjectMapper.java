package com.harenako.api.service.mapper;

import com.harenako.api.PojaGenerated;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.Argent;

@Component
@PojaGenerated
@NoArgsConstructor
@AllArgsConstructor
public class ArgentObjectMapper implements ObjectMapper<Argent, com.harenako.api.endpoint.rest.model.Argent> {
    private DeviseObjectMapper deviseObjectMapper;

    @Override
    public Argent toModel(com.harenako.api.endpoint.rest.model.Argent argent) {
        if (argent == null) return null;
        return new Argent(
                argent.getNom(),
                argent.getDateDOuverture(),
                argent.getValeurComptable(),
                deviseObjectMapper.toModel(argent.getDevise())
        );
    }

    @Override
    public com.harenako.api.endpoint.rest.model.Argent toRestModel(Argent argent) {
        if (argent == null) return null;
        return new com.harenako.api.endpoint.rest.model.Argent().nom(argent.getNom()).devise(
                deviseObjectMapper.toRestModel(argent.getDevise())
        ).dateDOuverture(argent.getDateOuverture()).valeurComptable(argent.getValeurComptable());
    }
}
