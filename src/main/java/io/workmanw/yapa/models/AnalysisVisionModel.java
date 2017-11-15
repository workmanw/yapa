package io.workmanw.yapa.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jmethods.catatumbo.*;

import java.util.ArrayList;
import java.util.List;


@Entity(kind = "AnalysisVision")
public class AnalysisVisionModel extends BaseModel {
  @Embeddable
  static public class VisionItem {
    public static final String TYPE_LABEL = "Label";
    public static final String TYPE_LANDMARK = "Landmark";
    public static final String TYPE_LOGO = "Logo";
    public static final String TYPE_TEXT = "Text";

    private String type;
    private String mid;
    private String description;
    private float score;

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

    public String getDescription() {
      return this.description;
    }

    public void setDescription(String description) {
      this.description = description;
    }

    public float getScore() {
      return this.score;
    }

    public void setScore(float score) {
      this.score = score;
    }

    public JsonObject toJson() {
      JsonObject jsonObj = new JsonObject();
      jsonObj.addProperty("mid", this.getMid());
      jsonObj.addProperty("description", this.getDescription());
      jsonObj.addProperty("score", this.getScore());
      return jsonObj;
    }
  }

  @Identifier
  protected long id;
  @Key
  protected DatastoreKey key;

  private List<VisionItem> visionItems;

  public AnalysisVisionModel() {
    this.visionItems = new ArrayList<>();
  }

  public AnalysisVisionModel(List<VisionItem> visionItems) {
    this.visionItems = visionItems;
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

  public List<VisionItem> getVisionItems() {
    return this.visionItems;
  }

  public void setVisionItems(List<VisionItem> visionItems) {
    this.visionItems = visionItems;
  }

  public List<VisionItem> getVisionLabels() {
    return this.visionItemsByType(VisionItem.TYPE_LABEL);
  }

  public List<VisionItem> getVisionLandmarks() {
    return this.visionItemsByType(VisionItem.TYPE_LANDMARK);
  }

  public List<VisionItem> getVisionLogos() {
    return this.visionItemsByType(VisionItem.TYPE_LOGO);
  }

  public List<VisionItem> getVisionTexts() {
    return this.visionItemsByType(VisionItem.TYPE_TEXT);
  }

  protected List<VisionItem> visionItemsByType(String type) {
    List<VisionItem> results = new ArrayList<>();
    for (VisionItem visionItem : this.getVisionItems()) {
      if (visionItem.getType().equals(type)) {
        results.add(visionItem);
      }
    }
    return results;
  }

  public JsonObject toJson() {
    JsonObject visionJsonObj = new JsonObject();
    visionJsonObj.add("labels", this.visionItemsToJson(this.getVisionLabels()));
    visionJsonObj.add("landmarks", this.visionItemsToJson(this.getVisionLandmarks()));
    visionJsonObj.add("logos", this.visionItemsToJson(this.getVisionLogos()));
    visionJsonObj.add("texts", this.visionItemsToJson(this.getVisionTexts()));
    return visionJsonObj;
  }

  protected JsonArray visionItemsToJson(List<VisionItem> models) {
    JsonArray visionLabelsJson = new JsonArray();
    if (models != null) {
      for (VisionItem visionItem : models) {
        visionLabelsJson.add(visionItem.toJson());
      }
    }
    return visionLabelsJson;
  }

  public String getSearchText() {
    StringBuilder strBuilder = new StringBuilder();
    List<VisionItem> visionItems = this.getVisionItems();
    for (VisionItem visionItem : visionItems) {
      strBuilder.append(visionItem.getDescription() + " ");
    }
    return strBuilder.toString();
  }

  public static AnalysisVisionModel getById(String sId) {
    return BaseModel.getById(AnalysisVisionModel.class, sId);
  }

  public static AnalysisVisionModel getById(long id) {
    return BaseModel.getById(AnalysisVisionModel.class, id);
  }

  public static AnalysisVisionModel getByKey(DatastoreKey key) {
    return BaseModel.getByKey(AnalysisVisionModel.class, key);
  }
}
