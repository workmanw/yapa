package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.AlbumModel;
import io.workmanw.yapa.models.PhotoModel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;

@RestController
@RequestMapping("/_tasks")
public class TasksController {
  private static final Logger log = Logger.getLogger(TasksController.class.getName());

  @RequestMapping(value="/post_process", method=RequestMethod.POST)
  public void postProcessModel(@RequestParam String action,
                               @RequestParam String kind,
                               @RequestParam String id) {
    log.info(String.format("%s -- %s -- %s", action, kind, id));
    if (kind.equals("Album")) {
      AlbumModel.postProcess(action, id);
    } else if (kind.equals("Photo")) {
      PhotoModel.postProcess(action, id);
    } else {
      log.severe(String.format("Cannot post-process unknown kind: \"%s\"", kind));
    }
  }
}
