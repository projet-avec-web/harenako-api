package com.harenako.api.service.mapper;

public interface ObjectMapper<Model, RestModel> {
  Model toModel(RestModel restModel);

  RestModel toRestModel(Model model);
}
