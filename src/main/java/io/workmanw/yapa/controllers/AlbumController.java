package io.workmanw.yapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityManager;

import io.workmanw.yapa.models.AlbumModel;

@Controller
@RequestMapping("/album")
public class AlbumController {
  @RequestMapping
  public String myDefault() {
    return "";
  }

  @RequestMapping("/create")
  public void createAlbum(@RequestParam String name) {
    AlbumModel album = new AlbumModel();
    album.setName(name);

    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.insert(album);
  }

  // @RequestMapping("/{id}")
  // public String getAlbum(@PathVariable long id) {
  //   return String.format("Album: %d", id);
  // }
}
