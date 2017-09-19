package io.workmanw.yapa.controllers;

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

  @RequestMapping(value="/created_photo", method=RequestMethod.POST)
  public void createdPhoto(@RequestParam("photo") String sPhotoId) {
    PhotoModel photo = PhotoModel.getById(sPhotoId);
    photo.populateVisionData();
    photo.saveModel();
  }
}
