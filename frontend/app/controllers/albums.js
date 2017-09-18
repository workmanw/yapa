import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  actions: {
    createAlbum() {
      this.get('store').createRecord('album', { name: this.get('newAlbumName') }).save();
    }
  }
});
