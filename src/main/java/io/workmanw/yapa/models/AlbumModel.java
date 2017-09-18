package io.workmanw.yapa.models;

import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Key;

import com.google.gson.JsonObject;

@Entity(kind="Album")
public class AlbumModel extends BaseModel {
  public AlbumModel() { }

  @Identifier
  private long id;

  @Key
  private DatastoreKey key;

  private String name;

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

  public JsonObject toJson() {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("id", this.id);
    jsonObj.addProperty("name", this.name);
    return jsonObj;
  }

  public void fromJson(JsonObject jsonObj) {
    this.setName(jsonObj.get("name").getAsString());
  }
}
