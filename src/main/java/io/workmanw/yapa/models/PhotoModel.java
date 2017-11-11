package io.workmanw.yapa.models;

import io.workmanw.yapa.utils.VisionClient;
import io.workmanw.yapa.utils.VideoIntelClient;
import io.workmanw.yapa.utils.SearchClient;
import io.workmanw.yapa.utils.TranscriptionClient;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.images.ServingUrlOptions;

import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Key;
import com.jmethods.catatumbo.CreatedTimestamp;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.JsonArray;
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
  @CreatedTimestamp
  private Date createdOn;

  private DatastoreKey album;
  private DatastoreKey analysisVision;
  private DatastoreKey analysisVideoIntel;
  private String photoName;
  private String blobKey;
  private String contentType;
  private String filename;
  private String gcsPath;
  private String md5;
  private long filesize;
  private String servingUrl;

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

  public DatastoreKey getAnalysisVision() {
    return this.analysisVision;
  }
  public void setAnalysisVision(DatastoreKey analysisVision) {
    this.analysisVision = analysisVision;
  }
  public AnalysisVisionModel fetchAnalysisVision() {
    return AnalysisVisionModel.getByKey(this.analysisVision);
  }

  public DatastoreKey getAnalysisVideoIntel() {
    return this.analysisVideoIntel;
  }
  public void setAnalysisVideoIntel(DatastoreKey analysisVideoIntel) {
    this.analysisVideoIntel = analysisVideoIntel;
  }
  public AnalysisVideoIntelModel fetchAnalysisVideoIntel() {
    return AnalysisVideoIntelModel.getByKey(this.analysisVideoIntel);
  }

  public Date getCreatedOn() {
    return this.createdOn;
  }
  public void setCreatedOn(Date createdOn) {
    this.createdOn = createdOn;
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

  public Boolean isImage() {
    return this.getContentType().startsWith("image/");
  }
  public Boolean isAudio() {
    return this.getContentType().startsWith("audio/");
  }
  public Boolean isVideo() {
    return this.getContentType().startsWith("video/");
  }

  public void fromBlobInfo(AlbumModel album, BlobInfo bi) {
    this.setAlbum(album.getKey());

    BlobKey bk = bi.getBlobKey();
    this.setBlobKey(bk.getKeyString());
    this.setContentType(bi.getContentType());
    this.setPhotoName(bi.getFilename());
    this.setFilename(bi.getFilename());
    this.setGcsPath(bi.getGsObjectName());
    this.setMd5(bi.getMd5Hash());
    this.setFilesize(bi.getSize());

    this.fetchServingURL();
  }

  // ................................................................
  // Serialization support
  //
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

    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    df.setTimeZone(tz);
    String createdOn = df.format(this.getCreatedOn());
    jsonObj.addProperty("createdOn", createdOn);

    // jsonObj.add("vision", this.visionDataToJson());
    // jsonObj.add("videoIntel", this.videoIntelDataToJson());
    jsonObj.addProperty("speechTranscript", this.getSpeechTranscipt());

    return jsonObj;
  }

  // ................................................................
  // Image support
  //
  protected void fetchServingURL() {
    String blobKey = this.getBlobKey();
    BlobKey bk = null;
    if (blobKey != null) {
      bk = new BlobKey(blobKey);
    }
    if (this.isImage() && bk != null) {
      ImagesService imagesService = ImagesServiceFactory.getImagesService();
      ServingUrlOptions options = ServingUrlOptions.Builder.withBlobKey(bk);
      String servingUrl = imagesService.getServingUrl(options);
      this.setServingUrl(servingUrl);
    }
  }

  // ................................................................
  // Analysis support
  //
  public void populateAnalysisData() {
    // https://cloud.google.com/video-intelligence/docs/reference/libraries
    if (this.isImage()) {
      this.populateVisionData();
    } else if (this.isAudio()) {
      this.transcribeAudioFile();
      this.saveModel();
    } else if (this.isVideo()) {
      this.populateVideoIntelligence();
      this.saveModel();
    }
  }

  public void populateVisionData() {
    VisionClient visionClient = new VisionClient();
    AnalysisVisionModel analysisVision = visionClient.analyzeImage("gs:/" + this.getGcsPath());

    analysisVision = (AnalysisVisionModel) analysisVision.createModel();
    this.setAnalysisVision(analysisVision.getKey());
    this.saveModel();
  }

  private String speechTranscipt;
  public String getSpeechTranscipt() {
    return this.speechTranscipt;
  }
  public void setSpeechTranscipt(String speechTranscipt) {
    this.speechTranscipt = speechTranscipt;
  }

  public void transcribeAudioFile() {
    TranscriptionClient speechClient = new TranscriptionClient();
    String text = speechClient.analyzeSpeech("gs:/" + this.getGcsPath());
    this.setSpeechTranscipt(text);
  }

  public void populateVideoIntelligence() {
    VideoIntelClient videoClient = new VideoIntelClient();
    AnalysisVideoIntelModel analysisVideoIntel = videoClient.analyzeVideo("gs:/" + this.getGcsPath());

    analysisVideoIntel = (AnalysisVideoIntelModel) analysisVideoIntel.createModel();
    this.setAnalysisVideoIntel(analysisVideoIntel.getKey());
    this.saveModel();
  }

  // ................................................................
  // Search support
  //
  public String getSearchText() {
    StringBuilder strBuilder = new StringBuilder();
    strBuilder.append(this.getPhotoName());
//    if (this.isImage()) {
//      List<VisionModel> visionModels = new ArrayList<VisionModel>();
//      visionModels.addAll(this.getVisionLabels());
//      visionModels.addAll(this.getVisionLandmarks());
//      visionModels.addAll(this.getVisionLogos());
//      visionModels.addAll(this.getVisionTexts());
//      for (VisionModel visionModel : visionModels) {
//        strBuilder.append(visionModel.getDescription() + " ");
//      }
//    }
    return strBuilder.toString();
  }

  // ................................................................
  // Static utils
  //
  public static PhotoModel getById(String sId) {
    return BaseModel.getById(PhotoModel.class, sId);
  }
  public static PhotoModel getById(long id) {
    return BaseModel.getById(PhotoModel.class, id);
  }
  public static PhotoModel getByKey(DatastoreKey key) {
    return BaseModel.getByKey(PhotoModel.class, key);
  }

  public static void postProcess(String action, String id) {
    SearchClient sc = new SearchClient();

    if (action.equals("CREATE")) {
      PhotoModel photo = PhotoModel.getById(id);
      photo.populateAnalysisData();

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
