package io.workmanw.yapa.models;

import com.google.gson.JsonObject;
import com.jmethods.catatumbo.DatastoreKey;
import com.jmethods.catatumbo.Entity;
import com.jmethods.catatumbo.Identifier;
import com.jmethods.catatumbo.Key;
import com.jmethods.catatumbo.Property;

@Entity(kind="AnalysisSpeech")
public class AnalysisSpeechModel extends BaseModel {
    @Identifier
    protected long id;
    @Key
    protected DatastoreKey key;
    @Property(indexed = false)
    private String transcript;

    public AnalysisSpeechModel() { }

    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public DatastoreKey getKey() {
        return this.key;
    }
    public void setKey(DatastoreKey key) {
        this.key = key;
    }

    public String getTranscript() {
        return this.transcript;
    }
    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public JsonObject toJson() {
        JsonObject speechJsonObj = new JsonObject();
        speechJsonObj.addProperty("transcript", this.getTranscript());
        return speechJsonObj;
    }

    public static AnalysisSpeechModel getById(String sId) { return BaseModel.getById(AnalysisSpeechModel.class, sId); }
    public static AnalysisSpeechModel getById(long id) {
        return BaseModel.getById(AnalysisSpeechModel.class, id);
    }
    public static AnalysisSpeechModel getByKey(DatastoreKey key) { return BaseModel.getByKey(AnalysisSpeechModel.class, key); }
}
