import Ember from 'ember';

export default Ember.Component.extend({
  classNames: 'photo-list-item',
  classNameBindings: 'square'.w(),

  photo: null,
  square: false,
  'on-click': null,

  isImage: Ember.computed.reads('photo.isImage'),
  isAudio: Ember.computed.reads('photo.isAudio'),
  isVideo: Ember.computed.reads('photo.isVideo'),

  actions: {
    clicked() {
      Ember.tryInvoke(this.attrs, 'on-click');
    }
  }
});
