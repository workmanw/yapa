package io.workmanw.yapa.controllers;

import io.workmanw.yapa.models.PhotoModel;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

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

  public static void scheduleCreatePhoto(PhotoModel photo) {
    Queue postProcessQueue = QueueFactory.getQueue("post-process");
    TaskOptions taskOpts = TaskOptions.Builder.withUrl("/_tasks/created_photo")
      .param("photo", Long.toString(photo.getId()));
    postProcessQueue.add(taskOpts);
  }
}
