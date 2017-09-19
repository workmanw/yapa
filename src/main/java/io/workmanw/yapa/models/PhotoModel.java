package io.workmanw.yapa.models;

import io.workmanw.yapa.utils.VisionClient;
import io.workmanw.yapa.utils.SearchClient;

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

import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Entity(kind="Photo")
public class PhotoModel extends BaseModel {
  protected static Class modelClass = PhotoModel.class;

  public PhotoModel() { }
  public String getKind() { return "Photo"; }

  @Identifier
  protected long id;
  @Key
  protected DatastoreKey key;

	private DatastoreKey album;
  private String photoName;
  private String blobKey;
  private String contentType;
  private String filename;
  private String gcsPath;
  private String md5;
  private long filesize;
  private String servingUrl;

  private List<VisionModel> visionLabels;
  private List<VisionModel> visionLandmarks;
  private List<VisionModel> visionLogos;
  private List<VisionModel> visionTexts;

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
  public String getAlbumId() {
    return Long.toString(this.getAlbum().id(), 10);
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

  public List<VisionModel> getVisionLabels() {
    return this.visionLabels;
  }
  public void setVisionLabels(List<VisionModel> visionLabels) {
    this.visionLabels = visionLabels;
  }

  public List<VisionModel> getVisionLandmarks() {
    return this.visionLandmarks;
  }
  public void setVisionLandmarks(List<VisionModel> visionLandmarks) {
    this.visionLandmarks = visionLandmarks;
  }

  public List<VisionModel> getVisionLogos() {
    return this.visionLogos;
  }
  public void setVisionLogos(List<VisionModel> visionLogos) {
    this.visionLogos = visionLogos;
  }

  public List<VisionModel> getVisionTexts() {
    return this.visionTexts;
  }
  public void setVisionTexts(List<VisionModel> visionTexts) {
    this.visionTexts = visionTexts;
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

    JsonObject visionJsonObj = new JsonObject();
    visionJsonObj.add("labels", this.visionListToJson(this.getVisionLabels()));
    visionJsonObj.add("landmarks", this.visionListToJson(this.getVisionLandmarks()));
    visionJsonObj.add("logos", this.visionListToJson(this.getVisionLogos()));
    visionJsonObj.add("texts", this.visionListToJson(this.getVisionTexts()));
    jsonObj.add("vision", visionJsonObj);

    return jsonObj;
  }

  protected JsonArray visionListToJson(List<VisionModel> models) {
    JsonArray visionLabelsJson = new JsonArray();
    if (models != null) {
      for (VisionModel visionLabel : models) {
        visionLabelsJson.add(visionLabel.toJson());
      }
    }
    return visionLabelsJson;
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

    List<VisionModel> visionLabels = visionClient.detectLabels("gs:/" + this.getGcsPath());
    this.setVisionLabels(visionLabels);

    List<VisionModel> visionLandmarks = visionClient.detectLandmarks("gs:/" + this.getGcsPath());
    this.setVisionLandmarks(visionLandmarks);

    List<VisionModel> visionLogos = visionClient.detectLogos("gs:/" + this.getGcsPath());
    this.setVisionLogos(visionLogos);

    List<VisionModel> visionTexts = visionClient.detectText("gs:/" + this.getGcsPath());
    this.setVisionTexts(visionTexts);
  }

  public String getSearchText() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(this.getPhotoName());
    List<VisionModel> visionModels = new ArrayList<VisionModel>();
    visionModels.addAll(this.getVisionLabels());
    visionModels.addAll(this.getVisionLandmarks());
    visionModels.addAll(this.getVisionLogos());
    visionModels.addAll(this.getVisionTexts());
    for (VisionModel visionModel : visionModels) {
      strBuilder.append(visionModel.getDescription() + " ");
    }
    return strBuilder.toString();
  }

  public static PhotoModel getById(String sId) {
    return BaseModel.getById(PhotoModel.class, sId);
  }
  public static PhotoModel getById(long id) {
    return BaseModel.getById(PhotoModel.class, id);
  }

  public static void postProcess(String action, String id) {
    SearchClient sc = new SearchClient();

    if (action.equals("CREATE")) {
      PhotoModel photo = PhotoModel.getById(id);
      photo.populateVisionData();
      photo.saveModel();

      sc.createPhotoDocument(photo);

      AlbumModel albumModel = AlbumModel.getById(photo.getAlbumId());
      albumModel.addPreviewImageUrl(photo.getServingUrl());
    } else if (action.equals("UPDATE")) {
      PhotoModel photo = PhotoModel.getById(id);
      sc.updatePhotoDocument(photo);
    } else if (action.equals("DELETE")) {
      sc.deletePhotoDocument(id);
    }
  }
}
