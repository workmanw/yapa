package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.PhotoModel;

import java.util.Map;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends BaseController<PhotoModel> {
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  public PhotoController() {
    modelClass = PhotoModel.class;
    entityKind = "Photo";
  }

  @RequestMapping(value="/upload_url", method=RequestMethod.GET)
  public String getUploadUrl() {
    UploadOptions uploadOpts = UploadOptions.Builder.withGoogleStorageBucketName("yapa-assets-0");
    String uploadUrl = blobstoreService.createUploadUrl("/_photo_upload", uploadOpts);
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("uploadUrl", uploadUrl);
    return jsonObj.toString();
  }

  public String uploadCallback(BlobKey bk, Map<String, String> parameters) {
    BlobInfo bi = this.getBlobInfo(bk);

    PhotoModel photo = this.createModelInstance();
    photo.fromBlobInfo(bi);
    photo = (PhotoModel) photo.createModel();

    return this.serialize(photo);
  }

  protected BlobInfo getBlobInfo(BlobKey bk) {
    BlobInfoFactory f = new BlobInfoFactory();
    return f.loadBlobInfo(bk);
  }
}
