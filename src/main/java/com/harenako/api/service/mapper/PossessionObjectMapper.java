package com.harenako.api.service.mapper;

import com.harenako.api.endpoint.rest.model.PossessionAvecType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.patrimoine.modele.possession.Possession;

import java.util.Objects;

@Component
@AllArgsConstructor
public class PossessionObjectMapper implements ObjectMapper<Object, com.harenako.api.endpoint.rest.model.PossessionAvecType> {
    private ArgentObjectMapper argentObjectMapper;
    private FluxArgentObjectMapper fluxArgentObjectMapper;
    private MaterielObjectMapper materielObjectMapper;

    @Override
    public Object toModel(com.harenako.api.endpoint.rest.model.PossessionAvecType possession) {
        if (possession == null) return null;
        if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.ARGENT)) {
            return argentObjectMapper.toModel(possession.getArgent());
        } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.FLUXARGENT)) {
            return fluxArgentObjectMapper.toModel(possession.getFluxArgent());
        } else if (Objects.equals(possession.getType(), PossessionAvecType.TypeEnum.MATERIEL)) {
            return materielObjectMapper.toModel(possession.getMateriel());
        }
        return null;
    }

    @Override
    public com.harenako.api.endpoint.rest.model.PossessionAvecType toRestModel(Object possession) {
        return null;
    }

}
