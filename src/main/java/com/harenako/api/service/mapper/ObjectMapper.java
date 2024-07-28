package com.harenako.api.service.mapper;

import org.springframework.stereotype.Component;

//@Component
public interface ObjectMapper<Model, RestModel> {
    Model toModel(RestModel restModel);
    RestModel toRestModel(Model model);
}
