package io.workmanw.yapa.controllers;

import static io.workmanw.yapa.Constants.GCS_BUCKET;
import io.workmanw.yapa.models.AlbumModel;
import io.workmanw.yapa.models.AnalysisSpeechModel;
import io.workmanw.yapa.models.AnalysisVisionModel;
import io.workmanw.yapa.models.AnalysisVideoIntelModel;
import io.workmanw.yapa.models.PhotoModel;

import java.util.Map;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.web.bind.annotation.PathVariable;
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
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/api/v1/photo")
public class PhotoController extends BaseController<PhotoModel> {
  private static final Logger log = Logger.getLogger(PhotoController.class.getName());

  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  public PhotoController() {
    modelClass = PhotoModel.class;
    entityKind = "Photo";
  }

  @RequestMapping(method=RequestMethod.GET, params = {"album"})
  public String queryEntities(@RequestParam("album") String albumId) {
    EntityManager em = this.getEntityManager();
    AlbumModel album = AlbumModel.getById(albumId);

    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Photo WHERE album=@1 ORDER BY createdOn DESC");
    request.addPositionalBinding(album.getKey());
    QueryResponse<PhotoModel> response = em.executeEntityQueryRequest(this.modelClass, request);
    List<PhotoModel> entities = response.getResults();

    return this.serialize(entities);
  }

  @RequestMapping(value="/upload_url", method=RequestMethod.GET)
  public String getUploadUrl() {
    UploadOptions uploadOpts = UploadOptions.Builder.withGoogleStorageBucketName(GCS_BUCKET);
    String uploadUrl = blobstoreService.createUploadUrl("/_uploads/photo", uploadOpts);
    JsonObject jsonObj = new JsonObject();
    jsonObj.addProperty("uploadUrl", uploadUrl);
    return jsonObj.toString();
  }

  public String uploadCallback(BlobKey bk, Map<String, String> parameters) {
    BlobInfo bi = this.getBlobInfo(bk);

    AlbumModel album = AlbumModel.getById(parameters.get("album"));
    PhotoModel photo = this.createModelInstance();
    photo.fromBlobInfo(album, bi);
    photo = (PhotoModel) photo.createModel();
    return this.serialize(photo);
  }

  @RequestMapping(value="/{id}/analysis/vision", method=RequestMethod.GET)
  public String getAnalysisVision(@PathVariable long id) {
    PhotoModel photo = this.getEntityById(id);

    if (!photo.hasAnalysisVision()) {
      throw new ResourceNotFoundException();
    }

    AnalysisVisionModel analysisVision = photo.fetchAnalysisVision();
    return analysisVision.toJson().toString();
  }

  @RequestMapping(value="/{id}/analysis/speech", method=RequestMethod.GET)
  public String getAnalysisSpeecht(@PathVariable long id) {
    PhotoModel photo = this.getEntityById(id);

    if (!photo.hasAnalysisSpeech()) {
      throw new ResourceNotFoundException();
    }

    AnalysisSpeechModel analysisSpeech = photo.fetchAnalysisSpeech();
    return analysisSpeech.toJson().toString();
  }

  @RequestMapping(value="/{id}/analysis/video-intel", method=RequestMethod.GET)
  public String getAnalysisVideoIntel(@PathVariable long id) {
    PhotoModel photo = this.getEntityById(id);

    if (!photo.hasAnalysisVideoIntel()) {
      throw new ResourceNotFoundException();
    }

    AnalysisVideoIntelModel analysisVideoIntel = photo.fetchAnalysisVideoIntel();
    return analysisVideoIntel.toJson().toString();
  }

  protected BlobInfo getBlobInfo(BlobKey bk) {
    BlobInfoFactory f = new BlobInfoFactory();
    return f.loadBlobInfo(bk);
  }
}
