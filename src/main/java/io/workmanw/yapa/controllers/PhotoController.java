package io.workmanw.yapa.controllers;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.workmanw.yapa.models.PhotoModel;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends BaseController<PhotoModel> {
  public PhotoController() {
    modelClass = PhotoModel.class;
    entityKind = "Photo";
  }

  @RequestMapping(value="upload_url", method=RequestMethod.GET)
  public String getUploadUrl() {
    BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    return blobstoreService.createUploadUrl("/api/v1/photo/upload_callback");
  }

  @RequestMapping(value="upload_callback", method=RequestMethod.GET)
  public String uploadCallback(@RequestBody String body) {
    // JsonObject json = this.extractJson(body);
    return body;
  }
}
