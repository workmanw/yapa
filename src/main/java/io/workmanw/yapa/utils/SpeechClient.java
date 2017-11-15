package io.workmanw.yapa.utils;

import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.language.v1.*;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.longrunning.Operation;
import io.workmanw.yapa.models.AnalysisSpeechModel;
import io.workmanw.yapa.models.AnalysisSpeechModel.EntityItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SpeechClient {
  private static final Logger log = Logger.getLogger(VisionClient.class.getName());

  public AnalysisSpeechModel analyzeSpeech(String gcsUri) {
    String transcript = this.transcribeAudio(gcsUri);

    List<EntityItem> entityItems = this.analyizeText(transcript);

    AnalysisSpeechModel speechModel = new AnalysisSpeechModel();
    speechModel.setTranscript(transcript);
    speechModel.setEntities(entityItems);

    return speechModel;
  }

  protected String transcribeAudio(String gcsUri) {
    String speechResult = "";
    // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
    try (com.google.cloud.speech.v1.SpeechClient speech = com.google.cloud.speech.v1.SpeechClient.create()) {
      // Configure remote file request for Linear16
      RecognitionConfig config = RecognitionConfig.newBuilder()
              .setEncoding(AudioEncoding.FLAC)
              .setLanguageCode("en-US")
              .build();
      RecognitionAudio audio = RecognitionAudio.newBuilder()
              .setUri(gcsUri)
              .build();

      // Use non-blocking call for getting file transcription
      OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata,
              Operation> response =
              speech.longRunningRecognizeAsync(config, audio);

      while (!response.isDone()) {
        System.out.println("Waiting for response...");
        Thread.sleep(10000);
      }

      List<SpeechRecognitionResult> results = response.get().getResultsList();

      for (SpeechRecognitionResult result : results) {
        // There can be several alternative transcripts for a given chunk of speech. Just use the
        // first (most likely) one here.
        SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
        speechResult += alternative.getTranscript();
      }

      speech.close();
    } catch (Exception e) {
      log.severe(e.toString());
    }

    return speechResult;
  }

  public List<EntityItem> analyizeText(String text) {
    List<EntityItem> entityItems = new ArrayList<>();

    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
              .setContent(text)
              .setType(Document.Type.PLAIN_TEXT)
              .build();
      AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
              .setDocument(doc)
              .setEncodingType(EncodingType.UTF16)
              .build();

      AnalyzeEntitiesResponse response = language.analyzeEntities(request);

      // Print the response
      for (Entity entity : response.getEntitiesList()) {
        EntityItem entityItem = new EntityItem();

        String type = entity.getType().toString();
        type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();

        entityItem.setName(entity.getName());
        entityItem.setType(type);
        entityItem.setSalience(entity.getSalience());
        for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
          if (entry.getKey().equals("mid")) {
            entityItem.setMid(entry.getValue());
          } else if (entry.getKey().equals("wikipedia_url")) {
            entityItem.setWikipediaUrl(entry.getValue());
          }
        }

        entityItems.add(entityItem);
      }
    } catch (Exception e) {
      log.severe(e.toString());
    }

    return entityItems;
  }
}