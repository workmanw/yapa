import Ember from 'ember';

export default Ember.Controller.extend({
  store: Ember.inject.service(),

  loadPhotos() {
    let albumId = this.get('album.id');

    this.set('selectedPhoto', null);
    this.set('photos', null);
    this.set('isLoadingPhotos', true);

    this.get('store').query('photo', { album: albumId }).then(photos => {
      this.set('photos', photos.toArray());
    }).finally(() => {
      this.set('isLoadingPhotos', false);
    });
  },

  actions: {
    editAlbum() {
      this.set('showEditAlbumModal', true);
    },

    hideEditAlbum() {
      this.get('album').rollbackAttributes();
      this.set('showEditAlbumModal', false);
    },

    saveAlbum() {
      this.set('isSavingAlbum', true);
      this.get('album').save().then(() => {
        this.set('showEditAlbumModal', false);
      }).finally(() => {
        this.set('isSavingAlbum', false);
      });
    },

    deleteAlbum() {
      let confirmed = window.confirm('Are you sure?');
      if (confirmed) {
        this.set('isSavingAlbum', true);
        this.get('album').destroyRecord().then(() => {
          this.transitionToRoute('albums');
        }).finally(() => {
          this.set('isSavingAlbum', false);
        });
      }
    },

    selectPhoto(photo) {
      this.set('selectedPhoto', photo);
    },

    refreshSelectedPhoto() {
      let selectedPhoto = this.get('selectedPhoto');
      if (selectedPhoto) {
        this.set('isRefreshingPhoto', true);
        selectedPhoto.reload().finally(() => {
          this.set('isRefreshingPhoto', false);
        });
      }
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
