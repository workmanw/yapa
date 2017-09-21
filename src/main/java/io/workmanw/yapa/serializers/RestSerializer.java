package io.workmanw.yapa.serializers;

import io.workmanw.yapa.models.AlbumModel;
import io.workmanw.yapa.models.PhotoModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.ArrayList;

public class RestSerializer {
  protected List<AlbumModel> albums;
  protected List<PhotoModel> photos;

  public RestSerializer() {
    this.albums = new ArrayList<AlbumModel>();
    this.photos = new ArrayList<PhotoModel>();
  }

  public RestSerializer addAlbum(AlbumModel album) {
    this.albums.add(album);
    return this;
  }

  public RestSerializer addAlbums(List<AlbumModel> albums) {
    this.albums.addAll(albums);
    return this;
  }

  public RestSerializer addPhoto(PhotoModel photo) {
    this.photos.add(photo);
    return this;
  }

  public RestSerializer addPhotos(List<PhotoModel> photos) {
    this.photos.addAll(photos);
    return this;
  }

  public JsonObject toJson() {
    JsonObject ret = new JsonObject();

    if (this.albums.size() > 0) {
      JsonArray albumsJson = new JsonArray();
      for (AlbumModel album : this.albums) {
        albumsJson.add(album.toJson());
      }
      ret.add("album", albumsJson);
    }
    if (this.photos.size() > 0) {
      JsonArray photosJson = new JsonArray();
      for (PhotoModel photo : this.photos) {
        photosJson.add(photo.toJson());
      }
      ret.add("photo", photosJson);
    }

    return ret;
  }

  // protected JsonArray _serializeEntities(BaseModel m,)

  public String toString() {
    return this.toJson().toString();
  }
}
