import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  actions: {
    deleteAlbum(album) {
      album.destroyRecord();
    },

    createAlbum() {
      this.get('store').createRecord('album', { name: this.get('newAlbumName') }).save();
    }
  }
});
