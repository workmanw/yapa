package io.workmanw.yapa.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/_tasks")
public class TasksController {
  private static final Logger log = Logger.getLogger(TasksController.class.getName());

  @RequestMapping(value="/post_process_crud", method=RequestMethod.POST)
  public void postProcessModelCrud(@RequestParam String kind,
                                   @RequestParam String id,
                                   @RequestParam String action) {

    log.info(String.format("%s -- %s -- %s", kind, id, action));

    try {
      Class<?> modelClass = this.getModelClass(kind);
      Method postProcessMethod = modelClass.getDeclaredMethod("postProcess", String.class, String.class);
      postProcessMethod.invoke(null, id, action);
    } catch (NoSuchMethodException e) {
      // Nothing to do, just swallow this exception because it means the model has no postProcess defined.
    } catch (Exception e) {
      log.severe(e.toString());
    }
  }

  @RequestMapping(value="/post_process_action", method=RequestMethod.POST)
  public void postProcessModelAction(@RequestParam String kind,
                                     @RequestParam String id,
                                     @RequestParam String action) {

    log.info(String.format("%s -- %s -- %s", kind, id, action));

    try {
      Object modelInstance = this.getModelInstance(kind, id);
      Method actionMethod = modelInstance.getClass().getDeclaredMethod(action);
      actionMethod.invoke(modelInstance);
    } catch (Exception e) {
      log.severe(e.toString());
    }
  }

  protected Class<?> getModelClass(String kind) {
    Class<?> modelClass = null;
    try {
      modelClass = Class.forName("io.workmanw.yapa.models." + kind);
    } catch (Exception e) {
      log.severe(String.format("Could not find model class %s", kind));
      log.severe(e.toString());
    }
    return modelClass;
  }

  protected Object getModelInstance(String kind, String id) {
    Object modelInstance = null;
    try {
      Class<?> modelClass = this.getModelClass(kind);
      Method getByIdMethod = modelClass.getDeclaredMethod("getById", String.class);
      modelInstance = getByIdMethod.invoke(null, id);
    } catch (Exception e) {
      log.severe(String.format("Could not find model instance %s (%s)", kind, id));
      log.severe(e.toString());
    }
    return modelInstance;
  }
}
