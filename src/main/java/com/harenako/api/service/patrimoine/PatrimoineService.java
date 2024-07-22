package com.harenako.api.service.patrimoine;

import static java.io.File.createTempFile;

import com.harenako.api.PojaGenerated;
import com.harenako.api.endpoint.rest.model.Patrimoine;
import com.harenako.api.file.BucketComponent;
import java.io.File;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import school.hei.patrimoine.serialisation.Serialiseur;

@Service
@PojaGenerated
@AllArgsConstructor
public class PatrimoineService {
  private final Serialiseur<Patrimoine> serialiseur = new Serialiseur();
  private BucketComponent bucketComponent;
  private final String PATRIMOINE_KEY = "patriomines/";

  public List<Patrimoine> getPatrimoines() {
    bucketComponent.download(PATRIMOINE_KEY);
    return null;
  }

  public Patrimoine getPatrimoineByNom(String nom) {
    ;
    String patrimoineString = bucketComponent.download(PATRIMOINE_KEY + nom).getName();
    return serialiseur.deserialise(patrimoineString);
  }

  @SneakyThrows
  public List<Patrimoine> crupdPatrimoines(List<Patrimoine> patrimoines) {
    for (Patrimoine patrimoine : patrimoines) {
      String patrimoineString = serialiseur.serialise(patrimoine);
      File patrimoineFile = createTempFile(patrimoineString, "");
      bucketComponent.upload(patrimoineFile, PATRIMOINE_KEY);
      System.out.println(
          bucketComponent.presign(PATRIMOINE_KEY + patrimoineString, Duration.ofMinutes(2)));
    }
    return patrimoines;
  }
}
