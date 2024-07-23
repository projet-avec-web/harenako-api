package com.harenako.api.service;

import static java.io.File.createTempFile;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.file.BucketComponent;
import java.io.File;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.serialisation.Serialiseur;

@Service
@PojaGenerated
@AllArgsConstructor
public class PatrimoineService {
  @SuppressWarnings({"rawtypes", "unchecked"})
  private final Serialiseur<Patrimoine> serialiseur = new Serialiseur();

  private BucketComponent bucketComponent;
  private final String PATRIMOINE_KEY = "patriomines/";

  public String getPatrimoines() {
    File allPatrimoineFile = bucketComponent.download(PATRIMOINE_KEY);
    return allPatrimoineFile.getAbsolutePath();
  }

  public Patrimoine getPatrimoineByNom(String nom) {
    String patrimoineString = bucketComponent.download(PATRIMOINE_KEY + nom).getName();
    return serialiseur.deserialise(patrimoineString);
  }

  @SneakyThrows
  public List<Patrimoine> crupdPatrimoines(List<Patrimoine> patrimoines) {
    for (Patrimoine patrimoine : patrimoines) {
      String patrimoineString = serialiseur.serialise(patrimoine);
      File patrimoineFile = createTempFile(patrimoineString, "");
      bucketComponent.upload(patrimoineFile, PATRIMOINE_KEY + patrimoine.getNom());
    }
    return patrimoines;
  }
}
