package io.workmanw.yapa.utils;

import com.google.api.gax.rpc.OperationFuture;
import com.google.cloud.speech.v1.LongRunningRecognizeMetadata;
import com.google.cloud.speech.v1.LongRunningRecognizeResponse;
import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognitionConfig.AudioEncoding;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.longrunning.Operation;
import io.workmanw.yapa.models.AnalysisSpeechModel;

import java.util.List;
import java.util.logging.Logger;

public class TranscriptionClient {
    private static final Logger log = Logger.getLogger(VisionClient.class.getName());

    public AnalysisSpeechModel analyzeSpeech(String gcsUri) {
        AnalysisSpeechModel speechModel = new AnalysisSpeechModel();
        String transcript = this.transcribeAudio(gcsUri);
        speechModel.setTranscript(transcript);
        return speechModel;
    }

    protected String transcribeAudio(String gcsUri) {
        String speechResult = "";
        // Instantiates a client with GOOGLE_APPLICATION_CREDENTIALS
        try (SpeechClient speech = SpeechClient.create()) {
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
}