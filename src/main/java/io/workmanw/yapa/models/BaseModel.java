package io.workmanw.yapa.models;

import com.jmethods.catatumbo.DatastoreKey;
import io.workmanw.yapa.utils.TaskClient;

import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityManager;

import com.google.gson.JsonObject;

public class BaseModel extends Object {
  public BaseModel() { }

  public long getId() { return 0; }
  public String getKind() { return ""; }

  public BaseModel createModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    BaseModel newModel = em.insert(this);
    newModel.schedulePostProcess("CREATE");
    return newModel;
  }

  public void saveModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.update(this);
    this.schedulePostProcess("UPDATE");
  }

  public void deleteModel() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    em.delete(this);
    this.schedulePostProcess("DELETE");
  }

  public void schedulePostProcess(String action) {
    String kind = this.getKind();
    String sId = Long.toString(this.getId(), 10);
    TaskClient taskClient = new TaskClient();
    taskClient.scheduleModelPostProcess(action, kind, sId);
  }

  public JsonObject toJson() {
    return new JsonObject();
  }

  public void fromJson(JsonObject jsonObj) { }

  public static <E> E getById(Class<E> modelClass, String sId) {
    long id = Long.parseLong(sId, 10);
    return BaseModel.getById(modelClass, id);
  }

  public static <E> E getById(Class<E> modelClass, long id) {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    return em.load(modelClass, id);
  }

  public static <E> E getByKey(Class<E> modelClass, DatastoreKey key) {
    return BaseModel.getById(modelClass, key.id());
  }


  public static void postProcess(String action, String id) { }
}
