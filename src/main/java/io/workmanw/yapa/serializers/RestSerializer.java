package io.workmanw.yapa.serializers;

import io.workmanw.yapa.models.AlbumModel;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.ArrayList;

public class RestSerializer {
  protected List<AlbumModel> albums;

  public RestSerializer() {
    this.albums = new ArrayList<AlbumModel>();
  }

  public RestSerializer addAlbum(AlbumModel album) {
    this.albums.add(album);
    return this;
  }

  public RestSerializer addAlbums(List<AlbumModel> albums) {
    this.albums.addAll(albums);
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

    return ret;
  }

  public String toString() {
    return this.toJson().toString();
  }
}
