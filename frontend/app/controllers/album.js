import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  actions: {
    deleteAlbum() {
      let album = this.get('album');
      album.destroyRecord();
      this.transitionToRoute('albums');
    },

    droppedFiles(fileUploads) {
      let album = this.get('album');
      album.uploadPhotos(fileUploads);
    }
  }
});
