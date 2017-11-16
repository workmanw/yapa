import Ember from 'ember';

export default Ember.Component.extend({
  classNames: 'photo-placeholder',

  photo: null,
  'on-click': null,
  larger: false,

  isImage: Ember.computed.reads('photo.isImage'),
  isAudio: Ember.computed.reads('photo.isAudio'),
  isVideo: Ember.computed.reads('photo.isVideo'),

  iconClass: Ember.computed('isImage', 'isAudio', 'isVideo', function() {
    let isImage = this.get('isImage'),
        isAudio = this.get('isAudio'),
        isVideo = this.get('isVideo');

    if (isImage) {
      return 'picture-o';
    } else if (isAudio) {
      return 'volume-up';
    } else if (isVideo) {
      return 'video-camera';
    } else {
      return 'question-circle';
    }
  }),

  iconSize: Ember.computed('larger', function() {
    return this.get('larger') ? 5 : 3;
  }),

  click() {
    this._super(...arguments);
    Ember.tryInvoke(this.attrs, 'on-click');
  }
});
