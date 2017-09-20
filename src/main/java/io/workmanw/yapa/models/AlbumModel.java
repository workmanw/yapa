package io.workmanw.yapa.models;

import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Key;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Entity(kind="Album")
public class AlbumModel extends BaseModel {
  public AlbumModel() { }
  public String getKind() { return "Album"; }

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
}
