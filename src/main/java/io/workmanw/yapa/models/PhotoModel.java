package io.workmanw.yapa.models;

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
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.JsonObject;

@Entity(kind="Photo")
public class PhotoModel extends BaseModel {
  protected static Class modelClass = PhotoModel.class;

  public PhotoModel() { }

  @Identifier
  protected long id;
  @Key
  protected DatastoreKey key;
  @CreatedTimestamp
  private Date createdOn;

  private DatastoreKey album;
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

    jsonObj.addProperty("isImage", this.isImage());
    jsonObj.addProperty("isAudio", this.isAudio());
    jsonObj.addProperty("isVideo", this.isVideo());

    TimeZone tz = TimeZone.getTimeZone("UTC");
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
    df.setTimeZone(tz);
    String createdOn = df.format(this.getCreatedOn());
    jsonObj.addProperty("createdOn", createdOn);

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

}
