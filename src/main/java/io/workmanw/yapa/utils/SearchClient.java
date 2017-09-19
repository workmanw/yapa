package io.workmanw.yapa.utils;

import io.workmanw.yapa.models.PhotoModel;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.StatusCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.util.logging.Logger;


public class SearchClient {
  private static final Logger log = Logger.getLogger(SearchClient.class.getName());

  private static final String PHOTO_INDEX = "photo";

  public void createPhotoDocument(PhotoModel photo) {
    String sId = Long.toString(photo.getId());
    Document doc = Document.newBuilder()
      .setId("photo-" + sId)
      .addField(Field.newBuilder().setName("photoId").setAtom(sId))
      .addField(Field.newBuilder().setName("albumId").setAtom(Long.toString(photo.getAlbum().id())))
      .addField(Field.newBuilder().setName("photoName").setText(photo.getPhotoName()))
      .addField(Field.newBuilder().setName("searchText").setText(photo.getSearchText()))
      .build();
    this.indexPhotoDocument(doc);
  }

  public void updatePhotoDocument(PhotoModel photo) {
    this.createPhotoDocument(photo);
  }

  public void deletePhotoDocument(String photoId) {
    String docId = "photo-" + photoId;
    try {
      this.getPhotoIndex().delete(docId);
    } catch (RuntimeException e) {
      log.severe("Failed to delete documents " + docId);
    }
  }

  public List<Long> searchPhotos(String searchText) {
    Set<Long> photoIds = new HashSet<Long>();

    final int maxRetry = 3;
    int attempts = 0;
    int delay = 2;
    while (true) {
      try {
        String queryString = String.format("searchText = %s", searchText);
        Results<ScoredDocument> results = this.getPhotoIndex().search(queryString);

        // Iterate over the documents in the results
        for (ScoredDocument document : results) {
          Long id = Long.parseLong(document.getOnlyField("photoId").getAtom(), 10);
          photoIds.add(id);
        }
      } catch (SearchException e) {
        if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())
            && ++attempts < maxRetry) {
          // retry
          try {
            Thread.sleep(delay * 1000);
          } catch (InterruptedException e1) {
            // ignore
          }
          delay *= 2; // easy exponential backoff
          continue;
        } else {
          throw e;
        }
      }
      break;
    }

    return new ArrayList<Long>(photoIds);
  }

  protected void indexADocument(Index index, Document document) {
    try {
      final int maxRetry = 3;
      int attempts = 0;
      int delay = 2;
      while (true) {
        try {
          index.put(document);
        } catch (PutException e) {
          if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())
              && ++attempts < maxRetry) { // retrying
            Thread.sleep(delay * 1000);
            delay *= 2; // easy exponential backoff
            continue;
          } else {
            throw e; // otherwise throw
          }
        }
        break;
      }
    } catch(InterruptedException e) {
    }
  }

  protected Index getIndex(String indexName) {
    IndexSpec indexSpec = IndexSpec.newBuilder().setName(indexName).build();
    return SearchServiceFactory.getSearchService().getIndex(indexSpec);
  }

  protected Document getDocument(String indexName, String documentId) {
    Index index = this.getIndex(indexName);
    return index.get(documentId);
  }

  protected Index getPhotoIndex() {
    return this.getIndex(PHOTO_INDEX);
  }

  protected void indexPhotoDocument(Document document) {
    Index photoIndex = this.getPhotoIndex();
    this.indexADocument(photoIndex, document);
  }
}
