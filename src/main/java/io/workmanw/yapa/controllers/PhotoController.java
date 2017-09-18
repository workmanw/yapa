package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.AlbumModel;
import io.workmanw.yapa.models.PhotoModel;

import java.util.Map;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.UploadOptions;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends BaseController<PhotoModel> {
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  public PhotoController() {
    modelClass = PhotoModel.class;
    entityKind = "Photo";
  }

  @RequestMapping(method=RequestMethod.GET, params = {"album"})
  public String queryEntities(@RequestParam("album") String albumId) {
    EntityManager em = this.getEntityManager();
    AlbumModel album = this.fetchAlbum(albumId);

    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Photo WHERE album=@1");
    request.addPositionalBinding(album.getKey());
    QueryResponse<PhotoModel> response = em.executeEntityQueryRequest(this.modelClass, request);
    List<PhotoModel> entities = response.getResults();

    return this.serialize(entities);
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

    AlbumModel album = this.fetchAlbum(parameters.get("album"));
    PhotoModel photo = this.createModelInstance();
    photo.fromBlobInfo(album, bi);
    photo = (PhotoModel) photo.createModel();

    return this.serialize(photo);
  }

  protected BlobInfo getBlobInfo(BlobKey bk) {
    BlobInfoFactory f = new BlobInfoFactory();
    return f.loadBlobInfo(bk);
  }

  protected AlbumModel fetchAlbum(String sAlbumId) {
    long albumId = Long.parseLong(sAlbumId, 10);
    EntityManager em = this.getEntityManager();
    return em.load(AlbumModel.class, albumId);
  }
}
