import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  loadPhotos() {
    let albumId = this.get('album.id');

    this.set('photos', null);
    this.set('isLoadingPhotos', true);

    this.get('store').query('photo', { album: albumId }).then(photos => {
      this.set('photos', photos.toArray());
    }).finally(() => {
      this.set('isLoadingPhotos', false);
    });
  },

  actions: {
    deleteAlbum() {
      let album = this.get('album');
      album.destroyRecord();
      this.transitionToRoute('albums');
    },

    droppedFiles(fileUploads) {
      let album = this.get('album');
      fileUploads.forEach(fileUpload => {
        album.uploadPhoto(fileUpload).then(newPhoto => {
          let photos = this.get('photos');
          photos.insertAt(0, newPhoto);
        });
      });
    }
  }
});
