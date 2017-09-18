import io.workmanw.yapa.controllers.PhotoController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@WebServlet(name = "_photo_upload", value = "/_photo_upload")
public class PhotoUploadServlet extends HttpServlet {
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
    throws ServletException, IOException {

    Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(req);
    List<BlobKey> blobKeys = blobs.get("file");

    Map<String, String> parameters = this.mapParameters(req);
    BlobKey blobKey = blobKeys.get(0);
    PhotoController pc = new PhotoController();
    res.getWriter().println(pc.uploadCallback(blobKey, parameters));
  }

  protected Map<String, String> mapParameters(HttpServletRequest req) {
    Map<String, String> map = new HashMap<String, String>();
    Enumeration<String> parameterNames = req.getParameterNames();
    while (parameterNames.hasMoreElements()) {
      String key = (String) parameterNames.nextElement();
      String val = req.getParameter(key);
      map.put(key, val);
    }
    return map;
  }
}
