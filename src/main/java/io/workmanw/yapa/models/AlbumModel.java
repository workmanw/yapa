package io.workmanw.yapa.models;

import com.jmethods.catatumbo.*;

import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity(kind="Album")
public class AlbumModel extends BaseModel {
  public AlbumModel() { }

  @Identifier
  protected long id;
  @Key
  protected DatastoreKey key;

  private String name;

  private List<String> previewImageUrls;

  public long getId() {
    return this.id;
  }
  public void setId(long id) {
    this.id = id;
  }

  public DatastoreKey getKey() {
    return this.key;
  }
  public void setKey(DatastoreKey key) {
    this.key = key;
  }

  public String getName() {
    return this.name;
  }
  public void setName(String name) {
    this.name = name;
  }

  public List<String> getPreviewImageUrls() {
    return this.previewImageUrls;
  }
  public void setPreviewImageUrls(List<String> previewImageUrls) {
    this.previewImageUrls = previewImageUrls;
  }

  public void addPreviewImageUrl(String url) {
    List<String> urls = this.getPreviewImageUrls();
    urls.add(0, url);
    if (urls.size() > 4) {
      urls = new ArrayList<String>(urls.subList(0, 4));
    }
    this.setPreviewImageUrls(urls);
    this.saveModel();
  }

  public JsonObject toJson() {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("id", this.id);
    jsonObj.addProperty("name", this.name);

    JsonArray jsonUrls = new JsonArray();
    List<String> urls = this.getPreviewImageUrls();
    if (urls != null) {
      for (String url : urls) {
        jsonUrls.add(url);
      }
    }
    jsonObj.add("previewImageUrls", jsonUrls);

    return jsonObj;
  }

  public void fromJson(JsonObject jsonObj) {
    this.setName(jsonObj.get("name").getAsString());
    this.setPreviewImageUrls(new ArrayList<String>());
  }

  public static AlbumModel getById(String sId) {
    return BaseModel.getById(AlbumModel.class, sId);
  }
  public static AlbumModel getById(long id) {
    return BaseModel.getById(AlbumModel.class, id);
  }
  public static AlbumModel getByKey(DatastoreKey key) {
    return BaseModel.getByKey(AlbumModel.class, key);
  }

  public static List<PhotoModel> queryPhotos(String albumId) {
    // This should really be refactor into a Query Manager class(es)
    // This stuff goes on in the controllers and the models.
    long id = Long.parseLong(albumId, 10);
    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
    KeyFactory keyFactory = datastore.newKeyFactory().setKind("Album");
    com.google.cloud.datastore.Key dsKey = keyFactory.newKey(id);
    DatastoreKey albumKey = new DefaultDatastoreKey(dsKey);

    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Photo WHERE album=@1 ORDER BY createdOn DESC");
    request.addPositionalBinding(albumKey);
    QueryResponse<PhotoModel> response = em.executeEntityQueryRequest(PhotoModel.class, request);
    return response.getResults();
  }

  public static void postProcess(String id, String action) {
    if (action.equals("DELETE")) {
      List<PhotoModel> photos = AlbumModel.queryPhotos(id);
      for (PhotoModel photo : photos) {
        photo.deleteModel();
      }
    }
  }
}
