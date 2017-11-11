package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.AlbumModel;
import io.workmanw.yapa.models.PhotoModel;
import io.workmanw.yapa.serializers.RestSerializer;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jmethods.catatumbo.EntityManager;
import com.jmethods.catatumbo.EntityManagerFactory;
import com.jmethods.catatumbo.EntityQueryRequest;
import com.jmethods.catatumbo.QueryResponse;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/_admin")
public class AdminController {
  @RequestMapping(value="/update_album_previews", method=RequestMethod.POST)
  public String updateAlbumPreviews() {
    EntityManagerFactory emf = EntityManagerFactory.getInstance();
    EntityManager em = emf.createDefaultEntityManager();
    EntityQueryRequest request = em.createEntityQueryRequest("SELECT * FROM Album");
    List<AlbumModel> albums = em.executeEntityQueryRequest(AlbumModel.class, request).getResults();

    for (AlbumModel album : albums) {
      request = em.createEntityQueryRequest("SELECT * FROM Photo WHERE album=@1 ORDER BY createdOn DESC LIMIT @2");
      request.addPositionalBinding(album.getKey());
      request.addPositionalBinding(4);
      List<PhotoModel> photos = em.executeEntityQueryRequest(PhotoModel.class, request).getResults();
      List<String> previewImageUrls = new ArrayList<String>();
      for (PhotoModel photo : photos) {
        previewImageUrls.add(photo.getServingUrl());
      }
      album.setPreviewImageUrls(previewImageUrls);
      album.saveModel();
    }

    return new RestSerializer().toString();
  }
}
