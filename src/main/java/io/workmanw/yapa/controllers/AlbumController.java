package io.workmanw.yapa.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;

import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import io.workmanw.yapa.models.AlbumModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/album")
public class AlbumController {
  @RequestMapping(method = RequestMethod.GET)
  public String queryEntities() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();

    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Album");
    QueryResponse<AlbumModel> response = em.executeEntityQueryRequest(AlbumModel.class, request);
    List<AlbumModel> albums = response.getResults();

    return this.serialize(albums);
  }

  @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
  public String createEntity(@RequestBody String body) {
    JsonObject jsonAlbum = this.extractEntityJson(body);
    AlbumModel album = new AlbumModel();

    album.fromJson(jsonAlbum);
    album = (AlbumModel) album.createModel();

    return this.serialize(album);
  }

  @RequestMapping(value="/{id}", method = RequestMethod.GET)
  public String getEntity(@PathVariable long id) {
    AlbumModel album = this.getEntityById(id);
    return this.serialize(album);
  }

  @RequestMapping(value="/{id}", method = RequestMethod.PUT)
  public String putEntity(@PathVariable long id, @RequestBody String body) {
    JsonObject jsonAlbum = this.extractEntityJson(body);
    AlbumModel album = this.getEntityById(id);

    album.fromJson(jsonAlbum);
    album.saveModel();

    return this.serialize(album);
  }

  @RequestMapping(value="/{id}", method = RequestMethod.DELETE)
  public String deleteEntity(@PathVariable long id) {
    AlbumModel album = this.getEntityById(id);
    album.deleteModel();
    album = null;

    return this.serialize(album);
  }

  protected String serialize(AlbumModel album) {
    List<AlbumModel> albums = new ArrayList<AlbumModel>();
    if (album != null) {
      albums.add(album);
    }
    return this.serialize(albums);
  }

  protected String serialize(List<AlbumModel> albums) {
    JsonArray jsonList = new JsonArray();
    for (AlbumModel a : albums) {
      jsonList.add(a.toJson());
    }

    JsonObject obj = new JsonObject();
    obj.add("album", jsonList);
    return obj.toString();
  }

  protected JsonObject extractEntityJson(String body) {
    JsonParser parser = new JsonParser();
    JsonObject json = parser.parse(body).getAsJsonObject();
    JsonObject entityJson = json.getAsJsonObject("album");
    return entityJson;
  }

  protected AlbumModel getEntityById(long id) {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    return em.load(AlbumModel.class, id);
  }
}
