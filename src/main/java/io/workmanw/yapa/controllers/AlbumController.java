package io.workmanw.yapa.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.workmanw.yapa.models.AlbumModel;

@RestController
@RequestMapping("/api/v1/album")
public class AlbumController extends BaseController<AlbumModel> {
  public AlbumController() {
    modelClass = AlbumModel.class;
    entityKind = "Album";
  }
}
