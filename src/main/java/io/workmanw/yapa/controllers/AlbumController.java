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

@RestController
@RequestMapping("/api/v1/album")
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

    JsonObject obj = new JsonObject();
    obj.add("album", jsonList);
    return obj.toString();
  }

  @RequestMapping(method = RequestMethod.POST, consumes={"application/json"})
  public String create(@RequestBody String body) {
    JsonParser parser = new JsonParser();
    JsonObject json = parser.parse(body).getAsJsonObject();
    JsonObject jsonAlbum = json.getAsJsonObject("album");

    AlbumModel albumModel = new AlbumModel();
    albumModel.setName(jsonAlbum.get("name").getAsString());
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.insert(albumModel);

    JsonObject obj = new JsonObject();
    JsonArray jsonList = new JsonArray();
    jsonList.add(albumModel.toJson());
    obj.add("album", jsonList);
    return obj.toString();
  }

  @RequestMapping(value="/{id}", method = RequestMethod.GET)
  public String getAlbum(@PathVariable long id) {
    return String.format("Album: %d", id);
  }
}
