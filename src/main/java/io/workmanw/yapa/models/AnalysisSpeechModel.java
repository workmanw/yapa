package io.workmanw.yapa.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jmethods.catatumbo.*;

import java.util.List;


@Entity(kind = "AnalysisSpeech")
public class AnalysisSpeechModel extends BaseModel {
  @Embeddable
  static public class EntityItem {
    private String name;
    private String type;
    private String mid;
    private String wikipediaUrl;
    private float salience;

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getType() {
      return this.type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getMid() {
      return this.mid;
    }

    public void setMid(String mid) {
      this.mid = mid;
    }

    public String getWikipediaUrl() {
      return this.wikipediaUrl;
    }

    public void setWikipediaUrl(String wikipediaUrl) {
      this.wikipediaUrl = wikipediaUrl;
    }

    public float getSalience() {
      return this.salience;
    }

    public void setSalience(float salience) {
      this.salience = salience;
    }

    public JsonObject toJson() {
      JsonObject jsonObj = new JsonObject();
      jsonObj.addProperty("name", this.getName());
      jsonObj.addProperty("type", this.getType());
      jsonObj.addProperty("mid", this.getMid());
      jsonObj.addProperty("wikipediaUrl", this.getWikipediaUrl());
      jsonObj.addProperty("salience", this.getSalience());
      return jsonObj;
    }
  }

  @Identifier
  protected long id;
  @Key
  protected DatastoreKey key;

  @Property(indexed = false)
  private String transcript;
  private List<EntityItem> entities;

  public AnalysisSpeechModel() {
  }

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

  public String getTranscript() {
    return this.transcript;
  }

  public void setTranscript(String transcript) {
    this.transcript = transcript;
  }

  public List<EntityItem> getEntities() {
    return this.entities;
  }

  public void setEntities(List<EntityItem> entities) {
    this.entities = entities;
  }

  public JsonObject toJson() {
    JsonObject speechJsonObj = new JsonObject();
    JsonArray entitiesJson = new JsonArray();
    for (EntityItem entityItem : this.getEntities()) {
      entitiesJson.add(entityItem.toJson());
    }
    speechJsonObj.addProperty("transcript", this.getTranscript());
    speechJsonObj.add("entity", entitiesJson);
    return speechJsonObj;
  }

  public String getSearchText() {
    StringBuilder strBuilder = new StringBuilder();
    List<EntityItem> entities = this.getEntities();
    for (EntityItem entityItem : entities) {
      strBuilder.append(entityItem.getName() + " ");
    }
    return strBuilder.toString();
  }

  public static AnalysisSpeechModel getById(String sId) {
    return BaseModel.getById(AnalysisSpeechModel.class, sId);
  }

  public static AnalysisSpeechModel getById(long id) {
    return BaseModel.getById(AnalysisSpeechModel.class, id);
  }

  public static AnalysisSpeechModel getByKey(DatastoreKey key) {
    return BaseModel.getByKey(AnalysisSpeechModel.class, key);
  }
}
