package io.workmanw.yapa.utils;

import io.workmanw.yapa.models.VisionModel;

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

  public List<VisionModel> detectLabels(String gcsPath) {
    return this.processVisionAnalysis(gcsPath, Type.LABEL_DETECTION, VisionModel.TYPE_LABEL);
  }
  public List<VisionModel> detectLandmarks(String gcsPath) {
    return this.processVisionAnalysis(gcsPath, Type.LANDMARK_DETECTION, VisionModel.TYPE_LANDMARK);
  }
  public List<VisionModel> detectLogos(String gcsPath) {
    return this.processVisionAnalysis(gcsPath, Type.LOGO_DETECTION, VisionModel.TYPE_LOGO);
  }
  public List<VisionModel> detectText(String gcsPath) {
    return this.processVisionAnalysis(gcsPath, Type.TEXT_DETECTION, VisionModel.TYPE_TEXT);
  }

  public List<VisionModel> processVisionAnalysis(String gcsPath, Type detectionType, String modelType) {
    List<VisionModel> visionModels = new ArrayList<VisionModel>();

    try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
      List<AnnotateImageRequest> requests = new ArrayList<>();

      ImageSource imgSource = ImageSource.newBuilder().setGcsImageUri(gcsPath).build();
      Image img = Image.newBuilder().setSource(imgSource).build();
      Feature feat = Feature.newBuilder().setType(detectionType).build();
      AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
        .addFeatures(feat).setImage(img).build();
      requests.add(request);

      BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
      List<AnnotateImageResponse> responses = response.getResponsesList();

      for (AnnotateImageResponse resp : responses) {
        if (resp.hasError()) {
          String err = String.format("Error: %s\n", resp.getError().getMessage());
          log.severe(err);
        } else {
          List<VisionModel> mappedVisionModels = this.mapVisionModel(resp, detectionType, modelType);
          visionModels.addAll(mappedVisionModels);
        }
      }
    } catch (IOException e) {
    } catch (Exception e) {
    }

    return visionModels;
  }

  protected List<VisionModel> mapVisionModel(AnnotateImageResponse resp, Type detectionType, String modelType) {
    List<VisionModel> visionModels = new ArrayList<VisionModel>();
    List<EntityAnnotation> annotations = null;

    if (detectionType == Type.LABEL_DETECTION) {
      annotations = resp.getLabelAnnotationsList();
    } else if (detectionType == Type.LANDMARK_DETECTION) {
      annotations = resp.getLandmarkAnnotationsList();
    } else if (detectionType == Type.LOGO_DETECTION) {
      annotations = resp.getLogoAnnotationsList();
    } else if (detectionType == Type.TEXT_DETECTION) {
      annotations = resp.getTextAnnotationsList();
    }

    if (annotations != null) {
      for (EntityAnnotation annotation : annotations) {
        VisionModel visionModel = new VisionModel();
        visionModel.setType(modelType);
        String mid = (annotation.getMid() != null) ? annotation.getMid() : "";
        visionModel.setMid(mid);
        String description = (annotation.getDescription() != null) ? annotation.getDescription() : "";
        visionModel.setDescription(description);
        visionModel.setScore(annotation.getScore());
        visionModels.add(visionModel);
      }
    }

    return visionModels;
  }
}
