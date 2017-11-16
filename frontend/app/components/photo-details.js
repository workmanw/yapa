import Ember from 'ember';

export default Ember.Component.extend({
  classNames: 'photo-details',
  classNameBindings: 'photo:visible'.w(),

  photo: null,
  collapseDetails: null,

  isImageDocked: false,

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
      this.set('isLoadingAnalysisVision', true);
      photo.fetchVisionData().then(visionData => {
        this.set('selectedVisionData', visionData);
      }).finally(() => {
        this.set('isLoadingAnalysisVision', false);
      });
    } else if (photo.get('hasAnalysisSpeech')) {
      this.set('isLoadingAnalysisSpeech', true);
      photo.fetchSpeechData().then(speechData => {
        this.set('selectedSpeechData', speechData);
      }).finally(() => {
        this.set('isLoadingAnalysisSpeech', false);
      });
    } else if (photo.get('hasAnalysisVideoIntel')) {
      this.set('isLoadingAnalysisVideo', true);
      photo.fetchVideoIntelData().then(videoIntelData => {
        this.set('selectedVideoIntelData', videoIntelData);
      }).finally(() => {
        this.set('isLoadingAnalysisVideo', false);
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
    },

    toggleImageDocking() {
      this.toggleProperty('isImageDocked');
    }
  }
});
