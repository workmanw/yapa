package io.workmanw.yapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import io.workmanw.yapa.models.AlbumModel;

import com.google.gson.JsonArray;

import java.util.List;

@RestController
@RequestMapping("/album")
public class AlbumController {
  @RequestMapping(method = RequestMethod.GET)
  public String query() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();

    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Album");
    QueryResponse<AlbumModel> response = em.executeEntityQueryRequest(AlbumModel.class, request);
    List<AlbumModel> albums = response.getResults();

    JsonArray jsonList = new JsonArray();
    for (AlbumModel a : albums) {
    	jsonList.add(a.toJson());
    }

    return jsonList.toString();
  }

  @RequestMapping(method = RequestMethod.POST)
  public void create(@RequestParam String name) {
    AlbumModel album = new AlbumModel();
    album.setName(name);

    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.insert(album);
  }

  @RequestMapping(value="/{id}", method = RequestMethod.GET)
  public String getAlbum(@PathVariable long id) {
    return String.format("Album: %d", id);
  }
}
