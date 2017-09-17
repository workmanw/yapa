package io.workmanw.yapa.models;

import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

import com.google.gson.JsonObject;

@Entity(kind="Album")
public class AlbumModel {
  @Identifier
  private long id;

  private String name;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public JsonObject toJson() {
    JsonObject obj = new JsonObject();
    obj.addProperty("id", this.id);
    obj.addProperty("name", this.name);
    return obj;
  }
}
