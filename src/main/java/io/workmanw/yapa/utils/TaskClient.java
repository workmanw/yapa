package io.workmanw.yapa.utils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.logging.Logger;

public class TaskClient {
  private static final Logger log = Logger.getLogger(TaskClient.class.getName());

  public void scheduleModelPostProcess(String action, String kind, String id) {
    Queue postProcessQueue = QueueFactory.getQueue("post-process");
    TaskOptions taskOpts = TaskOptions.Builder
      .withUrl("/_tasks/post_process")
      .param("action", action)
      .param("kind", kind)
      .param("id", id);
    postProcessQueue.add(taskOpts);
  }
}
