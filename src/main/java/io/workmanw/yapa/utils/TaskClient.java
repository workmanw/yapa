package io.workmanw.yapa.utils;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.util.logging.Logger;

public class TaskClient {
  private static final Logger log = Logger.getLogger(TaskClient.class.getName());

  public void scheduleModelPostProcessCrud(String kind, String id, String action) {
    Queue postProcessQueue = QueueFactory.getQueue("post-process");
    TaskOptions taskOpts = TaskOptions.Builder
      .withUrl("/_tasks/post_process_crud")
      .param("kind", kind)
      .param("id", id)
      .param("action", action);
    postProcessQueue.add(taskOpts);
  }

  public void scheduleModelPostProcessAction(String kind, String id, String action) {
    Queue postProcessQueue = QueueFactory.getQueue("post-process");
    TaskOptions taskOpts = TaskOptions.Builder
      .withUrl("/_tasks/post_process_action")
      .param("id", id)
      .param("kind", kind)
      .param("action", action);
    postProcessQueue.add(taskOpts);
  }
}
