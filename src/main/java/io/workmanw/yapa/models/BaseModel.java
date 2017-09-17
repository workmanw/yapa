package io.workmanw.yapa.models;

import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityManager;

import com.google.gson.JsonObject;

public class BaseModel extends Object {
  public BaseModel() { }

  public BaseModel createModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    return em.insert(this);
  }

  public void saveModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.update(this);
  }

  public void deleteModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.delete(this);
  }

  public JsonObject toJson() {
    return new JsonObject();
  }

  public void fromJson(JsonObject jsonObj) {
  }
}
