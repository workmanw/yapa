package io.workmanw.yapa.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.annotation.WebServlet;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.ArrayList;

import io.workmanw.yapa.models.BaseModel;

public class BaseController<T extends BaseModel> {
  protected Class<T> modelClass;
  protected String entityKind;

  @RequestMapping(method=RequestMethod.GET)
  public String queryEntities() {
    EntityManager em = this.getEntityManager();

    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM " + this.entityKind);
    QueryResponse<T> response = em.executeEntityQueryRequest(this.modelClass, request);
    List<T> entities = response.getResults();

    return this.serialize(entities);
  }

  @RequestMapping(method=RequestMethod.POST, consumes={"application/json"})
  public String createEntity(@RequestBody String body) {
    JsonObject jsonEntity = this.extractEntityJson(body);
    T entity = this.createModelInstance();

    entity.fromJson(jsonEntity);
    entity = (T) entity.createModel();

    return this.serialize(entity);
  }

  @RequestMapping(value="/{id}", method=RequestMethod.GET)
  public String getEntity(@PathVariable long id) {
    T entity = this.getEntityById(id);
    return this.serialize(entity);
  }

  @RequestMapping(value="/{id}", method=RequestMethod.PUT)
  public String putEntity(@PathVariable long id, @RequestBody String body) {
    JsonObject jsonEntity = this.extractEntityJson(body);
    T entity = this.getEntityById(id);

    entity.fromJson(jsonEntity);
    entity.saveModel();

    return this.serialize(entity);
  }

  @RequestMapping(value="/{id}", method=RequestMethod.DELETE)
  public String deleteEntity(@PathVariable long id) {
    T entity = this.getEntityById(id);
    entity.deleteModel();
    entity = null;
    return this.serialize(entity);
  }

  protected String serialize(T entity) {
    List<T> entities = new ArrayList<T>();
    if (entity != null) {
      entities.add(entity);
    }
    return this.serialize(entities);
  }

  protected String serialize(List<T> entities) {
    JsonObject jsonObj = new JsonObject();
    JsonArray jsonList = new JsonArray();
    for (T e : entities) {
      jsonList.add(e.toJson());
    }
    jsonObj.add(this.entityKind.toLowerCase(), jsonList);
    return jsonObj.toString();
  }

  protected JsonObject extractJson(String body) {
    JsonParser parser = new JsonParser();
    return parser.parse(body).getAsJsonObject();
  }

  protected JsonObject extractEntityJson(String body) {
    JsonObject json = this.extractJson(body);
    return json.getAsJsonObject(this.entityKind.toLowerCase());
  }

  protected T getEntityById(long id) {
    EntityManager em = this.getEntityManager();
    return em.load(this.modelClass, id);
  }

  protected EntityManager getEntityManager() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    return emf.createDefaultEntityManager();
  }

  protected T createModelInstance() {
    try {
      return this.modelClass.newInstance();
    } catch (InstantiationException e) {
    } catch (IllegalAccessException e) {
    }
    return null;
  }
}
