package com.harenako.api.endpoint.rest.controller;

import com.harenako.api.endpoint.rest.model.Argent;
import com.harenako.api.endpoint.rest.model.FluxArgent;
import com.harenako.api.endpoint.rest.model.Materiel;
import com.harenako.api.endpoint.rest.model.PossessionAvecType.TypeEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PossessionData {
  private TypeEnum type;
  private Argent argent;
  private FluxArgent fluxArgent;
  private Materiel materiel;
}
