package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.PhotoModel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends BaseController<PhotoModel> {
  public PhotoController() {
    modelClass = PhotoModel.class;
    entityKind = "Photo";
  }

  @RequestMapping(value="/upload_url", method=RequestMethod.GET)
  public String getUploadUrl() {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    String uploadUrl = blobstoreService.createUploadUrl("/_photo_upload");
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("uploadUrl", uploadUrl);
    return jsonObj.toString();
  }

  public String uploadCallback(BlobKey bk) {
    return bk.getKeyString();
  }
}
