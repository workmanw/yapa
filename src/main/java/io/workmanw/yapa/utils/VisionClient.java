package io.workmanw.yapa.utils;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageSource;

import com.google.protobuf.ByteString;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VisionClient {
  private static final Logger log = Logger.getLogger(VisionClient.class.getName());

  public JsonArray detectLabelsGcs(String gcsPath) {
    JsonArray jsonArray = new JsonArray();

    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
      List<AnnotateImageRequest> requests = new ArrayList<>();

      ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
      Image img = Image.newBuilder().setSource(imgSource).build();
      Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
      AnnotateImageRequest request =
          AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
      requests.add(request);

      BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse res : responses) {
        if (res.hasError()) {
          log.severe(String.format("Error: %s\n", res.getError().getMessage()));
        } else {
          for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
            JsonObject jsonObj = new JsonObject();
            jsonObj.addProperty("mid", annotation.getMid());
            jsonObj.addProperty("description", annotation.getDescription());
            jsonObj.addProperty("score", annotation.getScore());
            jsonArray.add(jsonObj);
          }
        }
      }
    } catch (IOException e) {
    } catch (Exception e) {
    }

    return jsonArray;
  }

  // TODO landmarks | logos | text
}
