import DS from 'ember-data';

export default DS.Model.extend({
  photoName: DS.attr('string'),
  album: DS.belongsTo('album'),
  createdOn: DS.attr('string'),

  blobKey: DS.attr('string'),
  content: DS.attr('string'),
  filename: DS.attr('string'),
  filesize: DS.attr('number'),
  gcsPath: DS.attr('string'),
  md5: DS.attr('string'),
  servingUrl: DS.attr('string'),

  hasAnalysisVision: DS.attr('boolean'),
  hasAnalysisSpeech: DS.attr('boolean'),
  hasAnalysisVideoIntel: DS.attr('boolean'),

  fetchVisionData() {
    return this.fetchAnalysisData('vision');
  },

  fetchSpeechData() {
    return this.fetchAnalysisData('speech');
  },

  fetchVideoIntelData() {
    return this.fetchAnalysisData('video-intel');
  },

  fetchAnalysisData(type) {
    let modelName = this.constructor.modelName;
    let adapter = this.store.adapterFor(modelName);
    let baseUrl = adapter._buildURL(modelName, this.get('id'));
    return adapter.ajax(`${baseUrl}/analysis/${type}`, 'GET');
  }
});
