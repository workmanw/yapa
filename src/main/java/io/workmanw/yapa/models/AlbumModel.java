package io.workmanw.yapa.models;

import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;

@Entity
public class AlbumModel {
  @Identifier
  private String id;

  private String name;

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
