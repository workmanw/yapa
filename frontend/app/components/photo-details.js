import Ember from 'ember';

export default Ember.Component.extend({
  classNames: 'photo-details',
  classNameBindings: 'photo:visible'.w(),

  photo: null,
  collapseDetails: null,

  _photoDidChange: Ember.observer('photo', function() {
    this.loadAnalysisData();
  }),

  loadAnalysisData() {
    let photo = this.get('photo');
    if (!photo) { return; }

    this.set('selectedVisionData', null);
    this.set('selectedSpeechData', null);
    this.set('selectedVideoIntelData', null);

    if (photo.get('hasAnalysisVision')) {
      photo.fetchVisionData().then(visionData => {
        this.set('selectedVisionData', visionData);
      });
    } else if (photo.get('hasAnalysisSpeech')) {
      photo.fetchSpeechData().then(speechData => {
        this.set('selectedSpeechData', speechData);
      });
    } else if (photo.get('hasAnalysisVideoIntel')) {
      photo.fetchVideoIntelData().then(videoIntelData => {
        this.set('selectedVideoIntelData', videoIntelData);
      });
    }
  },

  actions: {
    refreshPhoto() {
      let photo = this.get('photo');
      if (photo) {
        this.set('isRefreshingPhoto', true);
        photo.reload().then(() => {
          this.loadAnalysisData();
        }).finally(() => {
          this.set('isRefreshingPhoto', false);
        });
      }
    },

    collapseDetails() {
      Ember.tryInvoke(this.attrs, 'collapseDetails');
    }
  }
});
