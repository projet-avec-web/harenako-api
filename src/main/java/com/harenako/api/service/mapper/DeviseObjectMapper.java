package com.harenako.api.service.mapper;

import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;
import school.hei.patrimoine.modele.Devise;

import java.util.Objects;

@Component
@NoArgsConstructor
public class DeviseObjectMapper implements ObjectMapper<Devise, com.harenako.api.endpoint.rest.model.Devise> {
    @Override
    public Devise toModel(com.harenako.api.endpoint.rest.model.Devise devise) {
        if (devise == null) return null;
        if (Objects.requireNonNull(devise.getNom()).equalsIgnoreCase(Devise.MGA.nom())) {
            return Devise.MGA;
        } else if (devise.getNom().equalsIgnoreCase(Devise.EUR.nom())) {
            return Devise.EUR;
        } else if (devise.getNom().equalsIgnoreCase(Devise.CAD.nom())) {
            return Devise.CAD;
        } else {
            return Devise.NON_NOMMEE;
        }

    }

    @Override
    public com.harenako.api.endpoint.rest.model.Devise toRestModel(Devise devise) {
        return null;
    }
}
