package io.workmanw.yapa.models;

import io.workmanw.yapa.utils.VisionClient;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Exploded;
import com.jmethods.catatumbo.Embedded;
import com.jmethods.catatumbo.Key;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Entity(kind="Photo")
public class PhotoModel extends BaseModel {
  public PhotoModel() { }

  @Identifier
  private long id;
  @Key
  private DatastoreKey key;

	private DatastoreKey album;
  private String photoName;
  private String blobKey;
  private String contentType;
  private String filename;
  private String gcsPath;
  private String md5;
  private long filesize;
  private String servingUrl;

  private List<VisionLabel> visionLabels;

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

  public DatastoreKey getAlbum() {
    return this.album;
  }
  public void setAlbum(DatastoreKey album) {
    this.album = album;
  }

  public String getPhotoName() {
    return this.photoName;
  }
  public void setPhotoName(String photoName) {
    this.photoName = photoName;
  }

  public String getBlobKey() {
    return this.blobKey;
  }
  public void setBlobKey(String blobKey) {
    this.blobKey = blobKey;
  }

  public String getContentType() {
    return this.contentType;
  }
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFilename() {
    return this.filename;
  }
  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getGcsPath() {
    return this.gcsPath;
  }
  public void setGcsPath(String gcsPath) {
    this.gcsPath = gcsPath;
  }

  public String getMd5() {
    return this.md5;
  }
  public void setMd5(String md5) {
    this.md5 = md5;
  }

  public long getFilesize() {
    return this.filesize;
  }
  public void setFilesize(long filesize) {
    this.filesize = filesize;
  }

  public String getServingUrl() {
    return this.servingUrl;
  }
  public void setServingUrl(String servingUrl) {
    this.servingUrl = servingUrl;
  }

  public List<VisionLabel> getVisionLabels() {
    return this.visionLabels;
  }
  public void setVisionLabels(List<VisionLabel> visionLabels) {
    this.visionLabels = visionLabels;
  }

  public JsonObject toJson() {
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("id", this.getId());
    jsonObj.addProperty("album", this.getAlbum().id());
    jsonObj.addProperty("photoName", this.getPhotoName());
    jsonObj.addProperty("blobKey", this.getBlobKey());
    jsonObj.addProperty("content", this.getContentType());
    jsonObj.addProperty("filename", this.getFilename());
    jsonObj.addProperty("gcsPath", this.getGcsPath());
    jsonObj.addProperty("md5", this.getMd5());
    jsonObj.addProperty("filesize", this.getFilesize());
    jsonObj.addProperty("servingUrl", this.getServingUrl());
    jsonObj.add("vision", this.visionToJson());

    return jsonObj;
  }

  protected JsonObject visionToJson() {
    JsonObject visionJsonObj = new JsonObject();

    List<VisionLabel> visionLabels = this.getVisionLabels();
    JsonArray visionLabelsJson = new JsonArray();
    if (visionLabels != null) {
      for (VisionLabel visionLabel : visionLabels) {
        visionLabelsJson.add(visionLabel.toJson());
      }
    }
    visionJsonObj.add("labels", visionLabelsJson);

    return visionJsonObj;
  }

  public void fromBlobInfo(AlbumModel album, BlobInfo bi) {
    this.setAlbum(album.getKey());

    BlobKey bk = bi.getBlobKey();
    this.setPhotoName(bi.getFilename());

    this.setBlobKey(bk.getKeyString());
    this.setContentType(bi.getContentType());
    this.setFilename(bi.getFilename());
    this.setGcsPath(bi.getGsObjectName());
    this.setMd5(bi.getMd5Hash());
    this.setFilesize(bi.getSize());

    ImagesService imagesService = ImagesServiceFactory.getImagesService();
    ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(bk);
    String servingUrl = imagesService.getServingUrl(options);
    this.setServingUrl(servingUrl);
  }

  public void populateVisionData() {
    VisionClient visionClient = new VisionClient();
    this._populateVisionLabelData(visionClient);
  }

  public void _populateVisionLabelData(VisionClient visionClient) {
    JsonArray visionLabelData = visionClient.detectLabelsGcs("gs:/" + this  .getGcsPath());
    List<VisionLabel> visionLabels = new ArrayList<VisionLabel>();
    for(JsonElement elem : visionLabelData) {
      JsonObject obj = elem.getAsJsonObject();
      VisionLabel label = new VisionLabel();
      label.setMid(obj.get("mid").getAsString());
      label.setDescription(obj.get("description").getAsString());
      label.setScore(obj.get("score").getAsFloat());
      visionLabels.add(label);
    }
    this.setVisionLabels(visionLabels);
  }

  public static PhotoModel getById(String sId) {
    return BaseModel.getById(PhotoModel.class, sId);
  }
  public static PhotoModel getById(long id) {
    return BaseModel.getById(PhotoModel.class, id);
  }
}
