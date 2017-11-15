package io.workmanw.yapa.models;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jmethods.catatumbo.*;

import java.util.ArrayList;
import java.util.List;


@Entity(kind = "AnalysisVideoIntel")
public class AnalysisVideoIntelModel extends BaseModel {
  @Embeddable
  static public class VideoIntelItem {
    public static final String TYPE_SHOT_LABEL = "Shot";
    public static final String TYPE_SEGEMENT_LABEL = "Segement";
    public static final String TYPE_FRAME_LABEL = "Frame";

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

  private List<VideoIntelItem> videoIntelItems;

  public AnalysisVideoIntelModel() {
    this.videoIntelItems = new ArrayList<>();
  }

  public AnalysisVideoIntelModel(List<VideoIntelItem> videoIntelItems) {
    this.videoIntelItems = videoIntelItems;
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

  public List<VideoIntelItem> getVideoIntelItems() {
    return this.videoIntelItems;
  }

  public void setVideoIntelItems(List<VideoIntelItem> videoIntelItems) {
    this.videoIntelItems = videoIntelItems;
  }

  public List<VideoIntelItem> getVideoIntelSegments() {
    return this.videoIntelItemsByType(VideoIntelItem.TYPE_SEGEMENT_LABEL);
  }

  public List<VideoIntelItem> getVideoIntelShots() {
    return this.videoIntelItemsByType(VideoIntelItem.TYPE_SHOT_LABEL);
  }

  public List<VideoIntelItem> getVideoIntelFrames() {
    return this.videoIntelItemsByType(VideoIntelItem.TYPE_FRAME_LABEL);
  }

  protected List<VideoIntelItem> videoIntelItemsByType(String type) {
    List<VideoIntelItem> results = new ArrayList<>();
    for (VideoIntelItem videoIntelItem : this.getVideoIntelItems()) {
      if (videoIntelItem.getType().equals(type)) {
        results.add(videoIntelItem);
      }
    }
    return results;
  }

  public JsonObject toJson() {
    JsonObject videoIntelJsonObj = new JsonObject();
    videoIntelJsonObj.add("segments", this.videoIntelItemsToJson(this.getVideoIntelSegments()));
    videoIntelJsonObj.add("shots", this.videoIntelItemsToJson(this.getVideoIntelShots()));
    videoIntelJsonObj.add("frames", this.videoIntelItemsToJson(this.getVideoIntelFrames()));
    return videoIntelJsonObj;
  }

  protected JsonArray videoIntelItemsToJson(List<VideoIntelItem> models) {
    JsonArray videoIntelLabelsJson = new JsonArray();
    if (models != null) {
      for (VideoIntelItem videoIntelItem : models) {
        videoIntelLabelsJson.add(videoIntelItem.toJson());
      }
    }
    return videoIntelLabelsJson;
  }


  public String getSearchText() {
    StringBuilder strBuilder = new StringBuilder();
    List<VideoIntelItem> videoIntelItems = this.getVideoIntelItems();
    for (VideoIntelItem videoIntelItem : videoIntelItems) {
      strBuilder.append(videoIntelItem.getDescription() + " ");
    }
    return strBuilder.toString();
  }

  public static AnalysisVideoIntelModel getById(String sId) {
    return BaseModel.getById(AnalysisVideoIntelModel.class, sId);
  }

  public static AnalysisVideoIntelModel getById(long id) {
    return BaseModel.getById(AnalysisVideoIntelModel.class, id);
  }

  public static AnalysisVideoIntelModel getByKey(DatastoreKey key) {
    return BaseModel.getByKey(AnalysisVideoIntelModel.class, key);
  }
}
