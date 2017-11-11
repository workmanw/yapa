package io.workmanw.yapa.utils;

import io.workmanw.yapa.models.AnalysisVisionModel;
import io.workmanw.yapa.models.AnalysisVisionModel.VisionItem;

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

import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class VisionClient {
  private static final Logger log = Logger.getLogger(VisionClient.class.getName());

  public AnalysisVisionModel analyzeImage(String gcsPath) {
    List<VisionItem> visionLabels = this.processVisionAnalysis(gcsPath, Type.LABEL_DETECTION, VisionItem.TYPE_LABEL);
    List<VisionItem> visionLandmarks = this.processVisionAnalysis(gcsPath, Type.LANDMARK_DETECTION, VisionItem.TYPE_LANDMARK);
    List<VisionItem> visionLogos = this.processVisionAnalysis(gcsPath, Type.LOGO_DETECTION, VisionItem.TYPE_LOGO);
    List<VisionItem> visionTexts = this.processVisionAnalysis(gcsPath, Type.TEXT_DETECTION, VisionItem.TYPE_TEXT);

    List<VisionItem> allVisionItems = new ArrayList<>();
    allVisionItems.addAll(visionLabels);
    allVisionItems.addAll(visionLandmarks);
    allVisionItems.addAll(visionLogos);
    allVisionItems.addAll(visionTexts);

    return new AnalysisVisionModel(allVisionItems);
  }

  public List<VisionItem> processVisionAnalysis(String gcsPath, Type detectionType, String modelType) {
    List<VisionItem> visionModels = new ArrayList<VisionItem>();

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
          List<VisionItem> mappedVisionItems = this.mapVisionModel(resp, detectionType, modelType);
          visionModels.addAll(mappedVisionItems);
        }
      }
    } catch (IOException e) {
    } catch (Exception e) {
    }

    return visionModels;
  }

  protected List<VisionItem> mapVisionModel(AnnotateImageResponse resp, Type detectionType, String modelType) {
    List<VisionItem> visionModels = new ArrayList<>();
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
        VisionItem visionItem = new VisionItem();
        visionItem.setType(modelType);
        String mid = (annotation.getMid() != null) ? annotation.getMid() : "";
        visionItem.setMid(mid);
        String description = (annotation.getDescription() != null) ? annotation.getDescription() : "";
        visionItem.setDescription(description);
        visionItem.setScore(annotation.getScore());
        visionModels.add(visionItem);
      }
    }

    return visionModels;
  }
}
